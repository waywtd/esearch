package com.globalegrow.esearch.realtime.interceptors

import com.globalegrow.esearch.realtime.constants.{Constants => RealConstants}
import org.globalegrow.esearch.constant.{Constants, NginxFieldConstant}
import org.globalegrow.esearch.utils.StringUtils

import scala.collection.mutable

object KafkaInterceptor {

  def commonFilter(map :mutable.Map[String,String]) :Boolean = {
    val flag1 = map.values.forall(!_.contains(Constants.NGINX_SPLIT_SYMBOL))
    val flag2 = Constants.S_LOGSSS_COM == map.getOrElse(NginxFieldConstant.MESSAGE_REQHOST, "") && Constants.CLOUD_MONITOR == map.getOrElse(NginxFieldConstant.TYPE, "")
    val ubcd = map.getOrElse(RealConstants.GLB_D, "")
    val flag3 = StringUtils.isNotEmpty(ubcd)
    flag1 && flag2 && flag3
  }

}
