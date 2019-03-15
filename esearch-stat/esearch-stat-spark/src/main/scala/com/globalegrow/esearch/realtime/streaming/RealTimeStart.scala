package com.globalegrow.esearch.realtime.streaming

import org.apache.logging.log4j.LogManager

object RealTimeStart {

  val logger = LogManager.getLogger(RealTimeStart.getClass)

  def main(args: Array[String]): Unit = {
    try {
//       UserActionCollect.main(null)
    }catch {
      case ex :Throwable => {logger.error(ex); logger.error("details :" + ex.getStackTraceString)}
    }
  }
}
