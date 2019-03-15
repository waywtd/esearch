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
import java.text.ParseException;
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
 *  2017年11月07日              chenchao1               Initial.
 *
 * </pre>
 */
public class KafkaLogInterceptor implements Interceptor
{

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaLogInterceptor.class);

    private static List<String> ubcdCache=new ArrayList<String>();

    public KafkaLogInterceptor()
    {
        ubcdCache.add("10002"); //gearbest
        ubcdCache.add("10007"); //rosegal
        ubcdCache.add("10019"); //gamiss
        ubcdCache.add("10013"); //zaful
    }

    @Override
    public void initialize()
    {
    }

    @SuppressWarnings("deprecation")
    @Override
    public Event intercept(Event event)
    {
        String msg = null;
        try
        {
            LOGGER.debug("KafkaLogAnalysisInterceptor intercept start.......");
            msg=new String(event.getBody(),"UTF-8");
            if (!msg.contains(Constants.NGINX_SPLIT_SYMBOL))
            {
                StringBuilder builder = new StringBuilder();
                AvroFlumeEvent avroFlumeEvent = FlumeAvroSerializer.deSerializer(event.getBody());
                JSONObject jsonObject = JSONObject.parseObject(new String(avroFlumeEvent.body.array()));
                builder.append(LogAnalysisServer.constructKafkaLogMsg(jsonObject));
                String domain = LogAnalysisServer.getUbcd(jsonObject);
                boolean tmFlag = builder.toString().contains("\"glb_tm\":\"NILL\"") || builder.toString().contains("\"log_time\":\"NILL\"");
                if (StringUtils.isNotEmpty(domain) && ubcdCache.contains(domain) && !tmFlag)
                {
                    event.setHeaders(LogAnalysisServer.restHeader(jsonObject,domain));
                    event.setBody(builder.toString().getBytes());
                }
                else
                {
                    event = null;
                }
            }
            else
            {
                event = null;
            }
            LOGGER.debug("KafkaLogAnalysisInterceptor intercept end.......");
            return event;
        }
        catch (Exception e)
        {
            LOGGER.info("Exception : {0} error due to {1}", msg, e);
            return null;
        }
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
            return new KafkaLogInterceptor();
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

    public static void main(String[] args) throws UnsupportedEncodingException, ParseException {
        String str = " {\n" +
                "    \"_domain\": \"cdn-log\",\n" +
                "    \"reqhdr_cookie\": \"ORIGINDC=1; AWSELB=4543B18D082E13EC956E32325DD39FAC7FC77834DE7106E9599810EB1321B3C82F918F152576F73582B09061A4F1404970CD1E2574EBDC712242840B8456317138412502BB9B44E97E9622188585F83C6D3DDD06FA\",\n" +
                "    \"message_bytes\": \"372\",\n" +
                "    \"resphdr_accrange\": \"bytes\",\n" +
                "    \"netperf_netoriginlatency\": \"216\",\n" +
                "    \"netperf_lastbyte\": \"1\",\n" +
                "    \"resphdr_date\": \"Tue, 28 Aug 2018 10:22:25 GMT\",\n" +
                "    \"type\": \"cloud_monitor\",\n" +
                "    \"resphdr_lastmod\": \"Thu, 13 Apr 2017 07:03:44 GMT\",\n" +
                "    \"reqhdr_accenc\": \"gzip, deflate, br\",\n" +
                "    \"message_protover\": \"2.0\",\n" +
                "    \"message_reqquery\": \"glb_t=ie&glb_w=2773&glb_tm=1535451745393&glb_pm=mp&glb_filter={%22view%22:60,%22sort%22:%22Recommend%22,%22page%22:1}&glb_ubcta=[{%22sku%22:%22266050002%22},{%22sku%22:%22186478712%22},{%22sku%22:%22265180809%22},{%22sku%22:%22260319307%22}]&glb_plf=pc&glb_d=10013&glb_s=b01&glb_b=b&glb_p=14-1&glb_k=sz01&glb_dc=1301&glb_olk=213532&glb_od=100131535449955763687485&glb_osr=ol%3Dhttps%3A%2F%2Fwww.google.com.au%2F%7Chref%3Dhttp%3A%2F%2Fwww.zafulswimwear.com%2Fswimwear-e_14%2F%3Flkid%3D213532%26gclid%3DEAIaIQobChMIn5TngsOP3QIVBwoqCh0TZQ9CEAAYASAAEgLHrPD_BwE&glb_cl=http%3A%2F%2Fwww.zafulswimwear.com%2Fswimwear-e_14%2F%3Flkid%3D213532%26gclid%3DEAIaIQobChMIn5TngsOP3QIVBwoqCh0TZQ9CEAAYASAAEgLHrPD_BwE&glb_pl=https%3A%2F%2Fwww.google.com.au%2F\",\n" +
                "    \"reqhdr_acclang\": \"en-US,en;q=0.9\",\n" +
                "    \"id\": \"de765f685b852261160f3ba1\",\n" +
                "    \"netperf_firstbyte\": \"1\",\n" +
                "    \"netperf_midmilertt\": \"15\",\n" +
                "    \"message_reqmethod\": \"GET\",\n" +
                "    \"format\": \"default\",\n" +
                "    \"reqhdr_referer\": \"http://www.zafulswimwear.com/swimwear-e_14/?lkid=213532&gclid=EAIaIQobChMIn5TngsOP3QIVBwoqCh0TZQ9CEAAYASAAEgLHrPD_BwE\",\n" +
                "    \"message_reqhost\": \"s.logsss.com\",\n" +
                "    \"message_reqpath\": \"/_ubc.gif\",\n" +
                "    \"version\": \"1.0\",\n" +
                "    \"resphdr_etag\": \"\\\"58ef22d0-174\\\"\",\n" +
                "    \"message_status\": \"200\",\n" +
                "    \"resphdr_alloworigin\": \"*\",\n" +
                "    \"netperf_midmilelatency\": \"7\",\n" +
                "    \"message_cliip\": \"14.203.145.176\",\n" +
                "    \"network_network\": \"\",\n" +
                "    \"_collectTime\": \"2018-08-28 10:22:28\",\n" +
                "    \"resphdr_conn\": \"keep-alive\",\n" +
                "    \"network_asnum\": \"7545\",\n" +
                "    \"netperf_lastmilertt\": \"47\",\n" +
                "    \"message_reqport\": \"443\",\n" +
                "    \"message_ua_text\": \"Mozilla/5.0 (X11; CrOS x86_64 10575.58.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36\",\n" +
                "    \"message_proto\": \"https\",\n" +
                "    \"resphdr_setcookie\": \"\",\n" +
                "    \"geo_region\": \"NSW\",\n" +
                "    \"_ip\": \"127.0.0.1\",\n" +
                "    \"netperf_asnum\": \"7545\",\n" +
                "    \"netperf_edgeip\": \"203.213.73.218\",\n" +
                "    \"message_resplen\": \"372\",\n" +
                "    \"network_networktype\": \"\",\n" +
                "    \"geo_city\": \"SYDNEY\",\n" +
                "    \"full_request\": \"/_ubc.gif?glb_t=ie&glb_w=2773&glb_pm=mp&glb_filter={%22view%22:60,%22sort%22:%22Recommend%22,%22page%22:1}&glb_ubcta=[{%22sku%22:%22266050002%22},{%22sku%22:%22186478712%22},{%22sku%22:%22265180809%22},{%22sku%22:%22260319307%22}]&glb_plf=pc&glb_d=10013&glb_s=b01&glb_b=b&glb_p=14-1&glb_k=sz01&glb_dc=1301&glb_olk=213532&glb_od=100131535449955763687485&glb_osr=ol%3Dhttps%3A%2F%2Fwww.google.com.au%2F%7Chref%3Dhttp%3A%2F%2Fwww.zafulswimwear.com%2Fswimwear-e_14%2F%3Flkid%3D213532%26gclid%3DEAIaIQobChMIn5TngsOP3QIVBwoqCh0TZQ9CEAAYASAAEgLHrPD_BwE&glb_cl=https://www.gearbest.com/xiaomi-_gear/c_11292&glb_pl=https%3A%2F%2Fwww.google.com.au%2F\",\n" +
                "    \"message_sslver\": \"TLSv1.2\",\n" +
                "    \"network_edgeip\": \"203.213.73.218\",\n" +
                "    \"message_respct\": \"image/gif\",\n" +
                "    \"resphdr_server\": \"Nginx\",\n" +
                "    \"netperf_cachestatus\": \"3\",\n" +
                "    \"biz_referer_4\": \"?lkid=213532&gclid=EAIaIQobChMIn5TngsOP3QIVBwoqCh0TZQ9CEAAYASAAEgLHrPD_BwE\",\n" +
                "    \"start\": \"1535451745.709\",\n" +
                "    \"biz_referer_2\": \"www.zafulswimwear.com\",\n" +
                "    \"biz_referer_3\": \"swimwear-e_14\",\n" +
                "    \"cp\": \"552145\",\n" +
                "    \"log_time\": \"2018-08-28 10:22:25\",\n" +
                "    \"geo_long\": \"151.22\",\n" +
                "    \"geo_lat\": \"-33.88\",\n" +
                "    \"geo_country\": \"AU\",\n" +
                "    \"netperf_downloadtime\": \"227\",\n" +
                "    \"message_fwdhost\": \"s.logsss.com\",\n" +
                "    \"message_ua\": \"Mozilla/5.0 (X11; CrOS x86_64 10575.58.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36\"\n" +
                "  }";
        JSONObject jsonObject = JSONObject.parseObject(str);
        String result = LogAnalysisServer.constructKafkaLogMsg(jsonObject);
        boolean tmFlag = result.contains("\"glb_tm\":\"NILL\"");
        System.out.println("tmFlag : " + tmFlag);
        System.out.println(result);
    }
}