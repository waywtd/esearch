package com.globalegrow.esearch.cstreaming.factorys

import com.globalegrow.esearch.cstreaming.connectors._
import com.globalegrow.esearch.cstreaming.constants.Constants
import com.globalegrow.esearch.cstreaming.transform.StreamingTransform
import org.apache.logging.log4j.LogManager
import org.apache.spark.SparkException
import org.apache.spark.streaming.StreamingContext

import scala.collection.mutable

class ConnectorFactory extends Serializable {
  private val logger = LogManager.getLogger(ConnectorFactory.getClass)

  val connectors = mutable.Map[String,SuperConnector]()

  def initConnectors(ssc :StreamingContext, paramMap :Map[String,String]) = {
    logger.info(s"Start init connectors, the param is: " + paramMap.mkString("\n"))
    val appName = paramMap(Constants.CSTREAMING_APP_NAME)
    val flows = paramMap(s"$appName.flows").split(",").toList
    val sinks = paramMap(s"$appName.sinks").split(",").toList
    val connectorStrs = flows ++ sinks

    connectorStrs.foreach(connectorStr => {
      val isMain = paramMap.getOrElse(s"$appName.flows.$connectorStr.isMain", "false").toBoolean
      val connectType = paramMap.getOrElse(s"$appName.flows.$connectorStr.type",paramMap(s"$appName.sinks.$connectorStr.type"))
      if (!isMain) {
        val connector = connectType match {
          case "elasticsearch" => new ESConnector(ssc, paramMap, connectorStr)
          case "jdbc" => new JDBCConnector(ssc, paramMap, connectorStr)
          case "merge" => new MergeConnector(ssc, paramMap, connectorStr)
          case "kafka" => new KafkaConnector(ssc, paramMap, connectorStr)
        }
        connectors += connectorStr -> connector
      }
    })
  }

  def getConnector(connectorName :String) :SuperConnector = {
     if (!connectors.contains(connectorName)) {
       throw new SparkException("can not find this connector: " + connectorName)
     }
    connectors(connectorName)
  }

}

object ConnectorFactory extends Serializable {

  private var instance :ConnectorFactory = null

  def getInstance() :ConnectorFactory = {
    synchronized{
      if (instance == null) {
        instance = new ConnectorFactory()
      }
      instance
    }
  }

}
