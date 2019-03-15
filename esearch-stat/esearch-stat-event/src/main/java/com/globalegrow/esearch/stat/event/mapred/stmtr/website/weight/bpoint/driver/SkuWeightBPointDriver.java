package com.globalegrow.esearch.stat.event.mapred.stmtr.website.weight.bpoint.driver;

import com.globalegrow.esearch.constant.PathConstant;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.globalegrow.esearch.constant.Constant;
import com.globalegrow.esearch.stat.event.filter.TmpFileFilter;
import com.globalegrow.esearch.stat.event.mapred.stmtr.website.weight.bpoint.mapper.SkuWeightBPointMapper;
import com.globalegrow.esearch.stat.event.mapred.stmtr.website.weight.bpoint.reducer.SkuWeightBPointReducer;

import java.text.MessageFormat;

/**
 * <pre>
 * 
 *  File: SkuWeightDriver.java
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
public class SkuWeightBPointDriver extends Configured implements Tool
{

    public final Long MAP_SPLIT_SIZE = 128 * 1024L*1024L*5;
    
    public static void main(String[] args)
    {
        try
        {
            ToolRunner.run(new SkuWeightBPointDriver(), args);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public int run(String[] args) throws Exception
    {
        if(args.length<7)
        {
            System.out.println("Usage:\t\n args[0] hadoop inputPath\t\n "
                    + "                    args[1]:hadoop outputPath\t\n "
                    + "                    args[2]:siteCode \t\n "
                    + "                    args[3]:state abbreviations  \t\n"
                    + "                    args[4]:Site alias \t\n"
                    + "                    args[5]:year partition hive \t\n"
                    + "                    args[6]:month partition hive \t\n"
                    + "                    args[7]:day partition hive \t\n");
            System.exit(0);
        }
        
        Configuration conf = new Configuration();
        conf.set("mapred.job.queue.name", "daily");
        conf.set(Constant.SITE_CODE,args[2]);
        conf.set(Constant.ALIAS,args[3]);
        conf.set(Constant.YEAR,args[4]);
        conf.set(Constant.MONTH,args[5]);
        conf.set(Constant.DAY,args[6]);
        if (args.length == 8)
        {
            conf.set(Constant.SITE, args[7]);
        }
        Job job = Job.getInstance(conf, "SkuWeightDriver" + " bPoint-"+args[2]);
        job.getConfiguration().set("mapreduce.input.fileinputformat.split.maxsize",MAP_SPLIT_SIZE.toString());
        
        Path out = new Path(getOutPath(args));
        
        FileSystem fs = FileSystem.get(conf);
        // 删除中间文件
        fs.delete(out, true);
        
        job.setJarByClass(SkuWeightBPointDriver.class);
        job.setMapperClass(SkuWeightBPointMapper.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        FileInputFormat.setInputDirRecursive(job, true);
        FileInputFormat.addInputPaths(job, args[0]);
        FileInputFormat.setInputPathFilter(job, TmpFileFilter.class);
        TextInputFormat.setMinInputSplitSize(job, MAP_SPLIT_SIZE);
        job.setInputFormatClass(CombineTextInputFormat.class);
        FileOutputFormat.setOutputPath(job, out);
        
        job.setReducerClass(SkuWeightBPointReducer.class);
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);
        
        int runflag = job.waitForCompletion(true) ? 0 : 1;
        return runflag;
    }


    private String getOutPath(String[] args){
        StringBuffer path = new StringBuffer();
        path.append(MessageFormat.format(PathConstant.PATH_B_POINT_WEIGHT, args[3])).append(Constant.SEPERATE);
        path.append("ubcd=").append(args[2]).append(Constant.SEPERATE);
        path.append("year=").append(args[4]).append(Constant.SEPERATE);
        path.append("month=").append(args[5]).append(Constant.SEPERATE);
        path.append("day=").append(args[6]).append(Constant.SEPERATE);
        return path.toString();
    }
}

