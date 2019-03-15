package com.globalegrow.esearch.cstreaming.connectors
import com.globalegrow.esearch.cstreaming.constants.Constants
import com.globalegrow.esearch.cstreaming.transform.StreamingTransform
import com.globalegrow.esearch.cstreaming.udfs.UserFunctions
import com.globalegrow.esearch.cstreaming.utils.StringUtils
import org.apache.logging.log4j.LogManager
import org.apache.spark.sql.{DataFrame, Row, SQLContext, SaveMode}
import org.apache.spark.streaming.StreamingContext

import scala.collection.JavaConversions._

/**
  * jdbc connector
  * driver
  * fetchsize
  * user
  * password
  * @param ssc
  * @param allConfigMap
  * @param currentName
  */
class JDBCConnector(ssc: StreamingContext, allConfigMap:Map[String,String], currentName:String) extends SuperConnector(ssc, allConfigMap, currentName){

  val JBDC_CONNECOTR_DRIVER_DEFALUT = "com.mysql.jdbc.Driver"

  private val logger = LogManager.getLogger("com.globalegrow.esearch.cstreaming.connectors.JDBCConnector")



  override def convertToDataFrame(): Unit = {
    import sqlContext.implicits._
    val driver = sourceExtraConfigMap.getOrElse(Constants.CONNECTOR_JDBC_DRIVER, JBDC_CONNECOTR_DRIVER_DEFALUT)
    sourceExtraConfigMap += Constants.CONNECTOR_JDBC_DRIVER -> driver
    val connectUrl = sourceConfigMap(Constants.CONNECTOR_CONNECT_URL)
    val isCache = sourceConfigMap.getOrElse(Constants.CONNECTOR_DATAFRAME_IS_CACHE, "false").toBoolean
    val mainFrameName = sourceConfigMap.getOrElse(Constants.CONNECTOR_JDBC_SQL_MAIN_DATAFRAME, "")
    val registerTableName = sourceConfigMap(Constants.CONNECTOR_REGISTER_TABLE_NAME)
    val sqlType = sourceConfigMap.getOrDefault(Constants.CONNECTOR_JDBC_SQL_TYPE, Constants.CONNECTOR_JDBC_SQL_TYPE_DEFAULT)
    //如果数据为空，则使用json格式创建空dataframe
    var dataframe :DataFrame = null
    if(mainFrameName != "" && sqlType != Constants.CONNECTOR_JDBC_SQL_TYPE_DEFAULT && sqlContext.table(mainFrameName).rdd.isEmpty()) {
      logger.info(s"$mainFrameName don't have any data, start default data")
      if(!table2Schema.contains(registerTableName)) {
        val exampleJson = sourceConfigMap(Constants.CONNECTOR_SOURCE_JDBC_EXAMPLE_JSON)
        val jsonRDD = sc.parallelize(exampleJson :: Nil)
        val schema = sqlContext.read.json(jsonRDD).schema
        table2Schema += registerTableName -> schema
      }
      dataframe = sqlContext.createDataFrame(sc.emptyRDD[Row], table2Schema(registerTableName))
    } else {
      var sql = "(" + getFinalSql() + ")v"
      logger.info(s"The JDBC SQL is $sql")

      dataframe = sqlContext.read.jdbc(connectUrl, sql, sourceExtraConfigMap)
      logger.info(s"The JDBC SQL is $sql")
    }
    dataframe.registerTempTable(registerTableName)
//    dataframe.createOrReplaceTempView(registerTableName)
    if(isCache) {
      dataframe.cache()
    }
  }

  override def sinkWriteToEnd(): Unit = {
    val connectUrl = sinkConfigMap(Constants.CONNECTOR_CONNECT_URL)
    val writeTableName = sinkConfigMap(Constants.CONNECTOR_SINK_WRITE_TABLENAME)
    val driver = sinkExtraConfigMap.getOrElse(Constants.CONNECTOR_JDBC_DRIVER, JBDC_CONNECOTR_DRIVER_DEFALUT)
    sinkExtraConfigMap += Constants.CONNECTOR_JDBC_DRIVER -> driver
    val writeDataFrameName = sinkConfigMap(Constants.CONNECTOR_SINK_WRITE_DATAFRAME_NAME)
    val writeDF = sqlContext.table(writeDataFrameName)
    writeDF.write.mode(SaveMode.Append).jdbc(connectUrl, writeTableName, sinkExtraConfigMap)
  }




}
