package com.globalegrow.esearch.cstreaming.udfs

object UserFunctions {

  def getOrderTableFromOrderSn(ordersn: Any, tableNum :Any) :String = {
    val ordersnNew = ordersn.toString
    val tableNumNew = tableNum.toString.toInt
    (ordersnNew.substring(9, 12).toInt % tableNumNew).toString
  }

}
