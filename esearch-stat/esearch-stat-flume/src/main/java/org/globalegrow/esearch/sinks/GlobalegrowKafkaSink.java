package org.globalegrow.esearch.sinks;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.flume.Channel;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.Transaction;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.globalegrow.esearch.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;

/**
 * <pre>
 *
 *  File: Constant.java
 *
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,                   Who,                    What;
 *  2017年11月15日             lizhaohui               Initial.
 *
 * </pre>
 */
public class GlobalegrowKafkaSink extends AbstractSink implements Configurable
{

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalegrowKafkaSink.class);

    @SuppressWarnings("unused")
    private Context context;
    private Properties parameters;
    private Producer<String, String> producer;

    private static final String PARTITION_KEY_NAME = "custom.partition.key";
    private static final String CUSTOME_TOPIC_KEY_NAME = "custom.topic.name";


    public void configure(Context context)
    {
        this.context = context;
        ImmutableMap<String, String> props = context.getParameters();
        this.parameters = new Properties();
        for (Map.Entry<String, String> entry : props.entrySet())
        {
            this.parameters.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public synchronized void start()
    {
        super.start();
        ProducerConfig config = new ProducerConfig(this.parameters);
        this.producer = new Producer<String, String>(config);
    }

    public Status process()
    {
        Status status = null;
        Channel channel = getChannel();
        Transaction transaction = channel.getTransaction();

        try
        {
            transaction.begin();
            Event event = channel.take();
            if (event != null)
            {
                String partitionKey = (String) parameters.get(PARTITION_KEY_NAME);
                String topic = Preconditions.checkNotNull((String) this.parameters.get(CUSTOME_TOPIC_KEY_NAME),"topic name is required");
                String eventData = new String(event.getBody(), Constants.UTF8);
                KeyedMessage<String, String> data = (partitionKey.isEmpty()) ? new KeyedMessage<String, String>(topic,eventData) : new KeyedMessage<String, String>(topic, partitionKey, eventData);
                LOGGER.debug("Sending Message to Kafka : [" + topic + ":" + eventData + "]");
                producer.send(data);
                transaction.commit();
                LOGGER.debug("Send message success");
                status = Status.READY;
            }
            else
            {
                transaction.rollback();
                status = Status.BACKOFF;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("Send message failed!");
            transaction.rollback();
            status = Status.BACKOFF;
        }
        finally
        {
            transaction.close();
        }
        return status;
    }

    @Override
    public void stop()
    {
        producer.close();
    }
}