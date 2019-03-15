package com.globalegrow.esearch.mq.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * <pre>
 * 
 *  File: HadoopUtil.java
 * 
 *  Copyright (c) 2016, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2016年7月29日				chengmo				Initial.
 *
 * </pre>
 */
public class HadoopUtil
{
    private static Configuration globalConfiguration = new Configuration();
    private static Configuration conf = null;
    private static boolean isCluster;
    private static String mapredJar;
    private static String queueName;
    private static Map<String, String> properties;

    static
    {
        try
        {
            URL path = HadoopUtil.class.getResource("/mapred-site.xml");
            if (path != null)
            {
                globalConfiguration.addResource(new File(path.getPath()).toURI().toURL());
            }

            String url = ClassUtils.jarForClass(HadoopUtil.class);
            // local
            if (url.contains("target"))
            {
                mapredJar = StringUtils.substringBefore(url, "target") + "target/esearch-hadoop.jar";
            }
            else
            {
                mapredJar = "lib/mapreduce/esearch-hadoop.jar";
            }

            initializeProperties();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

   public static void initializeProperties()
    {
        URL url = HadoopUtil.class.getResource("/rabbitmq-consumer.properties");
        if (url != null)
        {
            properties = PropertiesUtil.loadPropertiesFile(url.getPath());
        }
    }

    /**
     * New Configuration with default, running in local mode
     *
     * @return
     */
    public static Configuration newConfiguration()
    {
        return newConfiguration(false);
    }

    /**
     * New Configuration with hadoop.properties -> hadoop.mapreduce.cluster.mode
     *
     * @return
     */
    public static Configuration newConfigurationWith()
    {
        return newConfiguration(isCluster);
    }

    /**
     * New Configuration whether specified MapReduce local mode
     *
     * @param isCluster True if we're running in cluster mode
     * @return
     */
    public static Configuration newConfiguration(boolean isCluster)
    {
        Configuration conf = new Configuration();

        if (StringUtils.isNotBlank(queueName))
        {
            conf.set("mapred.job.queue.name", "daily");
        }
        if (isCluster)
        {
            conf.set("mapred.jar", mapredJar);
        }
        else
        {
            conf.set("mapreduce.framework.name", "local");
            conf.set("mapreduce.map.output.compress.codec", "org.apache.hadoop.io.compress.DefaultCodec");
        }
        conf.addResource(HadoopUtil.class.getClass().getResource("/core-site.xml"));
        conf.addResource(HadoopUtil.class.getClass().getResource("/hdfs-site.xml"));
        return conf;
    }
    
    public static Configuration getConf(String env){
        if(StringUtils.isNotEmpty(env)){
            conf = new Configuration();
            conf.addResource(HadoopUtil.class.getClass().getResource("/core-site.xml"));
            conf.addResource(HadoopUtil.class.getClass().getResource("/hdfs-site.xml"));
        }
        return conf;
    }

    public static Configuration getConf()
    {
        conf = new Configuration();
        conf.addResource(HadoopUtil.class.getClass().getResource("/core-site.xml"));
        conf.addResource(HadoopUtil.class.getClass().getResource("/hdfs-site.xml"));
        return conf;
    }

    public static int getMapMemoryMb(int newValue)
    {
        if (newValue >= 1024)
        {
            return newValue;
        }
        return getInt("mapreduce.map.memory.mb", 1024);
    }

    public static int getReduceMemoryMb(int newValue)
    {
        if (newValue >= 1024)
        {
            return newValue;
        }
        return getInt("mapreduce.reduce.memory.mb", 1024);
    }

    private static int getInt(String name, int defValue)
    {
        return globalConfiguration.getInt(name, 0);
    }

    public static Map<String, String> getProperties(){ return properties; }

    public static void main(String[] args)
    {
        System.out.println(HadoopUtil.class.getClass().getResource("/core-site.xml"));
        System.out.println(HadoopUtil.getConf("dev"));
        System.out.println(HadoopUtil.getConf());
        System.out.println(HadoopUtil.mapredJar);
    }
}
