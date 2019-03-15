package org.globalegrow.esearch.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.apache.flume.serializer.FlumeAvroSerializer;
import org.apache.flume.source.avro.AvroFlumeEvent;
import org.globalegrow.esearch.constant.Constants;
import org.globalegrow.esearch.server.LogAnalysisServer;
import org.globalegrow.esearch.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


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
public class LogInterceptor implements Interceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogInterceptor.class);

    private static List<String> ubcdCache = new ArrayList<>();


    public LogInterceptor() {
        // gearbest
        ubcdCache.add("10002");
        // rosegal
        ubcdCache.add("10007");
        // gamiss
        ubcdCache.add("10019");
        // zaful
        ubcdCache.add("10013");
    }

    @Override
    public void initialize() {
    }

    @SuppressWarnings("deprecation")
    @Override
    public Event intercept(Event event) {
        try {
            LOGGER.debug("LogAnalysisInterceptor intercept start.......");
            String msg=new String(event.getBody(),"UTF-8");
            if (!msg.contains(Constants.NGINX_SPLIT_SYMBOL)) {
                StringBuilder builder = new StringBuilder();
                AvroFlumeEvent avroFlumeEvent = FlumeAvroSerializer.deSerializer(event.getBody());
                JSONObject jsonObject = JSONObject.parseObject(new String(avroFlumeEvent.getBody().array()));
                builder.append(LogAnalysisServer.constructLogMsg(jsonObject));
                String domain = LogAnalysisServer.getUbcd(jsonObject);
                if (StringUtils.isNotEmpty(domain) && ubcdCache.contains(domain)) {
                    event.setHeaders(LogAnalysisServer.restHeader(jsonObject,domain));
                    event.setBody(builder.toString().getBytes());
                } else {
                    event = null;
                }
            } else {
                event = null;
            }
            LOGGER.debug("LogAnalysisInterceptor intercept end.......");
            return event;
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("LogAnalysisInterceptor intercept [body={0}] error due to {1}.", event.getBody(), e);
            return null;
        }
    }


    @Override
    public List<Event> intercept(List<Event> events) {
        List<Event> intercepted = Lists.newArrayListWithCapacity(events.size());
        for (Event event : events) {
            Event interceptedEvent = intercept(event);
            if (interceptedEvent != null) {
                intercepted.add(interceptedEvent);
            }
        }
        return intercepted;
    }

    public static class Builder implements Interceptor.Builder {
        @Override
        public Interceptor build()
        {
            return new LogInterceptor();
        }

        @Override
        public void configure(Context context) {
        }
    }

    @Override
    public void close() {
    }
}