package com.globalegrow.esearch.cstreaming.transform

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable

object MyTest {
  def main(args: Array[String]): Unit = {
//    得到conf,sqlContext,ssc等对象
    val conf = new SparkConf().setAppName("my_test").setMaster("local[*]")
    val ssc = new StreamingContext(conf, Seconds(3))
    val sqlContext = SQLContext.getOrCreate(ssc.sparkContext)
    val sc = ssc.sparkContext

    // 获取两个queue rdd，并且成为流，注册为相应的df
    val rdd1 = sc.parallelize(Seq(Tuple3(1,2,3), Tuple3(2,2,3), Tuple3(3,2,3)))
    val rdd2 = sc.parallelize(Seq(Tuple3(5,2,3), Tuple3(5,2,3), Tuple3(3,2,3)))
    val rdd3 = sc.parallelize(Seq(Tuple3(6,2,3), Tuple3(2,2,3), Tuple3(3,2,3)))
    val rdd4 = sc.parallelize(Seq(Tuple3(7,2,3), Tuple3(5,2,3), Tuple3(3,2,3)))
    val queueRDD1 = new mutable.Queue[RDD[Tuple3[Int, Int, Int]]]()
    queueRDD1.enqueue(rdd1, rdd2, rdd3, rdd4)
    val queueRDD2 = new mutable.Queue[RDD[Tuple3[Int, Int, Int]]]()
    queueRDD2.enqueue(rdd3, rdd4)
    val dstreaming1 = ssc.queueStream(queueRDD1)
    val dstreaming2 = ssc.queueStream(queueRDD2)
    import sqlContext.implicits._

    dstreaming1.window(Seconds(6), Seconds(3)).foreachRDD(rdd => {
      println(rdd.collect().mkString("|"))
      println("-------------------------")
    })

    /*dstreaming1.foreachRDD(rdd => {
      rdd.toDF("a", "b", "c").registerTempTable("tb1")
      println(rdd.collect().mkString("|"))
    })
    dstreaming2.foreachRDD(rdd => {
      rdd.toDF("d", "e", "f").registerTempTable("tb2")
      println(rdd.collect().mkString("|"))
      println("------------------")
      sqlContext.sql("select * from tb1 join tb2 on tb1.a = tb2.d").show(false)
    })*/


    // 根据sqlContext，吧相关df操作,输出
//    sqlContext.sql("select * from tb1").show(false)
    ssc.start()
    ssc.awaitTermination()

  }

  def main1(args: Array[String]): Unit = {
    //    得到conf,sqlContext,ssc等对象
    val conf = new SparkConf().setAppName("my_test").setMaster("local[*]")
    val ssc = new StreamingContext(conf, Seconds(3))
    val sqlContext = SQLContext.getOrCreate(ssc.sparkContext)
    val sc = ssc.sparkContext

    // 获取两个queue rdd，并且成为流，注册为相应的df


    // 根据sqlContext，吧相关df操作,输出
    //    sqlContext.sql("select * from tb1").show(false)
    ssc.start()
    ssc.awaitTermination()

  }
}
