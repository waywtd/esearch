//package com.globalegrow.esearch.realtime.streaming
//
//import java.sql.Connection
//import java.text.SimpleDateFormat
//import java.util.Date
//
//import com.globalegrow.esearch.realtime.conf.ConfigurationManager
//import com.globalegrow.esearch.realtime.utils._
//import kafka.serializer.StringDecoder
//import org.apache.commons.dbutils.QueryRunner
//import org.apache.commons.lang3.StringUtils
//import org.apache.commons.pool2.ObjectPool
//import org.apache.kafka.clients.consumer.ConsumerConfig
//import org.apache.logging.log4j.LogManager
//import org.apache.spark.SparkConf
//import org.apache.spark.streaming.{Seconds, StreamingContext}
//import org.apache.spark.streaming.dstream.DStream.toPairDStreamFunctions
//import org.apache.spark.streaming.kafka.KafkaUtils
//import org.globalegrow.esearch.constant.Constants
//
//object IpTopNCount {
//  private val SPLIT_STR = "^A"
//  private val DURATION_TIME = 1000 * 3600 * 65
//
//  private val logger = LogManager.getLogger(IpTopNCount.getClass)
//  private val queryRunner = new QueryRunner with Serializable
//
//  def createStreamingContext(mainTopic :String, conf :SparkConf, checkpointDir:String) :StreamingContext = {
//
//    val ssc = new StreamingContext(conf, Seconds(getProp(s"$mainTopic.streaming.duration.senconds").toInt))
//    ssc.checkpoint(checkpointDir)
//
//    //连接kafka
//    val kafkaParams: Map[String, String] =
//      Map(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> getProp(s"$mainTopic.streaming.bootstrap.servers"),
//        ConsumerConfig.GROUP_ID_CONFIG -> getProp(s"$mainTopic.streaming.group.id"),
//        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> getProp(s"$mainTopic.streaming.auto.offset.reset"),
//        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.StringDeserializer",
//        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.StringDeserializer",
//        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG -> "false"
//      )
//    var topics: Set[String] = Set(getProp(s"$mainTopic.streaming.consumer.kafka.topics"))
//
//    val sdf = new SimpleDateFormat("yyyy-MM-dd") with  Serializable
//
//
//    val orderNum = getProp(s"$mainTopic.streaming.count.num").toInt
//    val km = new KafkaManager(kafkaParams)
//    val kafkaDStreaming = km.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics)
//
//    //处理kafka数据
//    //使用ip和时间作为key,使用updateStateByKey存储,累加
//    val ipDateWebsiteLang2OneDStream = kafkaDStreaming.map{case (recordKey, recordValue) =>
//      val ip = recordValue.substring(0, recordValue.indexOf(SPLIT_STR))
//      val dateOption = "\\d{4}-\\d{2}-\\d{2}".r.findFirstIn(recordValue)
//      val resultMap = JsonUtils.getMapFromLogStr(recordValue)
//      val langStr = "glb_dc=(\\d)+".r.findFirstIn(resultMap(Constants.KAFKA_REQUEST_PARAM)).getOrElse("error=error").split("=")(1)
//      var website = "[glb_|ubc]d=(\\d)+".r.findFirstIn(resultMap(Constants.KAFKA_REQUEST_PARAM)).getOrElse("error=error").split("=")(1)
//      val lang = JsonUtils.langStr2RegualStr(langStr)
//      if(website == "10002") website = "GB"
//      val date = dateOption.get
//      if (langStr == "error" || website == "error") ("error", 1)
//      else
//        (ip + "_" + date + "_" + website + "_" + lang, 1)
//    }.filter(_._1 != "error")
//
//
//    var dateList :Array[String] = Array()
//
//    val ipDateWebsiteLang2CountDStream = ipDateWebsiteLang2OneDStream.updateStateByKey[Int]((values: Seq[Int], preValueOption: Option[Int]) => {
//      val sum = values.sum
//      var resultValue = sum
//      resultValue += preValueOption.getOrElse(0)
//      Some(resultValue)
//    }).filter({case (key,count) => {
//      val logDateTime = sdf.parse(key.split("_")(1)).getTime
//      val minDateTime = System.currentTimeMillis() - DURATION_TIME
//      logDateTime > minDateTime
//    }})
//
//
//    // 排序，取出前50个
//
//    ipDateWebsiteLang2CountDStream.foreachRDD(rdd => {
//      var resultRDD  = rdd
//      if (!dateList.isEmpty) {
//        resultRDD = rdd.filter({case (ipDateWebsiteLang, count) => ipDateWebsiteLang.split("_")(1) == dateList.max})
//      }
//      var topNIp = resultRDD.groupBy({case (str,count) => str.substring(str.indexOf("_") + 1)}).mapValues(_.toList.sortWith({case (a,b) => a._2 > b._2}).take(orderNum)).flatMapValues(_.iterator).collect()
//      if(!topNIp.isEmpty) {
//        val pool = getPool(mainTopic)
//        val connection = pool.borrowObject()
//        connection.setAutoCommit(false)
//        val params1 : Array[Array[AnyRef]]= topNIp.map(str => str._2._1.substring(str._2._1.indexOf("_") + 1)).distinct.map(_.split("_").map(_.asInstanceOf[AnyRef]))
//        queryRunner.batch(connection, "delete from stdb_realtime_ip_count_10002 where date = ? and pipeline_code =? and  lang = ?",
//          params1)
//        val param2 = topNIp.map{case (str,(str2, count)) =>
//          val attrs = str2.split("_")
//          Array(attrs(2), attrs(3), attrs(1), attrs(0), count, "sparkstreaming", new Date(), "A").map(_.asInstanceOf[AnyRef])
//        }
//        queryRunner.batch(connection, "insert into stdb_realtime_ip_count_10002  (pipeline_code, lang, date, click_ip, click_count,audit_id,audit_time,audit_status) values (?,?,?,?,?,?,?,?)", param2)
//        connection.commit()
//        pool.returnObject(connection)
//        logger.info(s"update top $orderNum: " + topNIp.mkString("\n"))
//      }
//
//    }
//
//
//
//    )
//
//     //更新zk中的offset
//      kafkaDStreaming.foreachRDD(rdd => {
////      if (!rdd.isEmpty)
////        km.updateZKOffsets(rdd)
//    })
//
//    ssc
//  }
//
////  private val sql1 = ""
//  def main(args: Array[String]): Unit = {
//
//    val mainTopic = "ip_click"
//    val conf = SparkRealTimeUtils.getSparkConf(mainTopic)
//    val checkpointDir = getProp(s"$mainTopic.streaming.checkpoint.dir") + "/" + getProp(s"$mainTopic.streaming.app.name")
//    val ssc = StreamingContext.getOrCreate(checkpointDir, () => createStreamingContext(mainTopic, conf, checkpointDir))
//
//
//    //持续连接
//    ssc.start()
//    ssc.awaitTermination()
//  }
//
//
//
//
//  /**
//    * ip_click.jdbc.mysql.url=10
//    * ip_click.jdbc.mysql.port=3306
//    * ip_click.jdbc.mysql.username=root
//    * ip_click.jdbc.mysql.password=test1qaz2wsx
//    *jdbc:mysql://localhost:3306/database
//    * @param mainTopic
//    * @return
//    */
//  private def getPool(mainTopic: String): ObjectPool[Connection] = {
//    val url = getProp(s"$mainTopic.jdbc.mysql.url")
//    val username = getProp(s"$mainTopic.jdbc.mysql.username")
//    val password = getProp(s"$mainTopic.jdbc.mysql.password")
//    val driver = "com.mysql.jdbc.Driver"
//
//    val connectionProp = ConnectionProp(url, driver, username, password)
//
//    MysqlPools(connectionProp)
//  }
//
//
//
//  def getProp(name :String) :String= {
//    ConfigurationManager.getProperty(name)
//  }
//
//  /**
//    * 获取jar包路径
//    * @return
//    */
//  def getJarPath(): String = {
//    var mapredJar :String = ""
//    val url = ClassUtils.jarForClass(ConfigurationManager.getClass)
//    // local
//    if (url.contains("target")) mapredJar = StringUtils.substringBefore(url, "target") + "target/esearch-stat-spark-esearch-glb.jar"
//    else mapredJar = "lib/spark/esearch-stat-spark-esearch-glb.jar"
//
//    System.out.println(mapredJar)
//    mapredJar
//  }
//
//
//
//}
