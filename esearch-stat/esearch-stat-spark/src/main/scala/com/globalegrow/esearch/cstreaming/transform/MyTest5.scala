package com.globalegrow.esearch.cstreaming.transform

import java.util.Properties

import com.globalegrow.esearch.cstreaming.udfs.UserFunctions
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext

object MyTest5 {
  def main1(args: Array[String]): Unit = {
    val columns = "121" :: "128" :: Nil
     val orderTableFromOrderSn = UserFunctions.getClass.getMethod("getOrderTableFromOrderSn", columns.map(str => classOf[Any]):_*)
    val result = orderTableFromOrderSn.invoke(UserFunctions,columns:_*)
    println(orderTableFromOrderSn)
  }

  def main(args: Array[String]): Unit = {
    val sql2 = "SELECT oi.order_sn,oi.parent_order_sn,CONCAT(og.goods_sn, '#', og.warehouse_code) AS goodsId, og.goods_amount AS amount FROM order_info_45 oi LEFT JOIN order_goods_45 og ON oi.order_sn= og.order_sn LEFT JOIN order_info_extend_45 oie ON oie.order_sn = og.order_sn WHERE oie.is_test = 0 AND oi.order_sn = '18112300955700117378' AND og.goods_sn = '294316601' AND og.warehouse_code = '1433363'"
    val user = "search_user"
    val password = "qazschwsx"
    val url2 = "jdbc:mysql://10.93.157.72:3306/gb_order?tinyInt1isBit=false"
    val url = "jdbc:es://http://cdh-sl-search01:9200"
    val driver = "org.elasticsearch.xpack.sql.jdbc.jdbc.JdbcDriver"

    val conf = new SparkConf().setMaster("local[*]").setAppName("aaa")
    val sqlContext = new SQLContext(new SparkContext(conf))

    val properties = new Properties()
   /* properties.setProperty("user", user)
    properties.setProperty("password", password)*/
    properties.setProperty("driver", driver)
    val sql3 = "(select * from gb_addcart_data where glb_u in ('6465576960800542720') or glb_od in ('foqdrdhvoogo1542950351330'))v"
    val sql = "(select * from gb_addcart_data where glb_dc = '1301' limit 10)v"

//    val frame = sqlContext.read.jdbc(url, sql, properties)
//    frame.show(false)
//    spark.sql("CREATE TEMPORARY TABLE addcart_data USING org.elasticsearch.spark.sql OPTIONS (resource 'gb_addcart_data/data', nodes 'cdh-sl-search01')").registerTempTable("addcart_data")
//    val frame = spark.sql("select * from addcart_data where glb_u in ('6465576960800542720') or glb_od in ('foqdrdhvoogo1542950351330')")
//    frame.show(false)
//    val frame = frame
  }
}
