package com.globalegrow.esearch.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.globalegrow.esearch.mq.util.KafkaUtils;
import com.globalegrow.esearch.mq.util.MapperUtil;
import com.globalegrow.esearch.mq.util.log.ELog;
import com.globalegrow.esearch.mq.util.log.LogFactory;
import com.rabbitmq.client.*;

import java.io.IOException;

public class RabbitMQConsumer implements Runnable {

    private static final ELog log = LogFactory.getLog(RabbitMQConsumer.class);

    private String domain;
    private String queue;
    private String virtualHost;

    @Override
    public void run() {
        ConnectionFactory factory = RabbitMQConfig.getConnectionFactory();
        factory.setVirtualHost(this.virtualHost);
        consumer(factory);
    }

    public void setDomain(String domain){
        this.domain = domain;
    }

    public void setQueue(String queue){
        this.queue = queue;
    }

    public void setVirtualHost(String virtualHost){
        this.virtualHost = virtualHost;
    }

    private void consumer(ConnectionFactory factory) {
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(this.queue, true, false, false, null);
            log.info("Connection [host:{0}, virtualHost:{1}, queue:{2}] success.", factory.getHost(), this.virtualHost, this.queue);

            Consumer consumer = new DefaultConsumer(channel) {
                long lastMessageTime = System.currentTimeMillis();
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    log.info("Receive domain:{1} message:[{0}].", message, domain);
                    String outputPathPrefix = RabbitMQConfig.getOutputPathPrefix();
                    lastMessageTime =  MapperUtil.writeToHdfs(domain, lastMessageTime, message, outputPathPrefix);
                    KafkaUtils.send( domain + "-order", JSONObject.toJSONString(MapperUtil.getLogMap(message)));
                }
            };
            channel.basicConsume(this.queue, true, consumer);
        } catch (Exception e) {
            log.error("Consumer [domain:{0},queue:{1},virtualHost{2}] RabbitMQ error due to {3}.", domain, queue, virtualHost, e);
            consumer(factory);
        }
    }

}
