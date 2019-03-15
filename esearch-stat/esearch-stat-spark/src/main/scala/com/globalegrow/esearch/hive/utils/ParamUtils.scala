package com.globalegrow.esearch.hive.utils

import scala.collection.mutable

object ParamUtils {

  /**
    * 从字符串数组中获取参数
    * @param params
    * @return
    */
    def  getParamMap(params :Array[String]) :Map[String,String] = {
      params.foldLeft(mutable.LinkedHashMap[String,String]())({case (map, param) => {
        if(param != null && param.trim != "") {
          val strings = StringUtils.splitFirst(param, "=")
          map += strings(0).trim -> strings(1).trim
        }
        map
      }}).toMap
    }
}
