package com.globalegrow.esearch.cstreaming.utils

import com.globalegrow.esearch.cstreaming.constants.Constants
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{DataType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SQLContext, SparkSession}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}

import scala.collection.mutable

object StreamingUtils {

  def initKafkaStreaming(sscSrc :StreamingContext,paramMap: Map[String, String], extraConfigMap :Map[String,String]): DStream[String] = {
    val duration = paramMap(s"${Constants.SPARK_DURATION_SENCONDS}").toInt
    val master = paramMap(s"${Constants.SPARK_MASTER}")
    val checkpointDir = paramMap.getOrElse(s"${Constants.SPARK_CHECKPOINT_DIR}", Constants.DEFALUT_CHECKPINT_DIR)
    val sparkConfig = StringUtils.getMapForStartWith(paramMap, s"${Constants.CSTREAMING_SPARKSTREAMING}.config.")
    val isWindow = paramMap.getOrElse(s"${Constants.SPARK_IS_WINDOW}", "false").toBoolean
    val windowSlide = if(isWindow) paramMap(s"${Constants.SPARK_WINDOW_SLIDE_DURAION}").toInt else -1
    val windowDuratiuon = if(isWindow) paramMap(s"${Constants.SPARK_WINDOW_DURATION}").toInt else -1


    val kafkaConfigMap = mutable.Map[String,String]()
    kafkaConfigMap ++= extraConfigMap

    if(!kafkaConfigMap.contains(Constants.KAFKA_KEY_DESERIALIZER)) {
      kafkaConfigMap += Constants.KAFKA_KEY_DESERIALIZER -> Constants.DEFAULT_KAFKA_KEY_DESERIALIZER
    }

    if(!kafkaConfigMap.contains(Constants.KAFKA_VALUE_DESERIALIZER)) {
      kafkaConfigMap += Constants.KAFKA_VALUE_DESERIALIZER -> Constants.DEFAULT_KAFKA_VALUE_DESERIALIZER
    }

    val brokers = paramMap(s"${Constants.CONNECTOR_KAFKA_BROKERS}")
    val groupId = paramMap(s"${Constants.CONNECTOR_KAFKA_GROUP_ID}")
    val topics  = paramMap(s"${Constants.CONNECTOR_KAFKA_TOPICS}").split(",").toSet
    kafkaConfigMap += ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> brokers
    kafkaConfigMap += ConsumerConfig.GROUP_ID_CONFIG -> groupId



    //初始化ssc,kafkastreaming
    var ssc  = sscSrc
    if(ssc == null) {
      val sparkAppName = paramMap(s"${Constants.SPARK_APP_NAME}")
      val conf = new SparkConf().setMaster(master).setAppName(sparkAppName).setAll(sparkConfig)
      ssc = new StreamingContext(conf, Seconds(duration))
      ssc.checkpoint(checkpointDir)
    }

//    val session = SparkSession.builder().getOrCreate()

    var kafkaStreaming :DStream[String] = KafkaUtils.createDirectStream[String,String](ssc, LocationStrategies.PreferConsistent, ConsumerStrategies.Subscribe[String,String](topics, kafkaConfigMap)).map(_.value())
    //var kafkaStreaming :DStream[String]  = KafkaUtils.createDirectStream[String,String,StringDecoder, StringDecoder](ssc, kafkaConfigMap.toMap, topics).map(_._2)
    if (isWindow) {
      kafkaStreaming = kafkaStreaming.window(Seconds(windowDuratiuon), Seconds(windowSlide))
    }
    kafkaStreaming
  }

  /**
    * rdd 2 dataframe，根据rdd的数据类型(json/delimiter)来转成dataframe
    * @param rdd
    * @param dataType
    * @param columns
    * @param delimeter 分隔符
    * @return
    */
  def rdd2DataFrame(rdd :RDD[String], dataType :String, columns :String, delimeter :String) :DataFrame = {
    val sqlContext = SQLContext.getOrCreate(rdd.sparkContext)
    var dataFrame :DataFrame = null
     dataType match {
       case Constants.DATA_TYPE_JSON =>  dataFrame = sqlContext.read.json(rdd)
       case Constants.DATA_TYPE_DELIMITER => {
         val structType = columns.split(delimeter).foldLeft(new StructType())({case (structType, column) => {
           structType.add(StructField(column, StringType))
         }})
         sqlContext.createDataFrame(rdd.map(str => Row(str.split(delimeter))), structType)
       }

     }
    dataFrame
  }
}
