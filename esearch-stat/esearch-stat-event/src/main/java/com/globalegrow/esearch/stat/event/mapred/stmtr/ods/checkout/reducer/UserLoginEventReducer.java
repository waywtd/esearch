package com.globalegrow.esearch.stat.event.mapred.stmtr.ods.checkout.reducer;

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
 *  File: UserLoginEventReducer.java
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
public class UserLoginEventReducer extends Reducer<Text, NullWritable, Text, NullWritable>
{

    /**
     * 输出对象
     */
    private MultipleOutputs<Text,NullWritable> out;
    
    private String siteCode = "";
    
    private String site="";
    
    private String alias="";
    
    
    
    @Override
    public void setup(Reducer<Text, NullWritable, Text, NullWritable>.Context context) throws IOException,
            InterruptedException
    {
        super.setup(context);
        Configuration conf = context.getConfiguration();
        siteCode = conf.get(Constant.SITE_CODE);
        site = conf.get(Constant.SITE);
        alias = conf.get(Constant.ALIAS);
        out = new MultipleOutputs<Text,NullWritable>(context);
    }



    @Override
    public void reduce(Text key, Iterable<NullWritable> values,Reducer<Text, NullWritable, Text, NullWritable>.Context context) throws IOException, InterruptedException
    {
        /*
        .append(eventMap.get(Constant.GLB_U)).append(Constant.HIVE_SEPERATE)
        .append(eventMap.get(Constant.GLB_OI)).append(Constant.HIVE_SEPERATE)
        .append(eventMap.get(FieldConstant.TIME_LOCAL)).append(Constant.HIVE_SEPERATE);*/
        String[] keyArr = key.toString().split(Constant.HIVE_SEPERATE);
        String path=CommonUtil.outputPath(MessageFormat.format(PathConstant.PATH_USER_LOGIN_EVENT,alias),siteCode,CommonUtil.getPartitionDate(keyArr[2]),CommonUtil.getURLHostName(site).split("\\.")[0]);
        out.write(key, NullWritable.get(),path);
    }
}