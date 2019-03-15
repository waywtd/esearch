package com.globalegrow.esearch.hive.bean



case class JdbcLinkConfig(
                         name :String = null,
                         jdbcUrl :String,
                         jdbcUsername :String,
                         jdbcPassword :String,
                         jdbcDriver :String = "com.mysql.jdbc.Driver"
)