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

public class AppWideTableMapper extends Mapper<Object, Text, Text,NullWritable>{

    private static Logger log = LogManager.getLogger(AppWideTableMapper.class);

    @Override
    protected void map(Object key, Text value, Mapper.Context context) throws IOException, InterruptedException {
        String line = value.toString();
        Text tValue = new Text();
        // 根据 ^A^ 将这一行切分成单词
        String[] strings = line.split(Constant.EVENTSEPERATE);
        for(String str : strings){
            if(str.contains("_app.gif?")){
                String info = StringUtils.substringAfter(str, "_app.gif?");
                if(StringUtils.isNotBlank(info)){
                    String [] infos = info.split("&");
                    Map<String, String> map = MapUtil.getAppWideTableMap();
                    for(String message : infos){
                        message = urlDecoder(message);
                        String mapKey = StringUtils.lowerCase(StringUtils.substringBefore(message,Constant.EQUAL_SIGN));
                        String mapValue = StringUtils.trim(StringUtils.substringAfter(message,Constant.EQUAL_SIGN));
                        map.put(mapKey, StringUtils.isBlank(mapValue) ? "NILL" : mapValue);
                    }
                    final List<String> list = new ArrayList<>(map.size());
                    map.forEach( (k, v) ->  list.add(v) );
                    tValue.set(String.join(Constant.HIVE_SEPERATE, list));
                    context.write(tValue,NullWritable.get());
                }
            }
        }
    }

    private String urlDecoder(String message){
        try {
            return URLDecoder.decode(message, "UTF-8");
        } catch (Exception e) {
            log.error("URL:{0} decode error due to {1}.", message, e);
            return message;
        }
    }
}
