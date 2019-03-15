package com.globalegrow.esearch.cstreaming.connectors

import java.util.Properties

import com.globalegrow.esearch.cstreaming.constants.Constants
import com.globalegrow.esearch.cstreaming.udfs.UserFunctions
import com.globalegrow.esearch.cstreaming.utils.StringUtils
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.sql.types.StructType
import org.apache.spark.streaming.StreamingContext

import scala.collection.mutable

/**
  * 父connector
  */
class SuperConnector(@transient ssc: StreamingContext, allConfigMap:Map[String,String], currentName:String) extends Serializable {

  @transient val sqlContext = SQLContext.getOrCreate(ssc.sparkContext)
  @transient val sc = sqlContext.sparkContext

  val sourceConfigMap:mutable.Map[String,String] = mutable.Map[String,String]()
  val sourceExtraConfigMap:Properties = new Properties()
  val sinkConfigMap:mutable.Map[String,String] = mutable.Map[String,String]()
  val sinkExtraConfigMap:Properties = new Properties()
  val table2Schema :mutable.Map[String,StructType] = mutable.Map()

  commonFilterConfig()

  def commonFilterConfig() = {
    val appName = allConfigMap(Constants.CSTREAMING_APP_NAME)

    val isMain = allConfigMap.getOrElse(s"$appName.${Constants.CONNECTOR_FLOWS}.${currentName}.${Constants.CONNECTOR_SOURCE_IS_MAIN}", Constants.DEFALUT_CONNECTOR_FLOWS_IS_MAIN).toBoolean
    if(isMain) {
      allConfigMap.foreach({case (key, value) => {
        if (key.startsWith(s"$appName.${Constants.CSTREAMING_SPARKSTREAMING}.")) {
          sourceConfigMap += key.substring(s"$appName.".length) -> value
        } else if(key.startsWith(s"$appName.${Constants.CSTREAMING_SPARKSTREAMING}.config.")) {
          sourceExtraConfigMap.setProperty(key.substring(s"$appName.${Constants.CSTREAMING_SPARKSTREAMING}.config.".length), value)
        }
      }})
    }

    allConfigMap.foreach({case (key, value) => {
      if (key.startsWith(s"$appName.${Constants.CONNECTOR_FLOWS}.$currentName.config.")) {
        sourceExtraConfigMap.setProperty(key.substring(s"$appName.${Constants.CONNECTOR_FLOWS}.$currentName.config.".length),value)
      } else if (key.startsWith(s"$appName.${Constants.CONNECTOR_FLOWS}.$currentName")) {
        sourceConfigMap += key.substring(s"$appName.${Constants.CONNECTOR_FLOWS}.$currentName.".length) -> value
      } else if (key.startsWith(s"$appName.${Constants.CONNECTOR_SINKS}.$currentName.config.")) {
        sinkExtraConfigMap.setProperty(key.substring(s"$appName.${Constants.CONNECTOR_SINKS}.$currentName.config.".length),value)
      } else if (key.startsWith(s"$appName.${Constants.CONNECTOR_SINKS}.$currentName")) {
        sinkConfigMap += key.substring(s"$appName.${Constants.CONNECTOR_SINKS}.$currentName.".length) -> value
      }
    }})
  }

  def convertToDataFrame(): Unit = {
  }


  def sinkWriteToEnd() :Unit = {
  }

  /**
    * 获取最终的sql
    * @return
    */
  def getFinalSql(): String = {
    import sqlContext.implicits._
    var sql = sourceConfigMap(Constants.CONNECTOR_SQL)
    val sqlType = sourceConfigMap.getOrElse(Constants.CONNECTOR_JDBC_SQL_TYPE, Constants.CONNECTOR_JDBC_SQL_TYPE_DEFAULT)
    //1. 判断sqltype类型，如果是默认,则替换sql里的特殊字符为用执行sql的方式
    sqlType match {
      case Constants.CONNECTOR_JDBC_SQL_TYPE_DEFAULT => {
        sql = sql
      }
      case Constants.CONNECTOR_JDBC_SQL_TYPE_MAIN_DF_COMMON => {
        val disposeSql = (elStr :String, b:Any) => sqlContext.sql(elStr).map(row => {
          var result = row(0).toString
          if(row(0).isInstanceOf[String])
          {
            result = "'" + row(0).toString + "'"
          }
          result
        }).collect().mkString(",")
        sql = StringUtils.repalceEL(sql, disposeSql, null)
      }
      //2. 若为union_all类型,则获取对应的主表，然后遍历替换特殊字符用相应的列,然后再合并之后用union all分隔
      case Constants.CONNECTOR_JDBC_SQL_TYPE_UNION_ALL => {
        //注意rowParame：Any可以优化成指定类型
        val disposeSql = (elStr:String, rowParam :Any) => {
          val row = rowParam.asInstanceOf[Row]
          var result :Any = null
          if(elStr.contains("(") && elStr.contains(")")) {
            val leftPosition = elStr.indexOf("(")
            val rightPosition = elStr.indexOf(")")
            val functionName = elStr.substring(0, leftPosition).trim
            val columnNames = elStr.substring(leftPosition + 1, rightPosition).split(",").map(_.trim)
            val columns = columnNames.map(columnName => {
              if(columnName.startsWith(Constants.CONNECTOR_JDBC_ROW_COLUMN_PREFIX)) {
                row.getAs[Any](columnName.substring(Constants.CONNECTOR_JDBC_ROW_COLUMN_PREFIX.length)).toString
              } else {
                columnName
              }
            })
            result = UserFunctions.getClass.getMethod(functionName, columnNames.map(column => classOf[Any]) :_*).invoke(UserFunctions, columns :_*)
          } else {
            result = row.getAs[Any](elStr)
          }
          result.toString
        }
        val mainDF = sqlContext.table(sourceConfigMap(Constants.CONNECTOR_JDBC_SQL_MAIN_DATAFRAME))
        sql = mainDF.map(row => {
          StringUtils.repalceEL(sql, disposeSql, row)
        }).collect().mkString("\n UNION ALL \n")
      }
    }

    //得到最终sql
    sql
  }



}
