package com.globalegrow.esearch.stat.event.mapred.stmtr.ods.checkout.mapper;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.globalegrow.esearch.constant.Constant;
import com.globalegrow.esearch.constant.MethodConstant;
import com.globalegrow.esearch.stat.event.services.ServiceParser;

/**
 * <pre>
 * 
 *  File: UserLoginInfoMapper.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2017年11月24日				lizhaohui				Initial.
 *
 * </pre>
 */
public class UserLoginEventMapper extends Mapper<LongWritable,Text, Text, NullWritable>
{

    /**
     * 结果value
     */
    private Text key = null;

    private String siteCode = "";
    
    private String site="";
    
    private StringBuilder builder=null;
    
    @Override
    protected void setup(Mapper<LongWritable, Text, Text, NullWritable>.Context context) throws IOException,
            InterruptedException
    {
        super.setup(context);
        Configuration conf = context.getConfiguration();
        siteCode = conf.get(Constant.SITE_CODE);
        site = conf.get(Constant.SITE);
        builder=new StringBuilder();
        builder.append(conf.get(Constant.YEAR)).append(Constant.IN_THE_LINE).append(conf.get(Constant.MONTH)).append(Constant.IN_THE_LINE).append(conf.get(Constant.DAY));
    }
    
    @Override
    protected void map(LongWritable offset, Text inputValue, Mapper<LongWritable, Text, Text, NullWritable>.Context context)
            throws IOException, InterruptedException
    {
        Map<String,String> eventMap=ServiceParser.searchBoxEvent(inputValue.toString(), site, siteCode,builder.toString());
        Map<String,String> resultMap=ServiceParser.mapperEmit(eventMap,MethodConstant.USER_LOGIN_EVENT);
        if(resultMap.size()>0 && resultMap.containsKey(Constant.KEY))
        {
            key=new Text(resultMap.get(Constant.KEY));
            context.write(key, NullWritable.get());
        }
    }
}

