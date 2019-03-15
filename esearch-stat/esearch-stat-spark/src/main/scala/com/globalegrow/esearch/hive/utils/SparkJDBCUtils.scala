package com.globalegrow.esearch.hive.utils

import java.util
import java.util.Properties

import com.globalegrow.esearch.hive.bean.JdbcLinkConfig
import org.apache.commons.logging.LogFactory
import org.apache.spark.sql.{DataFrame, SQLContext, SaveMode}

object SparkJDBCUtils {

  //每次抽取的数量
  val FETCH_SIZE :Int = 500000

  private val log  = LogFactory.getLog(this.getClass)

  def getDataFrame(sqlContext : SQLContext, schema : JdbcLinkConfig,tableName : String, sql : String): DataFrame = {
    val readConnProperties = new Properties()
    readConnProperties.put("driver", schema.jdbcDriver)
    readConnProperties.put("user", schema.jdbcUsername)
    readConnProperties.put("password", schema.jdbcPassword)
    readConnProperties.put("fetchsize", FETCH_SIZE.toString)
    val executeSql = "(" + sql.replace("${CONDITIONS}", "1 = 1") + ")t"
    val dataFrame = sqlContext.read.jdbc(schema.jdbcUrl, executeSql, readConnProperties)
    dataFrame.registerTempTable(tableName)
    dataFrame
  }

}
