package com.globalegrow.esearch.cstreaming.pools

import java.util.Properties

import org.apache.commons.pool2.impl.{DefaultPooledObject, GenericObjectPool, GenericObjectPoolConfig}
import org.apache.commons.pool2.{BasePooledObjectFactory, ObjectPool, PooledObject}
import org.apache.kafka.clients.producer.KafkaProducer


class KafkaPooducerFactory(kafkaConfig : Properties) extends BasePooledObjectFactory[KafkaProducer[String,String]] {
  override def create(): KafkaProducer[String,String] = {
   new KafkaProducer(kafkaConfig)
  }

  override def wrap(obj: KafkaProducer[String,String]): PooledObject[KafkaProducer[String,String]] = {
    new DefaultPooledObject[KafkaProducer[String, String]](obj)
  }
}

object KafkaProducerPool {

   var pool :GenericObjectPool[KafkaProducer[String,String]] = null

  def apply(kafkaConfig :Properties) :ObjectPool[KafkaProducer[String,String]] = {
    synchronized{
      if (pool == null) {
        pool = new GenericObjectPool(new KafkaPooducerFactory(kafkaConfig))
      }
    }
    pool
  }

}
