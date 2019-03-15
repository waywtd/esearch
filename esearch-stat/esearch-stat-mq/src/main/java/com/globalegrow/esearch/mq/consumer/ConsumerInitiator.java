package com.globalegrow.esearch.mq.consumer;

import com.globalegrow.esearch.mq.util.log.ELog;
import com.globalegrow.esearch.mq.util.log.LogFactory;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * <pre>
 *
 *  File: ConsumerInitiator.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018年07月19日			Nieruiqun				Initial.
 *
 * </pre>
 */
public class ConsumerInitiator {

    private static final ELog log = LogFactory.getLog(ConsumerInitiator.class);

    public static void start() throws IOException {
        ExecutorService executorService = null;
        try {
            log.info("Start to initialize message queue consumer.");
            Map<String, Map<String, String>> consumerQueue = RabbitMQConfig.getConsumerQueue();
            if (!consumerQueue.isEmpty()) {
                executorService = new ScheduledThreadPoolExecutor(consumerQueue.size(),
                        new BasicThreadFactory.Builder().namingPattern("query-order-scheduled-thread-%d").daemon(false).build());
                for (String domain : consumerQueue.keySet()) {
                    RabbitMQConsumer consumer = new RabbitMQConsumer();
                    consumer.setDomain(domain);
                    Map<String, String> prop = consumerQueue.get(domain);
                    consumer.setQueue(prop.get(MessageFormat.format(RabbitMQConfig.CONSUMER_MQ_QUEUE, domain)));
                    consumer.setVirtualHost(prop.get(MessageFormat.format(RabbitMQConfig.CONSUMER_MQ_VIRTUAL_HOST, domain)));
                    executorService.execute(consumer);
                    log.info("Consumer of domain:{0},prop:{1} begin to run.", domain, prop);
                }
                log.info("Initializing message queue consumer success!");
            }
        } catch (Exception e) {
            log.error("Failed to initialize message queue consumer due to {0}.", e);
        }
    }

}

