package com.globalegrow.esearch.mq.util;

import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.globalegrow.esearch.mq.util.log.ELog;
import com.globalegrow.esearch.mq.util.log.LogFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class MapperUtil {

    private static final ELog log = LogFactory.getLog(MapperUtil.class);

    private static final String HIVE_DELIMITER = "\001";
    private static final String LINE_DELIMITER = System.getProperty("line.separator");
    private static final int WRITE_DATA_SIZE =  100;
    private static Map<String, Set<String>> cacheMap = new ConcurrentHashMap<>();

    public static long writeToHdfs(String ubcd, long lastTime, String message, String pathPrefix){
        String line = getTableString(message);
        if(StringUtils.isNoneBlank(line)){
            log.debug("receive one line data : {0}.",line);
            Set<String> domainData = cacheMap.getOrDefault(ubcd, new HashSet<>());
            cacheMap.put(ubcd, domainData);
            lastTime = loadDataToHdfs(ubcd, pathPrefix, line, lastTime, domainData);
        }
        return lastTime;
    }

    private static long loadDataToHdfs(String ubcd, String pathPrefix, String line, long lastTime, Set<String> domainData){
        long currentTime = Calendar.getInstance().getTimeInMillis();
        String outputPath = getHdfsPath(ubcd, pathPrefix, lastTime);
        synchronized (domainData){
            if((DateUtil.isAllListLead(lastTime,currentTime) && domainData.size() != 0) || domainData.size() >= WRITE_DATA_SIZE){
                String messages = String.join(LINE_DELIMITER, domainData) + LINE_DELIMITER;
                writeToHdfs(messages, outputPath);
                domainData.clear();
            }
            domainData.add(line);
        }
        return currentTime;
    }

    private static void writeToHdfs(String messages, String outputPath){
        Path path = new Path(outputPath);
        try {
            FileSystem fileSystem = getFileSystem();
            if (!fileSystem.exists(path)) {
                fileSystem.createNewFile(path);
                log.info("Success create File path[{0}].", outputPath);
                fileSystem.close();
                fileSystem = getFileSystem();
            }
            FSDataOutputStream outputStream = fileSystem.append(path);
            outputStream.write(messages.getBytes());
            outputStream.flush();
            outputStream.close();
            log.info("Write to hdfs[path:{0}] successful!", outputPath);
        } catch (IOException e) {
            log.error("Write to hdfs[path:{0}] fail due to {1}.", outputPath, e);
        }
    }

    private static FileSystem getFileSystem() throws IOException {
        Configuration conf = HadoopUtil.getConf();
        conf.set("fs.hdfs.impl",org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        conf.set("fs.file.impl",org.apache.hadoop.fs.LocalFileSystem.class.getName());
        conf.setBoolean("dfs.support.append",true);
        return FileSystem.get(conf);
    }

    private static Map<String, String> getWideTableMap() {

        Map<String, String> map =  new LinkedHashMap<>();
        // 行为类型
        map.put("t", "NILL");
        //当前时间戳
        map.put("tm", "NILL");
        // 子事件详情
        map.put("x", "NILL");
        //商品详情
        map.put("skuinfo", "NILL");
        // session ID
        map.put("oi", "NILL");
        // 站点标识
        map.put("d", "NILL");
        //页面小类
        map.put("s", "NILL");
        //页面大类
        map.put("b", "NILL");
        // 国家编码
        map.put("dc", "NILL");
        // cookie ID
        map.put("od", "NILL");
        // 着陆页详情url
        map.put("osr_landing", "NILL");
        // 外部来源
        map.put("osr_referrer", "NILL");
        // 子时间属性
        map.put("ubcta", "NILL");
        map.put("user_agent","NILL");
        map.put("real_ip","NILL");
        map.put("u","NILL");
        map.put("plf","NILL");
        map.put("cl","NILL");
        return map;
    }

    public static String getTableString(String jsonString){
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            Map<String,String> logMap = getWideTableMap();
            jsonObject.forEach((key,value) -> {
                if(logMap.containsKey(key)){
                    String valueString = Objects.isNull(value) || StringUtils.isBlank(value.toString()) ? "NILL" : value.toString();
                    logMap.put(key, valueString);
                }
            });
            List<String> values = new ArrayList<>();
            logMap.forEach( (k, v) ->  values.add(v));
            return String.join(HIVE_DELIMITER, values);
        }catch (Exception e){
            log.error("Convert message:{0} to line string error due to {1}.", jsonString, e);
            return null;
        }
    }

    public static Map<String, String> getLogMap(String jsonString) {
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        Map<String,String> logMap = getWideTableMap();
        jsonObject.forEach((key,value) -> {
            if(logMap.containsKey(key)){
                String valueString = Objects.isNull(value) || StringUtils.isBlank(value.toString()) ? "NILL" : value.toString();
                logMap.put(key, valueString);
            }
        });
        return logMap;
    }

    private static String getHdfsPath(String ubcd, String pathPrefix, long time){
        Date date = new Date(time);
        String year = DateUtil.getYear(date).toString();
        String month = DateUtil.getMonth(date);
        String day = DateUtil.getDay(date);
        String filePath = pathPrefix + "/ubcd=" + ubcd + "/year=" + year + "/month=" + month + "/day=" + day + "/php-"+ year + month + day + ".log";
        return filePath;
    }

}
