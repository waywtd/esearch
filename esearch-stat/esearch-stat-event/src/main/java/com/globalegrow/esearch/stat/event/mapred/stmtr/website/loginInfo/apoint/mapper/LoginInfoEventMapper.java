package com.globalegrow.esearch.stat.event.mapred.stmtr.website.loginInfo.apoint.mapper;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.globalegrow.esearch.constant.Constant;
import com.globalegrow.esearch.constant.MethodConstant;
import com.globalegrow.esearch.stat.event.services.ServiceParser;
import com.globalegrow.esearch.util.StringUtils;

/**
 * <pre>
 * 
 *  File: LoginInfoEventMapper.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2017年12月6日				lizhaohui				Initial.
 *
 * </pre>
 */
public class LoginInfoEventMapper extends Mapper<LongWritable, Text, Text, Text>
{
    
    /**
     * 结果value
     */
    private Text value = null;

    /**
     * key值
     */
    private Text key = null;

    private StringBuilder builder = null;

    private String siteCode = "";

    private String site = "";
    
    @Override
    public void setup(Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException,
            InterruptedException
    {
        Configuration conf = context.getConfiguration();
        siteCode = conf.get(Constant.SITE_CODE);
        site = conf.get(Constant.SITE);
        builder=new StringBuilder();
        builder.append(conf.get(Constant.YEAR)).append(Constant.IN_THE_LINE).append(conf.get(Constant.MONTH)).append(Constant.IN_THE_LINE).append(conf.get(Constant.DAY));
    }
    
    @Override
    public void map(LongWritable offset, Text inputValue, Mapper<LongWritable, Text, Text, Text>.Context context)
            throws IOException, InterruptedException
    {
        Map<String,String> eventMap=ServiceParser.searchBoxEvent01(inputValue.toString(), site, siteCode,builder.toString());
        Map<String,String> resultMap=ServiceParser.mapperEmit(eventMap,MethodConstant.LOGIN_INFO_A_POINT);
        if (StringUtils.isNotEmpty(resultMap) && resultMap.size() > 0)
        {
            key=new Text(resultMap.get(Constant.KEY));
            value=new Text(resultMap.get(Constant.VALUE));
            context.write(key, value);
        }
    }
}