package com.globalegrow.esearch.cstreaming.transform

import java.sql.{Driver, DriverManager}
import java.util.Properties

import org.apache.spark.{SparkConf, SparkContext}

object MyTest3 {
  def main(args: Array[String]): Unit = {
//      Class.forName("org.apache.kylin.jdbc.Driver")
    val driver = Class.forName("org.elasticsearch.xpack.sql.jdbc.jdbc.JdbcDriver").newInstance().asInstanceOf[Driver];
//    val url = "jdbc:kylin://169.60.8.121:7070/esearch_stat"
    val url = "jdbc:es://http://cdh-sl-search01:9200"
    val username = "ADMIN"
    val password = "KYLIN"
    val prop = new Properties()
    /*prop.put("user", "ADMIN");
    prop.put("password", "KYLIN");*/
//    val sql = "select count(*) from es_gearbest_10002.stat_search_data_table"
    var sql = "select * from gb_addcart_data where glb_dc = '1301' limit 10"
    sql = "select * from (" + sql + ")v"
//    val sql = "insert into libraray values ('1','2','1989-05-26', 22)"
    /*val connection = driver.connect(url, prop)
      println(connection)
      val result = connection.createStatement().executeQuery(sql)*/
    val connection2 = DriverManager.getConnection(url)
    println(connection2)
    val statement2 = connection2.createStatement()
    val result2 = statement2.executeQuery(sql)
    while(result2.next()) {
      println(result2.getString("glb_dc"))
    }
  }
}
