package com.globalegrow.esearch.mq.util;

import com.globalegrow.esearch.mq.util.log.ELog;
import com.globalegrow.esearch.mq.util.log.LogFactory;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;

public class KafkaUtils {

    private static Producer producer = null;
    private static final String RABBITMQ_KAFKA_BROKERS = "rabbitmq.kafka.brokers";
    private static final String RABBITMQ_KAFKA_CONFIG = "rabbitmq.kafka.config.";
    private static final ELog log = LogFactory.getLog(KafkaUtils.class);

    static {
        Map<String, String> properties = PropertiesUtil.loadPropertiesFile("/rabbitmq-consumer.properties");
        String brokers = properties.get(RABBITMQ_KAFKA_BROKERS);
        Map<String,String> configMap = new HashMap();
        properties.forEach((key, value) -> {
            if (key.startsWith(RABBITMQ_KAFKA_CONFIG)) {
                configMap.put(key.substring(RABBITMQ_KAFKA_CONFIG.length()), value);
            }
        });
        configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        configMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producer = new KafkaProducer(configMap);
    }

    public static void send(String topic ,String message) {
            producer.send(new ProducerRecord(topic, message));
    }

}
