//package com.globalegrow.esearch.realtime.streaming
//
//import java.text.SimpleDateFormat
//import java.util.Date
//
//import com.alibaba.fastjson.JSON
//import com.globalegrow.esearch.realtime.streaming.IpTopNCount.{createStreamingContext, getProp}
//import com.globalegrow.esearch.realtime.utils.{JsonUtils, KafkaManager, SparkRealTimeUtils}
//import kafka.serializer.{DefaultDecoder, StringDecoder}
//import org.apache.flume.event.SimpleEvent
//import org.apache.flume.serializer.FlumeAvroSerializer
//import org.apache.kafka.clients.consumer.ConsumerConfig
//import org.apache.kafka.common.serialization.ByteArrayDeserializer
//import org.apache.logging.log4j.LogManager
//import com.globalegrow.esearch.realtime.constants.{Constants => RealConstants}
//import com.globalegrow.esearch.realtime.interceptors.KafkaInterceptor
//import org.apache.spark.SparkConf
//import org.apache.spark.rdd.RDD
//import org.apache.spark.streaming.{Seconds, StreamingContext}
//import org.globalegrow.esearch.constant.{Constants, NginxFieldConstant}
//import org.globalegrow.esearch.interceptor.LogInterceptor
//import org.globalegrow.esearch.utils.StringUtils
//import redis.clients.jedis.{HostAndPort, JedisCluster, JedisPool}
//
//import scala.collection.JavaConversions._
//import scala.collection.mutable
//
//object UserActionCollect {
//
//  private val logger = LogManager.getLogger(UserActionCollect.getClass)
//
//  val mainTopic = "user_action"
//  val hosts = getProp(s"$mainTopic.streaming.redis.host").split(",").map(str => new HostAndPort(str.split(":")(0), str.split(":")(1).toInt)).toSet
//
//  val jedispool = new JedisCluster(hosts)
//  def main(args: Array[String]): Unit = {
//
//    val conf = SparkRealTimeUtils.getSparkConf(mainTopic)
//    val checkpointDir = getProp(s"$mainTopic.streaming.checkpoint.dir") + "/" + getProp(s"$mainTopic.streaming.app.name")
//    val ssc = StreamingContext.getOrCreate(checkpointDir, () => createStreamingContext(mainTopic, conf, checkpointDir))
//
//
//    //持续连接
//    ssc.start()
//    ssc.awaitTermination()
//  }
//
//
//  def createStreamingContext(mainTopic :String, conf :SparkConf, checkpointDir:String) :StreamingContext = {
//    val ssc = new StreamingContext(conf, Seconds(getProp(s"$mainTopic.streaming.duration.senconds").toInt))
//    ssc.checkpoint(checkpointDir)
//
//    //连接kafka
//    val kafkaParams: Map[String, String] =
//      Map(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> getProp(s"$mainTopic.streaming.bootstrap.servers"),
//        ConsumerConfig.GROUP_ID_CONFIG -> getProp(s"$mainTopic.streaming.group.id"),
//        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> getProp(s"$mainTopic.streaming.auto.offset.reset"),
//        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.StringDeserializer",
//        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.StringDeserializer",
//        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG -> "false"
//      )
//    var topics: Set[String] = Set(getProp(s"$mainTopic.streaming.consumer.kafka.topics"))
//
//    val km = new KafkaManager(kafkaParams)
//    var kafkaDStreaming = km.createDirectStream[String, Array[Byte], StringDecoder, DefaultDecoder](ssc, kafkaParams, topics)
////    var kafkaDStreaming = km.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics)
//
//
//    val ubcds = getProp(s"$mainTopic.streaming.ubcds").split(",").toSet
//    val kafkaFiltedDStreaming = kafkaDStreaming.map{case (key, value) =>
//        val str = new String(FlumeAvroSerializer.deSerializer(value).getBody.array(), "utf-8")
//        val map : mutable.Map[String,String] = JSON.parseObject[java.util.Map[String,String]](str, classOf[java.util.Map[String, Any]])
//        if(map.contains(NginxFieldConstant.MESSAGE_REQQUERY)) {
//          val queryMap = map(NginxFieldConstant.MESSAGE_REQQUERY).split(RealConstants.MESSAGE_REQUERY_SPLIT)
//            .map(_.split(RealConstants.MESSAGE_REQUERY_PROP_SPLIT)).map({ case array =>
//            if(array.size == 2) (array(0) -> array(1)) else ("nothing" -> "nothing") })
//            .toMap
//          map ++= queryMap
//        }
//        map
//    }
//
//    //过滤掉没有glb_u和glb_ksku的数据
//    /**
//      * StringUtils.isNotEmpty(json) && Constants.S_LOGSSS_COM.equals(json.getString(NginxFieldConstant.MESSAGE_REQHOST)) && Constants.CLOUD_MONITOR.equals(json.getString(NginxFieldConstant.TYPE)
//      */
//    val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//    val userId2SkuDStreaming = kafkaFiltedDStreaming.filter(map => {
//      val flag1 = map.contains(RealConstants.GLB_TM)
//      val flag2 = map.contains(RealConstants.GLB_U)
//      val flag3 = map.contains(RealConstants.GLB_KSU)
//      val ubcd = map.getOrElse(RealConstants.GLB_D, "")
//      val flag4 = ubcds.contains(ubcd)
//      val flag5 = map.contains(NginxFieldConstant.MESSAGE_REQQUERY)
//        flag1 && flag2 && flag3 && flag4 && flag5 && KafkaInterceptor.commonFilter(map)
//      })
//      .map(map => {
//        val userId = map(RealConstants.GLB_U)
//        val skuId = map(RealConstants.GLB_KSU)
//        val ubcd = map(RealConstants.GLB_D)
//        val time = map(RealConstants.GLB_TM)
//        val timeStr = sdf.format(new Date(time.toLong))
//        (ubcd + "." + userId, (timeStr, skuId))
//      })
//
//    //开启窗口，在窗口内根据时间排序，送入redis
//    val slideDuration = getProp(s"$mainTopic.streaming.window.slide.duration").toInt
//    val windowDuration = getProp(s"$mainTopic.streaming.window.duration").toInt
//    userId2SkuDStreaming.groupByKeyAndWindow(Seconds(windowDuration), Seconds(slideDuration))
//      .foreachRDD(rdd => rdd.foreach{ case (userId, skuIterable) => {
//        val skuList = skuIterable.toList.distinct.sortBy(_._1).map(timeandsku => timeandsku._1 + "|" + timeandsku._2)
//        val userId2 = "stat.user." + userId
//        jedispool.del(userId2)
//        jedispool.set(userId2, skuList.mkString("||||||"))
//        logger.info(s"send user = $userId2 and skuList = ${skuList.mkString("\n")} ")
//      }
//    })
//
//    //更新zk中的offset
//
//
//    kafkaDStreaming.foreachRDD(rdd => {
//      if (!rdd.isEmpty)
//        km.updateZKOffsets2(rdd)
//    })
//  ssc
//  }
//
//}
//
//
//
