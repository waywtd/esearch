package com.globalegrow.esearch.stat.event.mapred.weight.reducer;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import com.globalegrow.esearch.constant.Constant;
import com.globalegrow.esearch.stat.event.bean.SkuWeightBean;

/**
 * <pre>
 * 
 *  File: ExposureReducer.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2017年3月23日				lizhaohui				Initial.
 *
 * </pre>
 */
public class SkuWeightReducer extends Reducer<Text, Text, NullWritable, SkuWeightBean>
{
    
    /**
     * 输出对象
     */
    private MultipleOutputs<NullWritable, SkuWeightBean> out;
    
    private SkuWeightBean skuWeightBean;

    @Override
    protected void setup(Reducer<Text, Text, NullWritable, SkuWeightBean>.Context context) throws IOException,
            InterruptedException
    {
        super.setup(context);
        out = new MultipleOutputs<NullWritable, SkuWeightBean>(context);
    }
    
    @Override
    protected void reduce(Text key, Iterable<Text> values, Reducer<Text, Text, NullWritable, SkuWeightBean>.Context context)
            throws IOException, InterruptedException
    {
        long ubctie = 0l;
        long ubctic = 0l;
        long adt = 0l;
        long adf = 0l;
        for (Text val : values) 
        {
            String value = new String(val.getBytes(), Constant.UTF8);
            if (value.contains(Constant._UBCTIE)) 
            {
                ubctie++;
            }
            if (value.contains(Constant._UBCTIC)) 
            {
                ubctic++;
            }
            if (value.contains(Constant._UBCTD_ADT)) 
            {
                String[] amountArr=value.split(Constant.HIVE_SEPERATE);
                adt+=Long.parseLong(amountArr[4]);
            }
            if (value.contains(Constant._UBCTD_ADF)) 
            {
                adf++;
            }
        }
        String[] keyArr = key.toString().split(Constant.HIVE_SEPERATE);
        skuWeightBean=new SkuWeightBean(keyArr[0],keyArr[1],ubctic,adt,adf,ubctie,keyArr[3]);
        String partitionDate=getPartitionDate(keyArr[3]);
        out.write(NullWritable.get(), skuWeightBean, Constant.PATH_SKUWEIGHT + Constant.SEPERATE +"ubcd="+keyArr[2]+Constant.SEPERATE
                + partitionDate + Constant.SEPERATE );
    }

    @Override
    public void run(Reducer<Text, Text, NullWritable, SkuWeightBean>.Context arg0) throws IOException, InterruptedException
    {
        super.run(arg0);
    }
    
    private String getPartitionDate(String timeLocal){
        StringBuffer buffer=new StringBuffer();
        String[] timeLocals=timeLocal.split("-");
        buffer.append("year=").append(timeLocals[0]).append("/").append("month=").append(timeLocals[1]).append("/").append("day=").append(timeLocals[2]);
        return buffer.toString();
    }

    @Override
    protected void cleanup(Reducer<Text, Text, NullWritable, SkuWeightBean>.Context context) throws IOException,
            InterruptedException
    {
        super.cleanup(context);
        if(out!=null)
        {
            out.close();
        }
    }
}