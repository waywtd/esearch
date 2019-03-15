package com.globalegrow.esearch.realtime.utils

import org.globalegrow.esearch.constant.{Constants, NginxFieldConstant}

import scala.collection.mutable

object JsonUtils {
   private def getInitMap():mutable.Map[String,String] = (
     mutable.LinkedHashMap[String,String](
        NginxFieldConstant.MESSAGE_CLIIP -> "",
        Constants.MIDDLELINE -> "",
        NginxFieldConstant.LOG_TIME -> "",
        Constants.KAFKA_REQUEST_PARAM -> "",
        NginxFieldConstant.MESSAGE_STATUS -> "",
        NginxFieldConstant.MESSAGE_BYTES -> "",
        NginxFieldConstant.REQHDR_REFERER -> "",
        NginxFieldConstant.MESSAGE_UA -> "",
        NginxFieldConstant.MESSAGE_REQHOST -> "",
        NginxFieldConstant.NETWORK_EDGEIP -> "",
        NginxFieldConstant.NETPERF_EDGEIP -> "",
        NginxFieldConstant.GEO_CITY -> "",
        NginxFieldConstant.GEO_COUNTRY -> ""
     )
  )

  /**
    * 根据kafka的消息组成map
    * @param logStr
    * @return
    */
  def getMapFromLogStr(logStr :String) :scala.collection.Map[String,String] = {
     var resultMap = getInitMap()
     val strings = logStr.split("\\^A\\^")
     var i = 0
     val length = strings.length
     val map = resultMap.map{case (k,v) => {
       if (i >= length) {
         (k, v)
       } else {
         val str = strings(i)
         i = i + 1
         (k, if (str != Constants.MIDDLELINE) str else null)
       }
     }
     }
    map
  }

  def main(args: Array[String]): Unit = {
    val str = "106.223.70.250^A^-^A^2018-05-26 02:31:28^A^GET /_ubc.gif?glb_t=ie&glb_tm=1527301888686&glb_d=10002&glb_b=a&glb_plf=m&glb_dc=1301&glb_pm=md&glb_ubcta=[{%22mdlc%22:%22A_1%22,%22mdID%22:%226538%22},{%22mdlc%22:%22A_1%22,%22mdID%22:%226941%22},{%22mdlc%22:%22A_1%22,%22mdID%22:%227076%22},{%22mdlc%22:%22A_1%22,%22mdID%22:%226943%22},{%22mdlc%22:%22A_1%22,%22mdID%22:%226799%22},{%22mdlc%22:%22A_1%22,%22mdID%22:%226946%22},{%22mdlc%22:%22A_1%22,%22mdID%22:%226944%22},{%22mdlc%22:%22A_1%22,%22mdID%22:%226538%22},{%22mdlc%22:%22A_1%22,%22mdID%22:%226941%22}]&glb_w=4400&glb_od=pwjwjocloggq1527301885415&glb_osr_referrer=originalurl&glb_osr_landing=https%3A%2F%2Fm.gearbest.com%2F%3Flkid%3D10714561%26cid%3Dpubd94c642003234775a917448610b93589&glb_cl=https%3A%2F%2Fm.gearbest.com%2F%3Flkid%3D10714561%26cid%3Dpubd94c642003234775a917448610b93589 HTTP/1.1^A^200^A^372^A^https://m.gearbest.com/?lkid=10714561&cid=pubd94c642003234775a917448610b93589^A^Mozilla/5.0 (Linux; Android 5.1; Micromax Q424 Build/LMY47D) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/39.0.0.0 Mobile Safari/537.36^A^s.logsss.com^A^96.17.182.100^A^96.17.182.100^A^BANGALORE^A^IN^A^"
    val resultMap = getMapFromLogStr(str)
   print(resultMap)
    /*val langStr = "glb_dc=(\\d)+".r.findFirstIn(resultMap(Constants.KAFKA_REQUEST_PARAM)).get.split("=")(1)
   var website = "glb_d=(\\d)+".r.findFirstIn(resultMap(Constants.KAFKA_REQUEST_PARAM)).get.split("=")(1)
   val lang = langStr2RegualStr(langStr)
   println(lang)
   println(website)

   val strings = "error=error".split("=")
   println()*/
  }

  def langStr2RegualStr(langStr :String) :String ={
    langStr match {
      case "1301" => "en"
      case "1302" => "ru"
      case "1303" => "es"
      case "1304" => "uk"
      case "1305" => "us"
      case "1306" => "it"
      case "1307" => "de"
      case "1308" => "pt"
      case "1309" => "br"
      case "1310" => "fr"
      case "1311" => "tr"
      case _ => ""
    }
  }


}
