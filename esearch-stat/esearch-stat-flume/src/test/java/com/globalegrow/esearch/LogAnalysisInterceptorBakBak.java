package com.globalegrow.esearch;

import java.util.List;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.apache.flume.serializer.FlumeAvroSerializer;
import org.apache.flume.source.avro.AvroFlumeEvent;
import org.globalegrow.esearch.server.LogAnalysisServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.globalegrow.esearch.constant.Constant;
import com.globalegrow.esearch.util.StringUtils;
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
public class LogAnalysisInterceptorBakBak implements Interceptor
{

    private static final Logger LOGGER = LoggerFactory.getLogger(LogAnalysisInterceptorBakBak.class);
    
    private String ubcd;
    
    public LogAnalysisInterceptorBakBak()
    {
        
    }
    
    public LogAnalysisInterceptorBakBak(String ubcd)
    {
        this.ubcd=ubcd;
    }
    
    @Override
    public void initialize()
    {
    }

    @SuppressWarnings("deprecation")
    @Override
    public Event intercept(Event event) 
    {
        LOGGER.info("LogAnalysisInterceptor intercept start.......");
        AvroFlumeEvent avroFlumeEvent = FlumeAvroSerializer.deSerializer(event.getBody());
        String eventStr=new String(avroFlumeEvent.body.array());
        JSONObject jsonObject = JSONObject.parseObject(eventStr);
        LOGGER.info("eventStr:"+eventStr);
        String ubcdValue=LogAnalysisServer.getUbcd(jsonObject);
        
        if(StringUtils.isNotEmpty(ubcdValue) && ubcd.equals(ubcdValue))
        {
            event.setBody(eventStr.getBytes());
        }  else {
            event=null;
        }
        LOGGER.info("LogAnalysisInterceptor intercept end.......");
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
        
        private String ubcd;
        
        @Override
        public Interceptor build()
        {
            return new LogAnalysisInterceptorBakBak(ubcd);
        }

        @Override
        public void configure(Context context)
        {
            ubcd=context.getString(Constant.UBCD);
        }
    }
    
    @Override
    public void close()
    {

    }
}
