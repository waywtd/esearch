package com.globalegrow.esearch.stat.event.mapred.stmtr.ods.clickcategory.mapper;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import com.globalegrow.esearch.constant.Constant;
import com.globalegrow.esearch.constant.FieldConstant;
import com.globalegrow.esearch.constant.MethodConstant;
import com.globalegrow.esearch.constant.PathConstant;
import com.globalegrow.esearch.stat.event.services.ServiceParser;
import com.globalegrow.esearch.util.CommonUtil;
import com.globalegrow.esearch.util.StringUtils;

/**
 * <pre>
 * 
 *  File: ClickCategoryEventMapper.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2017年9月20日				lizhaohui				Initial.
 *
 * </pre>
 */
public class ClickCategoryEventMapper extends Mapper<LongWritable,Text, Text, NullWritable>
{
    
    /**
     * 结果value
     */
    private Text key = null;

    private String siteCode = "";
    
    private String site="";
    
    private MultipleOutputs<Text, NullWritable> out;
    
    @Override
    protected void setup(Mapper<LongWritable, Text, Text, NullWritable>.Context context) throws IOException,
            InterruptedException
    {
        super.setup(context);
        Configuration conf = context.getConfiguration();
        siteCode = conf.get("siteCode");
        site = conf.get("site");
        out = new MultipleOutputs<Text,NullWritable>(context);
    }
    
    @Override
    protected void map(LongWritable offset, Text inputValue, Mapper<LongWritable, Text, Text, NullWritable>.Context context)
            throws IOException, InterruptedException
    {
        Map<String,String> eventMap=ServiceParser.searchBoxEvent(inputValue.toString(), site, siteCode);
        Map<String,String> resultMap=ServiceParser.mapperEmit(eventMap,MethodConstant.CLICK_CATEGORY_EVENT);
        if(StringUtils.isNotEmpty(resultMap) && resultMap.size()>0){
            String path=CommonUtil.outputPath(PathConstant.PATH_CLICK_CATEGORY_EVENT,eventMap.get(Constant.GLB_D),CommonUtil.getPartitionDate(eventMap.get(FieldConstant.TIME_LOCAL)),CommonUtil.getURLHostName(site).split("\\.")[0]);
            key=new Text(resultMap.get(Constant.KEY));
            out.write(key, NullWritable.get(),path);
        }
    }

    @Override
    protected void cleanup(Mapper<LongWritable, Text, Text, NullWritable>.Context context) throws IOException,
            InterruptedException
    {
        super.cleanup(context);
    }
}