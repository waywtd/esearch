package com.globalegrow.esearch.stat.event.mapred.widetable.mapper;

import com.globalegrow.esearch.constant.Constant;
import com.globalegrow.esearch.util.MapUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *
 *  File: WideTableDriver.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018年3月17日			nieruiqun				Initial.
 *
 * </pre>
 */
public class WideTableMapper extends Mapper<Object, Text, Text,NullWritable>
{
    private static Logger log = LogManager.getLogger(WideTableMapper.class);

    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        try {
            // 转义url
            line = URLDecoder.decode(line, "UTF-8");
        } catch (Exception e) {
            log.error("URL:{0} decode error due to {1}.", line, e);
        }
        Text tValue = new Text();
        //根据空格将这一行切分成单词
        String[] strings = line.split(Constant.EVENTSEPERATE);
        for(String str : strings){
            if(str.contains("_ubc.gif?")){
                String info = StringUtils.substringAfter(str, "_ubc.gif?");
                if(StringUtils.isNotBlank(info)){
                    String [] infos = info.split("&");
                    Map<String, String> map = MapUtil.getWideTableMap();
                    for(String message : infos){
                        String mapKey = StringUtils.lowerCase(StringUtils.substringBefore(message,Constant.EQUAL_SIGN));
                        String mapValue = StringUtils.trim(StringUtils.substringAfter(message,Constant.EQUAL_SIGN));
                        map.put(mapKey, StringUtils.isBlank(mapValue) ? "NILL" : mapValue);
                    }
                    map.put("ip", strings[0]);
                    map.put("city", strings[strings.length-2]);
                    map.put("country", strings[strings.length-1]);
                    final List<String> list = new ArrayList<>(map.size());
                    map.forEach( (k, v) ->  list.add(v) );
                    tValue.set(String.join(Constant.HIVE_SEPERATE, list));
                    context.write(tValue,NullWritable.get());
                }
            }
        }
    }

}
