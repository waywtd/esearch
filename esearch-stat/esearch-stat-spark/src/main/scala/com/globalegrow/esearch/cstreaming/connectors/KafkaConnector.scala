package com.globalegrow.esearch.cstreaming.connectors


import com.globalegrow.esearch.cstreaming.constants.Constants
import com.globalegrow.esearch.cstreaming.pools.KafkaProducerPool
import com.globalegrow.esearch.cstreaming.utils.StreamingUtils
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.logging.log4j.LogManager
import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.types.StructType
import org.apache.spark.streaming.StreamingContext

import scala.collection.mutable
import scala.collection.JavaConversions._

class KafkaConnector (@transient  ssc: StreamingContext, allConfigMap:Map[String,String], currentName:String) extends SuperConnector(ssc, allConfigMap, currentName){

  private val logger = LogManager.getLogger("com.globalegrow.esearch.cstreaming.connectors.KafkaConnector")



  override def convertToDataFrame(): Unit = {
    logger.info("Start read data from kafka, the param is: " + sourceConfigMap.mkString("\n") + sourceExtraConfigMap.mkString("\n"))
    val dataType = sourceConfigMap(s"${Constants.CONNECTOR_DATA_TYPE}")
    val columns = sourceConfigMap.getOrElse(s"${Constants.CONNECTOR_DATA_COLUMNS}", "")
    val delimiter = sourceConfigMap.getOrElse(s"${Constants.CONNECTOR_DATA_DELIMIETER}", "")
    val tableName = sourceConfigMap(s"${Constants.CONNECTOR_REGISTER_TABLE_NAME}")
    val exampleJson = sourceConfigMap(Constants.CONNECTOR_SOURCE_KAFKA_EXAMPLE_JSON)
    val isCache = sourceConfigMap.getOrElse(Constants.CONNECTOR_DATAFRAME_IS_CACHE, "false").toBoolean
    if(!table2Schema.contains(tableName)) {
      val jsonRDD = sc.parallelize(exampleJson :: Nil)
      val schema = sqlContext.read.json(jsonRDD).schema
      table2Schema += tableName -> schema
    }
    val kafkaStreaming = StreamingUtils.initKafkaStreaming(ssc, sourceConfigMap.toMap, sourceExtraConfigMap.toMap)
     kafkaStreaming.foreachRDD(rdd => {
       var dataFrame :DataFrame = null
       if(rdd.isEmpty()) {
         logger.info("The Kafka RDD is empty, can't convert to dataframe: " + tableName)
         dataFrame = sqlContext.createDataFrame(sc.emptyRDD[Row], table2Schema(tableName))
       } else {
         dataFrame = StreamingUtils.rdd2DataFrame(rdd, dataType, columns, delimiter)
         logger.info("Success register kakfka rdd named " + tableName)
       }
       if(isCache) {
         dataFrame.cache()
       }
       dataFrame.registerTempTable(tableName)
     })
  }

  override def sinkWriteToEnd(): Unit = {
    logger.info("Start write data to kafka, the param is : " + sinkConfigMap.mkString("\n") + sinkExtraConfigMap.mkString("\n"))
    val brokers = sinkConfigMap(s"${Constants.CONNECTOR_KAFKA_BROKERS}")
    val topics = sinkConfigMap(s"${Constants.CONNECTOR_KAFKA_TOPICS}").split(",").toSet
    val dataFrameName = sinkConfigMap(s"${Constants.CONNECTOR_SINK_WRITE_DATAFRAME_NAME}")
    val kafkaConfig = mutable.Map[String,String](
      ProducerConfig.BOOTSTRAP_SERVERS_CONFIG -> brokers,
      ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG -> classOf[StringSerializer].getName,
      ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG -> classOf[StringSerializer].getName)
    sinkExtraConfigMap.putAll(kafkaConfig)
    sqlContext.table(dataFrameName).toJSON.foreachPartition(jsonStrs => {
//      val producer = new KafkaProducer[String,String](sinkExtraConfigMap, new StringSerializer(), new StringSerializer())
      val pool = KafkaProducerPool.apply(sinkExtraConfigMap)
      val producer = pool.borrowObject()
      KafkaProducerPool.apply(sinkExtraConfigMap)
      for(jsonStr <- jsonStrs) {
        topics.foreach(topic => producer.send(new ProducerRecord[String,String](topic, jsonStr)))
      }
      pool.returnObject(producer)
    })
  }

}
