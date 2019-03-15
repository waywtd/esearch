package com.globalegrow.esearch.cstreaming.transform

import java.io.{BufferedInputStream, BufferedReader, InputStreamReader}

import com.globalegrow.esearch.cstreaming.connectors.KafkaConnector
import com.globalegrow.esearch.cstreaming.constants.Constants
import com.globalegrow.esearch.cstreaming.factorys.ConnectorFactory
import com.globalegrow.esearch.cstreaming.utils.{StreamingUtils, StringUtils}
import com.globalegrow.esearch.hive.utils.ParamUtils
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.io.ByteBufferPool
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.log4j.spi.LoggerFactory
import org.apache.logging.log4j.LogManager
import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.types.StructType

import scala.collection.mutable.ListBuffer
//import kafka.serializer.StringDecoder
//import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkException}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
//import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}

import scala.collection.JavaConversions._
import scala.collection.mutable

object StreamingTransform {

  private var paramMap :Map[String,String] = null

  private val logger = LogManager.getLogger(StreamingTransform.getClass)

  private var mainSchema :StructType = null


  def main(args: Array[String]): Unit = {
//    根据参数获取paramMap，获取checkpoint目录
    paramMap = initParam(args)
     val appName = paramMap(Constants.CSTREAMING_APP_NAME)
     val checkpointDir = paramMap.getOrElse(s"$appName.${Constants.SPARK_CHECKPOINT_DIR}", Constants.DEFALUT_CHECKPINT_DIR)
//    根据checkpint 或者独自创建
     val ssc = StreamingContext.getOrCreate(checkpointDir,creatingFunc)
//    启动streamingcontext
     ssc.start()
     ssc.awaitTermination()
  }


  def getMainKafkaParamMap(appName :String, mainFlowName :String, paramMap: Map[String, String]) :(Map[String, String],Map[String,String]) =  {
    logger.info(s"The ParmaMap: $paramMap")
    logger.info(s"The APP mainFlowName is : $mainFlowName")
    val mainParamMap = mutable.Map[String,String]()
    val extraConfigMap = mutable.Map[String,String]()
    paramMap.foreach{case (key, value) => {
      var newKey = key
       if (key.startsWith(s"$appName.${Constants.CONNECTOR_FLOWS}.$mainFlowName.config.")) {
        newKey = key.substring(s"$appName.${Constants.CONNECTOR_FLOWS}.$mainFlowName.config.".length)
        extraConfigMap += newKey -> value
      } else if (key.startsWith(s"$appName.${Constants.CONNECTOR_FLOWS}.$mainFlowName.")) {
        newKey = key.substring(s"$appName.${Constants.CONNECTOR_FLOWS}.$mainFlowName.".length)
        mainParamMap += newKey -> value
      } else if(key.startsWith(s"$appName.")) {
        newKey = key.substring(s"$appName.".length)
        mainParamMap += newKey -> value
      }
    }
    }
      (mainParamMap.toMap, extraConfigMap.toMap)
  }

  /**
    * 初始化主kafka的schema
    * @param mainKafkaParamMap
    */
  def initMainSchema(sqlContext:SQLContext,mainKafkaParamMap: Map[String, String]) = {
     val sc = sqlContext.sparkContext
     val mainJson = mainKafkaParamMap(Constants.CONNECTOR_SOURCE_KAFKA_EXAMPLE_JSON)
     val rdd = sc.parallelize(mainJson ::Nil)
     val frame = sqlContext.read.json(rdd)
     mainSchema = frame.schema
  }

  def creatingFunc(): StreamingContext = {
//    根据主数据源建立dstreaming,用factory初始化各个connector
      val appName = paramMap(Constants.CSTREAMING_APP_NAME)
      var flows = paramMap(s"$appName.${Constants.CONNECTOR_FLOWS}").split(",")
      var sinks = paramMap(s"$appName.${Constants.CONNECTOR_SINKS}").split(",")
      val mainFlowName = flows.filter(name => paramMap.getOrElse(s"$appName.${Constants.CONNECTOR_FLOWS}.$name.${Constants.CONNECTOR_SOURCE_IS_MAIN}", "false").toBoolean)(0)
      flows = flows.filter({case str => str != mainFlowName})
      val connectType = paramMap(s"$appName.${Constants.CONNECTOR_FLOWS}.$mainFlowName.${Constants.CONNECTOR_TYPE}")
      if (connectType != "kafka") {
        throw new SparkException("now can not support type except kafka! not :" + connectType)
      }
      val kafkaDataType = paramMap(s"$appName.${Constants.CONNECTOR_FLOWS}.$mainFlowName.${Constants.CONNECTOR_DATA_TYPE}")
      if(kafkaDataType != "json") {
        throw new SparkException("now can not support data type except json! not :" + kafkaDataType)
      }
      val registerName = paramMap(s"$appName.${Constants.CONNECTOR_FLOWS}.$mainFlowName.${Constants.CONNECTOR_REGISTER_TABLE_NAME}")
      val (mainKafkaParamMap, extraParamMap) = getMainKafkaParamMap(appName, mainFlowName, paramMap)
      val kafkaStreaming = StreamingUtils.initKafkaStreaming(null,mainKafkaParamMap,extraParamMap )
      val ssc = kafkaStreaming.context
      val factory = ConnectorFactory.getInstance()
      factory.initConnectors(ssc, paramMap)
      initMainSchema(SQLContext.getOrCreate(ssc.sparkContext),mainKafkaParamMap)
      factory.connectors.foreach{case (name,connector) => {
        if(connector.isInstanceOf[KafkaConnector] && flows.contains(name)) {
          connector.convertToDataFrame()
          flows = flows.filter(_ !=name)
          factory.connectors.remove(name)
        }
      }}

//    在主数据源foreachRDD，遍历flows,根据factory得到相应connector,调用convertToDataframe
    //    遍历sinks,根据factory得到相应connector,调用write方法
      kafkaStreaming.foreachRDD(rdd => {
         val sqlContext = SQLContext.getOrCreate(rdd.sparkContext)
         var kafkaDF :DataFrame = null
         if(rdd.isEmpty()) {
           kafkaDF = sqlContext.createDataFrame(sqlContext.sparkContext.emptyRDD[Row], mainSchema)
         } else {
           kafkaDF = kafkaDataType match {
             case "json" => sqlContext.read.json(rdd)
           }
         }
//        kafkaDF.registerTempTable(registerName)
        kafkaDF.createOrReplaceTempView(registerName)

        flows.foreach(flowName => {
          logger.info(s"Start flow: $flowName")
          factory.getConnector(flowName).convertToDataFrame()
        })

        sinks.foreach(sinkName => {
          logger.info(s"start sink: $sinkName")
          factory.getConnector(sinkName).sinkWriteToEnd()
        })

      })

    ssc
  }

  def initParam(args: Array[String]): Map[String, String] = {
    val paramMapSrc = ParamUtils.getParamMap(args)
    val configPath = paramMapSrc(s"${Constants.CONFIG_FILE_PATH}")
    val path = new Path(configPath)
    val bufferedReader = new BufferedReader(new InputStreamReader((FileSystem.get(new Configuration()).open(path)), "UTF-8"))
    var str = ""
    val args2 = ListBuffer[String]()
    while (str != null) {
      str = bufferedReader.readLine()
      if (str != null && !str.startsWith(Constants.CONTEXT_COMMENT_STR)) {
        args2 += str
      }
    }
    ParamUtils.getParamMap(args2.toArray)
  }



}
