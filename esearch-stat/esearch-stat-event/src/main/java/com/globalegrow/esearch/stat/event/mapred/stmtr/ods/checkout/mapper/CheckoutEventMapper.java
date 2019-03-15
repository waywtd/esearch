package com.globalegrow.esearch.stat.event.mapred.stmtr.ods.checkout.mapper;

import com.globalegrow.esearch.constant.Constant;
import com.globalegrow.esearch.constant.MethodConstant;
import com.globalegrow.esearch.constant.PathConstant;
import com.globalegrow.esearch.stat.event.mapred.stmtr.ods.checkout.server.CheckoutEventServer;
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
import java.util.List;
import java.util.Map;

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
public class CheckoutEventMapper extends Mapper<LongWritable,Text, Text, NullWritable>
{
    
    /**
     * 结果value
     */
    private Text key = null;

    private String siteCode = "";
    
    private String alias="";
    
    private StringBuilder builder=null;
    
    private MultipleOutputs<Text, NullWritable> out;
    
    @Override
    protected void setup(Mapper<LongWritable, Text, Text, NullWritable>.Context context) throws IOException,
            InterruptedException
    {
        super.setup(context);
        Configuration conf = context.getConfiguration();
        siteCode = conf.get(Constant.SITE_CODE);
        alias = conf.get(Constant.ALIAS);
        builder=new StringBuilder();
        builder.append(conf.get(Constant.YEAR)).append(Constant.IN_THE_LINE).append(conf.get(Constant.MONTH)).append(Constant.IN_THE_LINE).append(conf.get(Constant.DAY));
        out = new MultipleOutputs<Text,NullWritable>(context);
    }
    
    @Override
    protected void map(LongWritable offset, Text inputValue, Mapper<LongWritable, Text, Text, NullWritable>.Context context)
            throws IOException, InterruptedException
    {
                Map<String,String> eventMap=ServiceParser.searchBoxEvent02(inputValue.toString(), siteCode,builder.toString());
                Map<String,String> resultMap=ServiceParser.mapperEmit(eventMap,MethodConstant.CHECK_OUT_EVENT);
                if(StringUtils.isNotEmpty(resultMap) && resultMap.size()>0){
                    String output=CommonUtil.outputPath(MessageFormat.format(PathConstant.PATH_CHECK_OUT_EVENT,alias),siteCode,CommonUtil.getPartitionDate(builder.toString()));
                    List<String> listKey=CheckoutEventServer.loopList(resultMap.get(Constant.KEY));
                    for (int i = 0; i < listKey.size(); i++)
                    {
                        key = new Text(listKey.get(i));
                        out.write(key, NullWritable.get(), output);
                    }
        }
    }

    @Override
    protected void cleanup(Mapper<LongWritable, Text, Text, NullWritable>.Context context) throws IOException,
            InterruptedException
    {
        super.cleanup(context);
    }
}