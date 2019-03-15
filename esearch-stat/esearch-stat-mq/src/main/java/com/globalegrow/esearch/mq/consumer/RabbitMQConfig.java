package com.globalegrow.esearch.mq.consumer;

import com.globalegrow.esearch.mq.util.PropertiesUtil;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <pre>
 *
 *  File: RabbitMQConfig.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018年07月18日			Nieruiqun				Initial.
 *
 * </pre>
 */
public class RabbitMQConfig {

    private static final String LOCATION_CONSUMER = "/rabbitmq-consumer.properties";
    public static final String CONSUMER_MQ_VIRTUAL_HOST = "rabbitmq.{0}.virtual.host";
    public static final String CONSUMER_MQ_QUEUE = "rabbitmq.{0}.queue";
    private static final String RABBITMQ_SERVER_HOSTS = "rabbitmq.server.hosts";
    private static final String RABBITMQ_SERVER_PORT = "rabbitmq.server.port";
    private static final String RABBITMQ_USERNAME = "rabbitmq.username";
    private static final String RABBITMQ_PASSWORD = "rabbitmq.password";
    private static final String RABBITMQ_HDFS_OUTPUT_PATH = "rabbitmq.hdfs.output.path";

    private static Map<String, String> consumerProps;

    static
    {
        consumerProps = PropertiesUtil.loadPropertiesFile(LOCATION_CONSUMER);
    }

    public static Map<String, String> getConsumerProps() {
        return consumerProps;
    }

    public static String getOutputPathPrefix() {
        return consumerProps.get(RABBITMQ_HDFS_OUTPUT_PATH);
    }

    public static ConnectionFactory getConnectionFactory(){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(consumerProps.get(RABBITMQ_SERVER_HOSTS));
        factory.setPort(Integer.parseInt(consumerProps.get(RABBITMQ_SERVER_PORT)));
        factory.setUsername(consumerProps.get(RABBITMQ_USERNAME));
        factory.setPassword(consumerProps.get(RABBITMQ_PASSWORD));
        return factory;
    }

    public static Map<String, Map<String, String>> getConsumerQueue(){
        Map<String, Map<String, String>> consumerQueueMap = new LinkedHashMap<>();
        Map<String, String> virtualHostMap = consumerProps.entrySet().stream()
                .filter( map -> map.getKey().startsWith("rabbitmq.") && map.getKey().endsWith(".virtual.host"))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
        virtualHostMap.keySet().forEach( key -> {
            String domain = StringUtils.substringBetween(key, "rabbitmq.", ".virtual.host");
            Map<String, String> prop = new LinkedHashMap<>();
            prop.put(MessageFormat.format(CONSUMER_MQ_VIRTUAL_HOST, domain), consumerProps.get(MessageFormat.format(CONSUMER_MQ_VIRTUAL_HOST, domain)));
            prop.put(MessageFormat.format(CONSUMER_MQ_QUEUE, domain), consumerProps.get(MessageFormat.format(CONSUMER_MQ_QUEUE, domain)));
            consumerQueueMap.put(domain, prop);
        });
        return consumerQueueMap;
    }

}
