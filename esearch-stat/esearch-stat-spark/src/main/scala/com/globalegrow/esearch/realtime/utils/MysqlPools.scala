package com.globalegrow.esearch.realtime.utils

import java.sql.{Connection, DriverManager}

import org.apache.commons.pool2.impl.{DefaultPooledObject, GenericObjectPool}
import org.apache.commons.pool2.{BasePooledObjectFactory, ObjectPool, PooledObject}

class MysqlPoolFactory(connectionProp :ConnectionProp) extends BasePooledObjectFactory[Connection] {
  override def create(): Connection = {
    Class.forName(connectionProp.driver)
    val connection = DriverManager.getConnection(connectionProp.url, connectionProp.username, connectionProp.password)
    connection
  }

  override def wrap(obj: Connection): PooledObject[Connection] = new DefaultPooledObject[Connection](obj)
}

object MysqlPools {

  var objectPool :ObjectPool[Connection] = null

  def apply(connectionProp :ConnectionProp): ObjectPool[Connection] = {
    synchronized{
       if(objectPool == null) {
         objectPool = new GenericObjectPool(new MysqlPoolFactory(connectionProp))
       }
    }
    objectPool
  }
}

case class ConnectionProp(val url :String,
                           val driver:String,
                           val username :String,
                          val password :String
) extends Serializable
