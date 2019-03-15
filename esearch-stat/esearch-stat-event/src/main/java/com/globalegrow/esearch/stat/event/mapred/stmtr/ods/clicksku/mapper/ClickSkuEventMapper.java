package com.globalegrow.esearch.stat.event.mapred.stmtr.ods.clicksku.mapper;

import com.globalegrow.esearch.constant.Constant;
import com.globalegrow.esearch.constant.MethodConstant;
import com.globalegrow.esearch.constant.PathConstant;
import com.globalegrow.esearch.stat.event.services.ServiceParser;
import com.globalegrow.esearch.util.CommonUtil;
import com.globalegrow.esearch.util.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;

/**
 * <pre>
 * 
 *  File: ClickSkuMapper.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2017年9月16日				lizhaohui				Initial.
 *
 * </pre>
 */
public class ClickSkuEventMapper extends Mapper<LongWritable,Text, Text, NullWritable>
{


    /**
     * 结果value
     */
    private Text key = null;

    private String siteCode = "";
    
    private String alias="";

    private StringBuffer buffer = null;
    
    /*private MultipleOutputs<Text, NullWritable> out;*/
    
    @Override
    protected void setup(Mapper<LongWritable, Text, Text, NullWritable>.Context context) throws IOException,
            InterruptedException
    {
        super.setup(context);
        Configuration conf = context.getConfiguration();
        siteCode = conf.get("siteCode");
        alias = conf.get("alias");
        buffer=new StringBuffer();
        /*buffer.append(conf.get(Constant.YEAR)).append(Constant.IN_THE_LINE).append(conf.get(Constant.MONTH)).append(Constant.IN_THE_LINE).append(conf.get(Constant.DAY));
        out = new MultipleOutputs<Text,NullWritable>(context);*/
    }
    
    @Override
    protected void map(LongWritable offset, Text inputValue, Mapper<LongWritable, Text, Text, NullWritable>.Context context)
            throws IOException, InterruptedException
    {

        Map<String,String> eventMap=ServiceParser.searchBoxEvent02(inputValue.toString() ,siteCode,buffer.toString());
        Map<String,String> resultMap=ServiceParser.mapperEmit(eventMap,MethodConstant.CLICK_SKU_EVENT);
        if(StringUtils.isNotEmpty(resultMap) && resultMap.size()>0){
            key=new Text(resultMap.get(Constant.KEY));
            /*String path=CommonUtil.outputPath(MessageFormat.format(PathConstant.PATH_CLICK_SKU_EVENT,alias),siteCode,CommonUtil.getPartitionDate(buffer.toString()));
            out.write(key, NullWritable.get(),path);*/
            context.write(key, NullWritable.get());
        }
    }
    
    @Override
    protected void cleanup(Mapper<LongWritable, Text, Text, NullWritable>.Context context) throws IOException,
            InterruptedException
    {
        super.cleanup(context);
    }
}