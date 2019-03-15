package com.globalegrow.esearch.cstreaming.connectors

import com.globalegrow.esearch.cstreaming.constants.Constants
import org.apache.logging.log4j.LogManager
import org.apache.spark.SparkException
import org.apache.spark.streaming.StreamingContext
import org.elasticsearch.spark.sql._

import collection.JavaConversions._

class ESConnector (ssc: StreamingContext, allConfigMap:Map[String,String], currentName:String) extends SuperConnector(ssc, allConfigMap, currentName){

  private val logger = LogManager.getLogger("com.globalegrow.esearch.cstreaming.connectors.ESConnector")

  override def convertToDataFrame(): Unit = {
    throw new SparkException("Don't Support Read From Es, please use jdbc type ")
  }

  override def sinkWriteToEnd(): Unit = {
    logger.info("Start write to elasticsearch, the param is: " + sinkExtraConfigMap.mkString("\nX"))
    val writeDataFrameName = sinkConfigMap(Constants.CONNECTOR_SINK_WRITE_DATAFRAME_NAME)
    val dataFrame = sqlContext.table(writeDataFrameName)
    dataFrame.saveToEs(sinkExtraConfigMap)
  }
}
