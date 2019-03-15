package com.globalegrow.esearch.hive.utils

import scala.collection.mutable.ListBuffer

object StringUtils {
  def splitFirst(str :String, splitStr :String) :Array[String] = {
    var result = ListBuffer[String]();
    if (str == null) {
      result = null
    } else if (!str.contains(splitStr)){
       result += str
    } else {
      val splitIndex = str.indexOf(splitStr)
      result += str.substring(0, splitIndex)
      result += str.substring(splitIndex + splitStr.length)
    }
    result.toArray
  }
}
