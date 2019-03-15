package com.globalegrow.esearch.hive.utils

import com.alibaba.fastjson.JSON
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{DataTypes, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SQLContext}

import scala.collection.mutable.{ArrayBuffer, ListBuffer}

object SparkUtils {
  /**
    * 根据dataframe和顺序排列的列名组合获取排好序的列的rdd
    * @param df
    * @param columns 应该排序的column组合
    * @return
    */
  def getOrderRDD(df:DataFrame, columns :Array[String]) :RDD[String]= {
    val resultRDD = df.rdd.map(row => {
      val array = columns.foldLeft(ArrayBuffer[String]())({case (array, column) => {
        val value = row.getAs[Any](column)
        if (value != null) {
          array += value.toString
        } else {
          array += null
        }
        array
      }})
      array.mkString("\001")
    })
    resultRDD
  }




}
