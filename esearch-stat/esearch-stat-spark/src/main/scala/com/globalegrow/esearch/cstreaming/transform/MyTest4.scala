package com.globalegrow.esearch.cstreaming.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable

object MyTest4 {
  def main(args: Array[String]): Unit = {
     val conf = new SparkConf().setAppName("a").setMaster("local[*]")
     val sqlContext = new SQLContext(new SparkContext(conf))
     val rdd = sqlContext.sparkContext.parallelize(Array(("1","2"), ("3","4")))
    val rdd2 = sqlContext.sparkContext.parallelize(Array(("1","2"), ("3","4")))
    import sqlContext.implicits._
    val frame = rdd.toDF("a","b")
    val frame2 = rdd2.toDF("a","b")
    frame2.registerTempTable("df2")
    frame.registerTempTable("df1")
    sqlContext.sql("select * from df1 left join df2 on ((df1.a = df2.a or df1.b = df2.b) and df1.a = concat(df2.a,df2.b))").show(false)
//    val frame1 = sqlContext.registerTempTable("df1")
    val json3 = "{\n\t\"geo_city\": \"LILLE\",\n\t\"geo_country\": \"FR\",\n\t\"glb_at\": \"NILL\",\n\t\"glb_b\": \"c\",\n\t\"glb_bd\": \"NILL\",\n\t\"glb_bv\": \"NILL\",\n\t\"glb_cl\": \"https://fr.gearbest.com/scooters-and-wheels/pp_974669.html?wid=1349303#goodsDetail\",\n\t\"glb_cl_cat_id\": \"NILL\",\n\t\"glb_cl_search_word\": \"NILL\",\n\t\"glb_d\": \"10002\",\n\t\"glb_dc\": \"1310\",\n\t\"glb_filter\": \"NILL\",\n\t\"glb_filter_page\": \"NILL\",\n\t\"glb_filter_sort\": \"NILL\",\n\t\"glb_filter_view\": \"NILL\",\n\t\"glb_k\": \"NILL\",\n\t\"glb_ksku\": \"231480802\",\n\t\"glb_od\": \"yncipjnuhqtz1542617095460\",\n\t\"glb_oi\": \"dfed8be74425ff4ed5510e89609e2cd0\",\n\t\"glb_ol\": \"NILL\",\n\t\"glb_olk\": \"4278732\",\n\t\"glb_osr\": \"ol=https://www.google.fr/|href=https://fr.gearbest.com/promotion-amateur-de-xiaomi-mi-fans-special-2950.html?vip=4278732\",\n\t\"glb_p\": \"NILL\",\n\t\"glb_pl\": \"https://fr.gearbest.com/scooters-and-wheels/pp_974668.html?wid=1349303\",\n\t\"glb_plf\": \"pc\",\n\t\"glb_pm\": \"mb\",\n\t\"glb_s\": \"NILL\",\n\t\"glb_sc\": \"NILL\",\n\t\"glb_sckw\": \"NILL\",\n\t\"glb_siws\": \"NILL\",\n\t\"glb_sk\": \"NILL\",\n\t\"glb_skuinfo\": \"\",\n\t\"glb_skuinfo_sku\": \"231480802\",\n\t\"glb_sl\": \"NILL\",\n\t\"glb_t\": \"ic\",\n\t\"glb_tm\": \"1542703963530\",\n\t\"glb_u\": \"NILL\",\n\t\"glb_ubcta\": \"\",\n\t\"glb_ubcta_sckw\": \"xiaomi m365\",\n\t\"glb_w\": \"210551\",\n\t\"glb_x\": \"BDR\",\n\t\"log_src_time\": \"2018-11-20 08:52:43\",\n\t\"log_time\": \"2018-11-20 16:52:43\",\n\t\"message_bytes\": \"372\",\n\t\"message_cliip\": \"2.5.0.149\",\n\t\"message_reqhost\": \"s.logsss.com\",\n\t\"message_status\": \"200\",\n\t\"netperf_edgeip\": \"23.200.87.59\",\n\t\"network_edgeip\": \"23.200.87.59\",\n\t\"osr_landing\": \"NILL\",\n\t\"osr_referrer\": \"NILL\",\n\t\"other\": \"NILL\",\n\t\"reqhdr_referer\": \"https://fr.gearbest.com/scooters-and-wheels/pp_974669.html?wid=1349303\"\n}"
    val json2 = "{\"geo_city\": \"LILLE\", \"geo_country\": \"FR\"," +
      " \"glb_at\": \"NILL\", \"glb_b\": \"c\", \"glb_bd\": \"NILL\"," +
      " \"glb_bv\": \"NILL\", " +
      "\"glb_cl\": \"https://fr.gearbest.com/scooters-and-wheels/pp_974669.html?wid=1349303#goodsDetail\", " +
      "\"glb_cl_cat_id\": \"NILL\", " +
      "\"glb_cl_search_word\": \"NILL\", \"glb_d\": \"10002\", \"glb_dc\": \"1310\"," +
      " \"glb_filter\": \"NILL\", \"glb_filter_page\": \"NILL\", \"glb_filter_sort\": \"NILL\"," +
      " \"glb_filter_view\": \"NILL\", \"glb_k\": \"NILL\", \"glb_ksku\": \"231480802\"," +
      " \"glb_od\": \"yncipjnuhqtz1542617095460\", \"glb_oi\": \"dfed8be74425ff4ed5510e89609e2cd0\", " +
      "\"glb_ol\": \"NILL\", \"glb_olk\": \"4278732\"," +
      " \"glb_osr\": \"ol=https://www.google.fr/|href=https://fr.gearbest.com/promotion-amateur-de-xiaomi-mi-fans-special-2950.html?vip=4278732\", " +
      "\"glb_p\": \"NILL\", \"glb_pl\": \"https://fr.gearbest.com/scooters-and-wheels/pp_974668.html?wid=1349303\", " +
      "\"glb_plf\": \"pc\", \"glb_pm\": \"mb\", \"glb_s\": \"NILL\", " +
      "\"glb_sc\": \"NILL\", \"glb_sckw\": \"NILL\", \"glb_siws\": \"NILL\", " +
      "\"glb_sk\": \"NILL\", \"glb_skuinfo\": \"{\"sku\":\"231480802\",\"pam\":1," +
      "\"pc\":\"11564\",\"k\":\"1349303\",\"zt\":0}\", \"glb_skuinfo_sku\": \"231480802\", " +
      "\"glb_sl\": \"NILL\", \"glb_t\": \"ic\", \"glb_tm\": \"1542703963530\"," +
      " \"glb_u\": \"NILL\", \"glb_ubcta\": \"{\"fmd\":\"mp\",\"sk\":\"L\"," +
      "\"sc\":\"Toutes Catégories \",\"sckw\":\"xiaomi m365\",\"k\":\"1349303\"}\", " +
      "\"glb_ubcta_sckw\": \"xiaomi m365\", \"glb_w\": \"210551\", \"glb_x\": \"BDR\", " +
      "\"log_src_time\": \"2018-11-20 08:52:43\", \"log_time\": \"2018-11-20 16:52:43\"," +
      " \"message_bytes\": \"372\", \"message_cliip\": \"2.5.0.149\", " +
      "\"message_reqhost\": \"s.logsss.com\", \"message_status\": \"200\", " +
      "\"netperf_edgeip\": \"23.200.87.59\", \"network_edgeip\": \"23.200.87.59\", " +
      "\"osr_landing\": \"NILL\", \"osr_referrer\": \"NILL\", \"other\": \"NILL\", " +
      "\"reqhdr_referer\": \"https://fr.gearbest.com/scooters-and-wheels/pp_974669.html?wid=1349303\" }"
    val json = "{\"geo_city\": \"LILLE\", \"geo_country\": \"FR\"}"
    val rdd6 :RDD[String]= sqlContext.sparkContext.parallelize(json3 ::Nil)
    val schema = sqlContext.read.json(rdd6).schema
      rdd6.collect()
    sqlContext.sql("select df1.a from df2 left join df1")
    val rdd3 = sqlContext.sparkContext.emptyRDD[Row]
    val df4 = sqlContext.createDataFrame(rdd3, schema)
    df4.registerTempTable("df4")
    sqlContext.sql("select * from df4").show(false)
    println(df4.columns.length)
  }

  def main1(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("a").setMaster("local[*]")
    val ssc = new StreamingContext(conf, Seconds(1))
    ssc.checkpoint("/tmp/check1")

    val kafkaMap = mutable.Map[String,String]("bootstrap.servers" -> "cdh-search01:9092",
      "group.id" -> "a",
    "key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
    "value.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer")
    val kafkaStreaming1 = KafkaUtils.createDirectStream(ssc, LocationStrategies.PreferConsistent, ConsumerStrategies.Subscribe[String,String](Iterable("10002-order"), kafkaMap))
    val kafkaStreaming2 = KafkaUtils.createDirectStream(ssc, LocationStrategies.PreferConsistent, ConsumerStrategies.Subscribe[String,String](Iterable("10002-order"), kafkaMap))
    kafkaStreaming1.print(10)
    kafkaStreaming2.print(10)

    ssc.start()
    ssc.awaitTermination()
  }
}
