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
import com.globalegrow.esearch.constant.NginxFieldConstant;
import com.globalegrow.esearch.util.StringUtils;
import com.google.common.collect.Lists;


/**
 * <pre>
 * 
 *  File: LogAnalysisInterceptorBak.java
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
public class LogAnalysisInterceptorBak implements Interceptor
{

    private static final Logger LOGGER = LoggerFactory.getLogger(LogAnalysisInterceptorBak.class);
    
    @Override
    public void initialize()
    {
    }

    @SuppressWarnings("deprecation")
    @Override
    public Event intercept(Event event)
    {
        LOGGER.debug("LogAnalysisInterceptorBak intercept start.......");
        AvroFlumeEvent avroFlumeEvent = FlumeAvroSerializer.deSerializer(event.getBody());
        JSONObject jsonObject = JSONObject.parseObject(new String(avroFlumeEvent.body.array()));
        if(StringUtils.isNotEmpty(jsonObject) && Constant.S_LOGSSS_COM.equals(jsonObject.getString(NginxFieldConstant.MESSAGE_REQHOST)))
        {
            StringBuilder builder=new StringBuilder();
            builder.append(LogAnalysisServer.constructLogMsg(jsonObject));
            event.setBody(builder.toString().getBytes());
        }
        else
        {
            event=null;
        }
        LOGGER.debug("LogAnalysisInterceptorBak intercept end.......");
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
            return new LogAnalysisInterceptorBak();
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
