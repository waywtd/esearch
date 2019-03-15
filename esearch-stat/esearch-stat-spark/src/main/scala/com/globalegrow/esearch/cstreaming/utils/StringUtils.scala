package com.globalegrow.esearch.cstreaming.utils

import scala.collection.mutable

object StringUtils {

  /**
    * 根据开始字符串过滤掉特定的key，选出最后满足条件的map，否则返回空map
    * @param sourceMap 源map
    * @param startKey 过滤用的起始字符串
    * @param isCut 是否对key 用过滤起始字符串切除
    * @return
    */
  def getMapForStartWith(sourceMap :Map[String,String], startKey:String, isCut :Boolean = true) :Map[String,String] = {
    sourceMap.filterKeys(key => key.startsWith(startKey)).map({case (key, value) => {
      var newKey = key
      if (isCut) {
        newKey = key.substring(startKey.length)
      }
      (newKey, value)
    }})
  }

  //如何设置disposestr为变长参数?
  def repalceEL(elStr :String, disposeStr: (String, Any) => String, other :Any) :String = {
    if(!elStr.contains("#{") || !elStr.contains("}")) {
      return elStr
    }
    var resultStr = elStr
    var preFlag = false
    var preFlagIndex = 0
    var nowIndex = 0
    while(nowIndex <= resultStr.length - 1) {
      if (preFlag) {
        val afterIndex = resultStr.indexOf("}", nowIndex)
        if (afterIndex != -1) {
          var replaceStr = disposeStr(resultStr.substring(preFlagIndex + 2, afterIndex),other)
          resultStr = resultStr.substring(0, preFlagIndex) + replaceStr + resultStr.substring(afterIndex + 1)
          nowIndex = preFlagIndex + replaceStr.length
          preFlag = false
        }
      } else {
        val preIndex = resultStr.indexOf("#{", nowIndex)
        if (preIndex != -1) {
          preFlag = true
          preFlagIndex = preIndex
          nowIndex = preIndex + 2
        } else {
          nowIndex = resultStr.length + 1
        }
      }
    }
    resultStr
  }

}
