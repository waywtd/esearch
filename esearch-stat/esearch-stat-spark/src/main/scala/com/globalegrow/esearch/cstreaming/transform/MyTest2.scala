package com.globalegrow.esearch.cstreaming.transform

import java.sql.DriverManager
import java.util.regex.Pattern

import com.globalegrow.esearch.cstreaming.utils.StringUtils

object MyTest2 {
  def main1(args: Array[String]): Unit = {
    var str = "select * from v where a in #{select * from a}asdf #{select * from as}"
    var str2 = "select from v where a in"
    val regexStr = ".*(#\\{.+\\}?).*"
//    val pattern = Pattern.compile(regexStr)
//
//    val matcher = pattern.matcher(str)
//    val matches = matcher.matches()
//
//    println(matches)
//    println(matcher.group(1))

    val result = StringUtils.repalceEL(str, (str :String, b:Any) => "chenchao", "asd")
    println(result)
  }

  def main3(args: Array[String]): Unit = {
    val connectionUrl = "jdbc:hive2://cdh-search-cloud03:21050/default;auth=noSasl"
    val connectionUrl2 = "jdbc:impala://cdh-search01:21050"
    val driverClass = "org.apache.hive.jdbc.HiveDriver"
    val driverClass2 = "org.apache.hadoop.hive.ql.Driver"

    Class.forName(driverClass)
    val connection = DriverManager.getConnection(connectionUrl)
//    val t1 = connection.getMetaData().supportsDataManipulationTransactionsOnly()
//    val t2 = connection.getMetaData().supportsDataDefinitionAndDataManipulationTransactions()

//    println(t1)
//    println(t2)

    val statement = connection.createStatement()

//    val set = statement.executeQuery("show tables")
//    val set = statement.executeQuery("select  * \nfrom hbase_table_1")
//    val set = statement.executeQuery("select * from hbase_table_1")
    val bool = statement.executeUpdate("insert into hbase_table_1(key,log_time,glb_dc) values ('2018-09-31 21:27:351301', '2018-09-25 21:27:35', '1301')")
    println(bool)


//    val set = connection.prepareStatement("select * from values__tmp__table__1").executeQuery()
//    val set = connection.prepareStatement("show tables").executeQuery()

    /*while(set.next()) {
      println(set.getString(1))
    }
    println(set)*/
  }

  def main(args: Array[String]): Unit = {
    Class.forName("com.mysql.jdbc.Driver")
    val url = "jdbc:mysql://10.40.6.150:3306/gb_goods"
    val username = "java-service"
    val password = "java123456"

    val connection = DriverManager.getConnection(url, username, password)
    val set = connection.getMetaData.getTables(null, "goods_info_extend_s%", null, null)
    while (set.next()) {
      println(set.getString("TABLE_NAME"))
    }
  }
}
