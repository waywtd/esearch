package com.globalegrow.esearch.realtime.utils

import java.text.SimpleDateFormat
import java.util.Date

object DataTest {
  def main(args: Array[String]): Unit = {
   /* val inPath2 = "/esearch/hadoop-new/data/gearbest2/result/sku"
    val outPath = "/output"

    val conf = new SparkConf().setMaster("local[*]").setAppName("load-data")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)



    val dataFrame = sqlContext.read.parquet(inPath2)
    dataFrame.registerTempTable("tempdata")
    sqlContext.read.parquet("/esearch/hadoop-new/data/gearbest2/index/es_sku_category").registerTempTable("categorytemp")
    val resultDF3 = sqlContext.sql("select td.whCode,td.goodsSn,td.goodsTitle, categorytemp.catName catName from tempdata td left join categorytemp on td.catId = categorytemp.catId")
    resultDF3.rdd.map(row => row.mkString("\001")).repartition(1).saveAsTextFile(outPath + "3")*/

//    DateUtil.getDay(new Date())

   /* val format = new SimpleDateFormat("yyyy-MM-ddd hhh:mmm:ss")
    val value = format.format(new Date())*/

    val logStr = ""

  }
}
