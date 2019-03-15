//package com.globalegrow.esearch.realtime.utils
//
//import org.apache.spark.{SparkConf, SparkContext}
//
//object MyTest {
//  def main(args: Array[String]): Unit = {
//    val array :Array[String] = Array[String]()
//    val conf = new SparkConf().setMaster("local[*]").setAppName("my-test")
//    conf.setJars(Array(getJarPath()))
//    val sc = new SparkContext(conf)
//
//
//    val ints = sc.parallelize(array).foreach(str => println(str))
////    println(ints.mkString("|"))
//  }
//}
