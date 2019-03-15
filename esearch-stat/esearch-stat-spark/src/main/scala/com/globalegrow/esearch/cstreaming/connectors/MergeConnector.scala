package com.globalegrow.esearch.cstreaming.connectors

import com.globalegrow.esearch.cstreaming.constants.Constants
import com.globalegrow.esearch.cstreaming.utils.StringUtils
import org.apache.logging.log4j.LogManager
import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.streaming.StreamingContext

/**
  * 合并多个数据源
  * @param ssc
  * @param allConfigMap
  * @param currentName
  */
class MergeConnector(ssc: StreamingContext, allConfigMap:Map[String,String], currentName:String) extends SuperConnector (ssc, allConfigMap, currentName){

  private val logger = LogManager.getLogger("com.globalegrow.esearch.cstreaming.connectors.MergeConnector")

  override def convertToDataFrame(): Unit = {
    import sqlContext.implicits._
    val appName = allConfigMap(Constants.CSTREAMING_APP_NAME)
    var sql = sourceConfigMap(Constants.CONNECTOR_SQL)
    val mainFrameName = sourceConfigMap.getOrElse(Constants.CONNECTOR_JDBC_SQL_MAIN_DATAFRAME, "")
    val disposeSql = (sql :String, b:Any) => sqlContext.sql(sql).map(row => "'" + row(0) + "'").collect().mkString(",")
    val sqlType = sourceConfigMap.getOrElse(Constants.CONNECTOR_JDBC_SQL_TYPE, Constants.CONNECTOR_JDBC_SQL_TYPE_DEFAULT)
    val registerTableName = sourceConfigMap(Constants.CONNECTOR_REGISTER_TABLE_NAME)
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
      var sql = getFinalSql()
      logger.info(s"The JDBC SQL is $sql")
      dataframe = sqlContext.sql(sql)
    }
    val isCache = sourceConfigMap.getOrElse(Constants.CONNECTOR_DATAFRAME_IS_CACHE, "false").toBoolean
    dataframe.registerTempTable(registerTableName)
//    sqlContext.sql(sql).createOrReplaceTempView(registerTableName)
    if(isCache) {
      dataframe.cache()
    }

  }
}
