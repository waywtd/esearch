package com.globalegrow.esearch.hive.process

import com.globalegrow.esearch.hive.bean.JdbcLinkConfig
import com.globalegrow.esearch.hive.utils.{ParamUtils, SparkJDBCUtils, SparkUtils}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, Row, SQLContext}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object CategoryTransform {

  /**
    * jdbc_url,
    * jdbc_username,
    * jdbc_password,
    * sku_category_sql,
    * hdfs_path,
    * @param args
    */
  def main(args: Array[String]): Unit = {
    //获取配置信息组成jdbcconfig
    val paramMap = ParamUtils.getParamMap(args)
    val jdbcConfig = new JdbcLinkConfig(jdbcUrl = paramMap("jdbc_url"),
      jdbcUsername = paramMap("jdbc_username"),
      jdbcPassword = paramMap("jdbc_password")
    )

    //初始化环境
    val conf = new SparkConf().setAppName("category-dispose").setMaster("local[*]")
    val sqlContext = new SQLContext(new SparkContext(conf))

    //从mysql获取数据
    val categoryDF = SparkJDBCUtils.getDataFrame(sqlContext, jdbcConfig, "category", paramMap("sku_category_sql"))
    val categoryRelationDF = SparkJDBCUtils.getDataFrame(sqlContext, jdbcConfig, "category_relation", paramMap("sku_category_relation_sql"))

    //merge,transform获取最后结果
    val categoryDisposedDF = getCatIdCategoriesDFFromCategory(categoryDF)
    val resultDF = categoryRelationDF.join(categoryDisposedDF, categoryRelationDF("category_id") === categoryDisposedDF("catId"), "left_outer")

    //存入hdfs
//    val rdd = SparkUtils.getOrderRDD(resultDF, Array("goodsSn","catId", "catId3", "catId2", "catId1", "topCatId"))
    val rdd = SparkUtils.getOrderRDD(resultDF, paramMap("order_columns").split(","))
    rdd.saveAsTextFile(paramMap("hdfs_path"))
  }

  def getCatIdCategoriesDFFromCategory(goodsCategoryDF: DataFrame): DataFrame = {
    //从hdfs上获取数据，得到相应rdd
    val sc = goodsCategoryDF.sqlContext.sparkContext
    val goodsCategoryRDD = goodsCategoryDF.rdd



    //将rdd转成catId-name-parentid的map结构,发送成广播变量
    val catId2CategoryMessageMap = goodsCategoryRDD.aggregate(mutable.HashMap[Int, Int]())({ case (resultMap, row: Row) =>
      resultMap += (row.getAs[Int]("catId") -> row.getAs[Int]("parentId"));
      resultMap
    },
      { case (m1, m2) => m1 ++= m2 }
    )
    //广播
    val catId2CategoryMessageMapBC = sc.broadcast(catId2CategoryMessageMap)

    import goodsCategoryDF.sqlContext.implicits._

    //将每一条数据循环在map中找出id，得出一个list数组，然后得出最后结果
    goodsCategoryRDD.map(row => {
      var catId = row.getAs[Int]("catId")
      var categoryMessageList = ListBuffer[Int]()
      val catId2CategoryMessageMap = catId2CategoryMessageMapBC.value
      while (catId != 0) {
        if (catId2CategoryMessageMap.get(catId) != None) {
          val parentId = catId2CategoryMessageMap.get(catId).get
          categoryMessageList += catId
          catId = parentId
        } else {
          catId = 0
        }

      }

      val topCatId = categoryMessageList(categoryMessageList.size - 1)

      if (categoryMessageList.size < 4) {
        for (a <- 1 to (4 - categoryMessageList.size)) {
          categoryMessageList.append(0)
        }
      }

      categoryMessageList = categoryMessageList.splitAt(4)._1
      (categoryMessageList(0), categoryMessageList(1), categoryMessageList(2), categoryMessageList(3), topCatId)
    }).toDF("catId", "catId3", "catId2", "catId1", "topCatId")
  }
}
