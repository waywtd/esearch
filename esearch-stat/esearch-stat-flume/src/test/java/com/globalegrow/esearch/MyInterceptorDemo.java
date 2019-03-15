package com.globalegrow.esearch;

import java.util.List;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.apache.flume.serializer.FlumeAvroSerializer;
import org.apache.flume.source.avro.AvroFlumeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;


/**
 * <pre>
 * 
 *  File: LogAnalysisInterceptor.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,                   Who,                    What;
 *  2017年11月07日              lizhaohui               Initial.
 *
 * </pre>
 */
public class MyInterceptorDemo implements Interceptor
{

    private static final Logger LOGGER = LoggerFactory.getLogger(MyInterceptorDemo.class);
    
    public MyInterceptorDemo()
    {
        
    }
    
    @Override
    public void initialize()
    {
    }

    @SuppressWarnings("deprecation")
    @Override
    public Event intercept(Event event) 
    {
        LOGGER.info("MyInterceptorDemo intercept start.......");
        AvroFlumeEvent avroFlumeEvent = FlumeAvroSerializer.deSerializer(event.getBody());
        String eventStr=new String(avroFlumeEvent.body.array());
        LOGGER.info(eventStr);
        LOGGER.info("MyInterceptorDemo intercept end.......");
        return event;
    }
    

    @Override
    public List<Event> intercept(List<Event> events)
    {
        List<Event> intercepted = Lists.newArrayListWithCapacity(events.size());
        for (Event event : events)
        {
            Event interceptedEvent = intercept(event);
            if (interceptedEvent != null)
            {
                intercepted.add(interceptedEvent);
            }
        }
        return intercepted;
    }
    
    public static class Builder implements Interceptor.Builder
    {
        
        @Override
        public Interceptor build()
        {
            return new MyInterceptorDemo();
        }

        @Override
        public void configure(Context context)
        {
        }
    }
    
    @Override
    public void close()
    {

    }
}
