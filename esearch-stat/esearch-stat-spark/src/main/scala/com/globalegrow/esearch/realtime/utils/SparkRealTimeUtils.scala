//package com.globalegrow.esearch.realtime.utils
//
//import java.text.SimpleDateFormat
//import java.util.Date
//
//import com.globalegrow.esearch.realtime.conf.ConfigurationManager
//import com.globalegrow.esearch.realtime.streaming.IpTopNCount.{getJarPath, getProp}
//import org.apache.spark.SparkConf
//import org.apache.spark.streaming.{Seconds, StreamingContext}
//
//import scala.collection.JavaConversions._
//
//object SparkRealTimeUtils {
//    def getSparkConf(mainTopic :String) :SparkConf = {
//      var conf = new SparkConf()
//        .setAppName(getProp(s"$mainTopic.streaming.app.name"))
//
//      val masterStr = getProp(s"$mainTopic.streaming.master")
//      if(masterStr != null) {
//        conf = conf.setMaster(masterStr)
//      }
//
//      val properties = ConfigurationManager.getAllProperties()
//      for((key , value) <- properties) {
//        if(key.startsWith(s"$mainTopic.spark")) {
//          val key2 = key.substring(s"$mainTopic".length + 1)
//          conf.set(key2, properties.getProperty(key))
//        }
//      }
//
//
//      conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem")
////      conf.setJars(Array(getJarPath()))
//
//      conf
//    }
//
//  def main(args: Array[String]): Unit = {
//    val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
//    val sdf2 = new SimpleDateFormat("yyyy-MM-dd")
//    val l = System.currentTimeMillis()
//    println(sdf.format(l -  1000 * 3600 * 65))
//
//    val date = sdf.parse("2018-06-25 00:00:00:1000")
//    val date2 = sdf2.parse("2018-06-25")
////    println(new Date().getTime)
//    println(date.getTime)
//    println(date2.getTime)
////    println(l)
//
//  }
//}
