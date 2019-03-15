package com.globalegrow.esearch.stat.event.mapred.stmtr.website.weight.bpoint.reducer;

import com.globalegrow.esearch.constant.Constant;
import com.globalegrow.esearch.constant.PathConstant;
import com.globalegrow.esearch.util.CommonUtil;
import com.globalegrow.esearch.util.MD5Util;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * <pre>
 * 
 *  File: SkuWeightReducer.java
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
public class SkuWeightBPointReducer extends Reducer<Text,Text,NullWritable,Text>
{
    
    private String siteCode = "";
    
    private String alias="";
    
    @Override
    protected void setup(Reducer<Text, Text, NullWritable, Text>.Context context) throws IOException,
            InterruptedException
    {
        Configuration conf = context.getConfiguration();
        siteCode = conf.get(Constant.SITE_CODE);
        alias = conf.get(Constant.ALIAS);
    }
    
    @Override
    protected void reduce(Text key, Iterable<Text> values,
            Reducer<Text, Text, NullWritable, Text>.Context context) throws IOException, InterruptedException
    {
        String[] keyArr = key.toString().split(Constant.HIVE_SEPERATE);
        if(keyArr.length>=3){
            long ubctie = 0l;
            long ubctic = 0l;
            long adt = 0l;
            long adf = 0l;
            for (Text val : values)
            {
                String value = new String(val.getBytes(), Constant.UTF8);
                String[] valueArr=value.split(Constant.HIVE_SEPERATE);
                if ("_ubctie".equals(valueArr[4]))
                {
                    ubctie++;
                }
                if ("_ubctic".equals(valueArr[4]))
                {
                    ubctic++;
                }
                if (Constant._UBCTD_ADT.equals(valueArr[4]))
                {
                    if(CommonUtil.isNumeric(valueArr[3]))
                    {
                        adt+=Long.parseLong(valueArr[3]);
                    }
                }
                if (Constant._UBCTD_ADF.equals(valueArr[4]))
                {
                    adf++;
                }
            }
            StringBuffer buffer=new StringBuffer();
            buffer.append(keyArr[0]).append(Constant.MD5_SEPERATE).append(keyArr[1]).append(Constant.MD5_SEPERATE).append(keyArr[2]);
            StringBuffer resultBuffer=new StringBuffer();
            resultBuffer.append(MD5Util.getMD5(buffer.toString())).append(Constant.HIVE_SEPERATE)
                    .append(keyArr[0]).append(Constant.HIVE_SEPERATE)
                    .append(ubctic).append(Constant.HIVE_SEPERATE)
                    .append(adt).append(Constant.HIVE_SEPERATE)
                    .append(adf).append(Constant.HIVE_SEPERATE)
                    .append(ubctie).append(Constant.HIVE_SEPERATE)
                    .append("www".equals(keyArr[2])?"en":keyArr[2]).append(Constant.HIVE_SEPERATE)
                    .append(keyArr[1]);
            String path=CommonUtil.outputPath(MessageFormat.format(PathConstant.PATH_B_POINT_WEIGHT,alias),siteCode,CommonUtil.getPartitionDate(keyArr[1]));
            context.write(NullWritable.get(),new Text(resultBuffer.toString()));
        }else{
            System.out.println(key.toString());
        }
    }
}