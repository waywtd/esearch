package com.globalegrow.esearch.realtime.streaming

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object TransformKafkaStreaming {

  def main(args: Array[String]): Unit = {
      val timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
      val loop = 365
      var localDate = LocalDate.now()
      val sb = new StringBuilder("")
      for(i <- 0 to loop) {
        val dateStr = localDate.format(timeFormatter)
        val year = localDate.getYear
        val month = localDate.getMonthValue
        val day = localDate.getDayOfMonth
        val line = dateStr + "\001" + dateStr + "\001" + year + "\001" + month + "\001" + day + "\n"
        sb.append(line)
        localDate = localDate.plusDays(1)
      }

     println(sb.toString())
  }

}
