package com.globalegrow.esearch.cstreaming.constants

object Constants {

  val CONNECTOR_KAFKA_BROKERS = "brokers"

  val CONFIG_FILE_PATH = "configFilePath"

  val CONNECTOR_JDBC_SQL_TYPE = "sql.type"

  val CONNECTOR_JDBC_SQL_TYPE_DEFAULT = "common-sql"

  val CONNECTOR_JDBC_SQL_TYPE_MAIN_DF_COMMON = "common-one-sql"

  val CONNECTOR_JDBC_ROW_COLUMN_PREFIX = "row."

  val CONNECTOR_JDBC_SQL_TYPE_UNION_ALL = "union-all-sql"

  val CONNECTOR_JDBC_SQL_MAIN_DATAFRAME = "sql.main.dataframe"

  val CONNECTOR_SOURCE_KAFKA_EXAMPLE_JSON = "example.json"

  val CONNECTOR_SOURCE_JDBC_EXAMPLE_JSON = "example.json"

  val CONTEXT_COMMENT_STR = "#"

  val CONNECTOR_DATAFRAME_IS_CACHE = "isCache"

  val CONNECTOR_KAFKA_GROUP_ID = "groupId"

  val CONNECTOR_KAFKA_TOPICS = "topics"

  val CONNECTOR_TYPE = "type"

  val CONNECTOR_DATA_TYPE = "data.type"

  val CONNECTOR_DATA_DELIMIETER = "data.delimiter"

  val CONNECTOR_DATA_COLUMNS = "data.columns"

  val CONNECTOR_SOURCE_IS_MAIN = "isMain"

  val CONNECTOR_REGISTER_TABLE_NAME = "register.tableName"

  val CSTREAMING = "cstreaming"

  val CSTREAMING_SPARKSTREAMING = "sparkstreaming"

  val CSTREAMING_APP_NAME = "cstreaming.app.name"

  val SPARK_APP_NAME = "sparkstreaming.app.name"

  val SPARK_MASTER = "sparkstreaming.master"

  val SPARK_CHECKPOINT_DIR = "sparkstreaming.checkpoint.dir"

  val SPARK_DURATION_SENCONDS = "sparkstreaming.duration.senconds"

  val SPARK_IS_WINDOW = "sparkstreaming.isWindow"

  val SPARK_WINDOW_SLIDE_DURAION = "sparkstreaming.window.slide.duration"

  val SPARK_WINDOW_DURATION = "sparkstreaming.window.duration"

  val CONNECTOR_FLOWS = "flows"

  val DEFAULT_KAFKA_KEY_DESERIALIZER = "org.apache.kafka.common.serialization.StringDeserializer"

  val DEFAULT_KAFKA_VALUE_DESERIALIZER = "org.apache.kafka.common.serialization.StringDeserializer"

  val KAFKA_KEY_DESERIALIZER = "key.deserializer"

  val KAFKA_VALUE_DESERIALIZER = "value.deserializer"

  val CONNECTOR_SINKS = "sinks"

  val CONNECTOR_SQL = "sql"

  val CONNECTOR_JDBC_DRIVER = "driver"

  val CONNECTOR_SINK_WRITE_DATAFRAME_NAME = "write.dataframeName"

  val CONNECTOR_CONNECT_URL = "connectUrl"

  val CONNECTOR_SINK_WRITE_TABLENAME = "write.tableName"

  val DEFALUT_CONNECTOR_FLOWS_IS_MAIN = "false"

  val DEFALUT_CHECKPINT_DIR = "/cstreaming/data/checkpoint_dir/checkpoint"

  val DATA_TYPE_JSON = "json"

  val DATA_TYPE_DELIMITER = "delimiter"
}
