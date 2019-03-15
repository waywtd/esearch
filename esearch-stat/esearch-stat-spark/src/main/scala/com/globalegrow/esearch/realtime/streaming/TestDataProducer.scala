package com.globalegrow.esearch.realtime.streaming

import java.io._
import java.util
import java.util.Properties

import com.alibaba.fastjson.JSON
import org.apache.flume.serializer.FlumeAvroSerializer
import org.apache.flume.source.avro.AvroFlumeEvent
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.{ByteArraySerializer, StringSerializer}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.collection.JavaConversions._

object TestDataProducer {
  def main(args: Array[String]): Unit = {
    val topic = "gearbest2_test"
    val path = "C:\\Users\\Chenchao1\\Desktop\\data2.txt"
    val brokers = "cdh-search01:9092,cdh-search02:9092,cdh-search03:9092"

    //创建生产者
    val props = new Properties();
    props.put("bootstrap.servers", brokers);
    props.put("transactional.id", "my-transactional-id");

    val reader = new BufferedReader(new FileReader(path))
    val  producer = new KafkaProducer(props, new StringSerializer(), new StringSerializer());
//    val  producer = new KafkaProducer(props, new StringSerializer(), new ByteArraySerializer());

    //读取文件，每隔一点时间送入kafka
   /* val byteList = ArrayBuffer[Byte]()
    val fis = new FileInputStream(path)
    val buffer = new  Array[Byte](1000)
    var len = -1
    var loopFlag= true
    while(loopFlag) {
      len = fis.read(buffer)
      if (len == -1){
        loopFlag = false
      } else {
        byteList ++= buffer.take(len)
      }

    }

    val bytes = byteList.toArray
    val avroFlumeEvent = FlumeAvroSerializer.deSerializer(byteList.toArray)
    val message2 = new String(avroFlumeEvent.getBody.array())
*/




     var input = ""
     while((input = reader.readLine()) != null) {
       if (input != null) {
         println("send to kafka: " + input)
         producer.send(new ProducerRecord(topic, input))
       }
       Thread.sleep(1000)
     }
//    val message = "{\"message_reqpath\":\"\\/_ubc.gif\",\"resphdr_setcookie\":\"\",\"reqhdr_accenc\":\"br, gzip, deflate\",\"resphdr_alloworigin\":\"*\",\"message_sslver\":\"TLSv1.2\",\"network_edgeip\":\"96.16.98.48\",\"network_network\":\"qwest\",\"format\":\"default\",\"netperf_downloadtime\":\"80\",\"message_reqquery\":\"glb_t=ie&glb_w=136213&glb_tm=1530927495820&glb_u=12345678&glb_ksku=123456789&glb_pm=mp&glb_ubcta=[{%22sku%22:%22265597802%22},{%22sku%22:%22269527403%22}]&glb_plf=m&glb_u=12236649&glb_oi=82mmr9d3n84etucdb2kaji9v40&glb_d=10002&glb_s=b01&glb_b=b&glb_p=59-1&glb_k=sz01&glb_dc=1301&glb_od=100131530927148567kaji9v40221597&glb_osr_referrer=originalurl&glb_osr_landing=https%3A%2F%2Fm.zaful.com%2F&glb_cl=https%3A%2F%2Fm.zaful.com%2Ftwo-piece-outfits-e_59%2F&glb_pl=https%3A%2F%2Fm.zaful.com%2F\",\"message_protover\":\"2.0\",\"network_asnum\":\"209\",\"message_fwdhost\":\"s.logsss.com\",\"resphdr_lastmod\":\"Thu, 13 Apr 2017 07:03:44 GMT\",\"geo_city\":\"SEBRING\",\"version\":\"1.0\",\"message_proto\":\"https\",\"geo_region\":\"FL\",\"geo_lat\":\"27.5025\",\"full_request\":\"\\/_ubc.gif?glb_t=ie&glb_w=136213&glb_tm=1530927495820&glb_pm=mp&glb_ubcta=[{%22sku%22:%22265597802%22},{%22sku%22:%22269527403%22}]&glb_plf=m&glb_u=12236649&glb_oi=82mmr9d3n84etucdb2kaji9v40&glb_d=10013&glb_s=b01&glb_b=b&glb_p=59-1&glb_k=sz01&glb_dc=1301&glb_od=100131530927148567kaji9v40221597&glb_osr_referrer=originalurl&glb_osr_landing=https%3A%2F%2Fm.zaful.com%2F&glb_cl=https%3A%2F%2Fm.zaful.com%2Ftwo-piece-outfits-e_59%2F&glb_pl=https%3A%2F%2Fm.zaful.com%2F\",\"cp\":\"552145\",\"netperf_edgeip\":\"96.16.98.48\",\"message_reqport\":\"443\",\"type\":\"cloud_monitor\",\"netperf_asnum\":\"209\",\"netperf_lastbyte\":\"1\",\"netperf_netoriginlatency\":\"27\",\"message_ua\":\"Mozilla\\/5.0 (iPhone; CPU iPhone OS 11_0_1 like Mac OS X) AppleWebKit\\/604.1.38 (KHTML, like Gecko) Version\\/11.0 Mobile\\/15A402 Safari\\/604.1\",\"message_reqmethod\":\"GET\",\"reqhdr_acclang\":\"en-us\",\"message_resplen\":\"372\",\"start\":\"1530927487.690\",\"netperf_midmilertt\":\"21\",\"resphdr_server\":\"Nginx\",\"netperf_firstbyte\":\"1\",\"netperf_lastmilertt\":\"111\",\"message_status\":\"200\",\"id\":\"ef572c175b40197f1f892dfc\",\"resphdr_conn\":\"keep-alive\",\"log_time\":\"2018-07-07 01:38:07\",\"reqhdr_referer\":\"https:\\/\\/m.zaful.com\\/two-piece-outfits-e_59\\/\",\"resphdr_date\":\"Sat, 07 Jul 2018 01:38:07 GMT\",\"biz_referer_3\":\"two-piece-outfits-e_59\",\"netperf_cachestatus\":\"3\",\"message_respct\":\"image\\/gif\",\"geo_long\":\"-81.4505\",\"message_cliip\":\"71.215.92.147\",\"resphdr_accrange\":\"bytes\",\"geo_country\":\"US\",\"message_bytes\":\"372\",\"resphdr_etag\":\"\\\"58ef22d0-174\\\"\",\"network_networktype\":\"\",\"biz_referer_2\":\"m.zaful.com\",\"message_ua_text\":\"Mozilla\\/5.0 (iPhone; CPU iPhone OS 11_0_1 like Mac OS X) AppleWebKit\\/604.1.38 (KHTML, like Gecko) Version\\/11.0 Mobile\\/15A402 Safari\\/604.1\",\"message_reqhost\":\"s.logsss.com\",\"netperf_midmilelatency\":\"50\"}"
//    FlumeAvroSerializer.serializer(new AvroFlumeEvent().)
/*    while (true) {
      producer.send(new ProducerRecord[String,String](topic, message))
      println("send messeage  ..." )
      Thread.sleep(1000)
    }*/
  }

}
