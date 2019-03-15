package com.globalegrow.esearch.stat.event.mapred.stmtr.website.weight.bpoint.mapper;

import com.alibaba.fastjson.JSONArray;
import com.globalegrow.esearch.constant.Constant;
import com.globalegrow.esearch.constant.MethodConstant;
import com.globalegrow.esearch.stat.event.services.ServiceParser;
import com.globalegrow.esearch.util.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 
 *  File: SkuWeightMapper.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2017年12月5日				lizhaohui				Initial.
 *
 * </pre>
 */
public class SkuWeightBPointMapper extends Mapper<LongWritable, Text, Text, Text>
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
    protected void setup(Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException,InterruptedException
    {
        super.setup(context);
        Configuration conf = context.getConfiguration();
        siteCode = conf.get(Constant.SITE_CODE);
        site = conf.get(Constant.SITE);
        builder=new StringBuilder();
        builder.append(conf.get(Constant.YEAR)).append(Constant.IN_THE_LINE).append(conf.get(Constant.MONTH)).append(Constant.IN_THE_LINE).append(conf.get(Constant.DAY));
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected void map(LongWritable offset, Text inputValue, Mapper<LongWritable, Text, Text, Text>.Context context)
            throws IOException, InterruptedException
    {
        Map<String,String> eventMap=ServiceParser.searchBoxEvent01(inputValue.toString(),site, siteCode,builder.toString());
        Map<String,String> resultMap=ServiceParser.mapperEmit(eventMap,MethodConstant.SKU_WEIGHT_B_POINT);
        if (StringUtils.isNotEmpty(resultMap) && resultMap.size() > 0)
        {
            String jsonArr=resultMap.get(Constant.RESULT);
            List<Map<String,String>> result=(List<Map<String, String>>) JSONArray.parse(jsonArr);
            for(Map<String,String> mapResult:result){
                key=new Text(mapResult.get(Constant.KEY));
                value=new Text(mapResult.get(Constant.VALUE)+Constant.HIVE_SEPERATE+new String(inputValue.toString().getBytes(), Constant.UTF8));
                context.write(key, value);
            }
        }
    }
}