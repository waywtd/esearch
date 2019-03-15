package com.globalegrow.esearch.stat.event.mapred.stmtr.website.loginInfo.apoint.reducer;

import java.io.IOException;
import java.text.MessageFormat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import com.globalegrow.esearch.constant.Constant;
import com.globalegrow.esearch.constant.PathConstant;
import com.globalegrow.esearch.util.CommonUtil;

/**
 * <pre>
 * 
 *  File: LoginInfoEventReducer.java
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
public class LoginInfoEventReducer extends Reducer<Text,Text,NullWritable,Text>
{
    
    /**
     * 输出对象
     */
    private MultipleOutputs<NullWritable, Text> out;
    
    private String siteCode = "";
    
    private String site="";
    
    private String alias="";
    
    @Override
    public void setup(Reducer<Text, Text, NullWritable, Text>.Context context) throws IOException,
            InterruptedException
    {
        Configuration conf = context.getConfiguration();
        siteCode = conf.get(Constant.SITE_CODE);
        site = conf.get(Constant.SITE);
        alias = conf.get(Constant.ALIAS);
        out = new MultipleOutputs<NullWritable, Text>(context);
    }
    
    @Override
    public void reduce(Text key, Iterable<Text> values, Reducer<Text, Text, NullWritable, Text>.Context context)
            throws IOException, InterruptedException
    {
        String[] line=key.toString().split(Constant.HIVE_SEPERATE);
        String path=CommonUtil.outputPath(MessageFormat.format(PathConstant.PATH_A_POINT_LOGINFO,alias),siteCode,CommonUtil.getPartitionDate(line[3]),CommonUtil.getURLHostName(site).split("\\.")[0]);
        out.write(NullWritable.get(),key,path);
    }
}