package com.globalegrow.esearch.stat.event.mapred.stmtr.ods.clicksku.driver;

import java.text.MessageFormat;

import com.globalegrow.esearch.constant.Constant;
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

import com.globalegrow.esearch.stat.event.mapred.stmtr.ods.clicksku.mapper.ClickSkuEventMapper;
import com.globalegrow.esearch.util.CommonUtil;

/**
 * <pre>
 * 
 *  File: ClickSkuDriver.java
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
public class ClickSkuEventDriver extends Configured implements Tool
{
    
    final Long MAP_SPLIT_SIZE = 256 * 1024 * 1024L;
    
    
    public static void main(String[] args)
    {
        try
        {
            ToolRunner.run(new ClickSkuEventDriver(), args);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    

    @Override
    public int run(String[] args) throws Exception
    {
        if(args.length<6)
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
        conf.set(Constant.SITE_CODE,args[1]);
        conf.set(Constant.ALIAS,args[2]);
        conf.set(Constant.YEAR,args[3]);
        conf.set(Constant.MONTH,args[4]);
        conf.set(Constant.DAY,args[5]);
        Job job = Job.getInstance(conf, "ClickSkuEventMapper-"+args[1]);
        job.getConfiguration().set("mapreduce.input.fileinputformat.split.maxsize",MAP_SPLIT_SIZE.toString());
        
        Path out = new Path(getOutPath(args));
        
        FileSystem fs = FileSystem.get(conf);
        // 删除中间文件
        fs.delete(out, true);
        
        job.setJarByClass(ClickSkuEventDriver.class);
        job.setMapperClass(ClickSkuEventMapper.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);
        FileInputFormat.setInputDirRecursive(job, true);
        FileInputFormat.addInputPaths(job, args[0]);
        TextInputFormat.setMinInputSplitSize(job, 1024*1024*256*2);
        job.setInputFormatClass(CombineTextInputFormat.class);
        
        FileOutputFormat.setOutputPath(job, out);
        
        int runflag = job.waitForCompletion(true) ? 0 : 1;
        
       
        return runflag;
    }
    
    private String getOutPath(String[] args){
        StringBuffer buffer=new StringBuffer();
        buffer.append(args[3]).append(Constant.IN_THE_LINE).append(args[4]).append(Constant.IN_THE_LINE).append(args[5]);
        String path=CommonUtil.outputPath(MessageFormat.format(PathConstant.PATH_CLICK_SKU_EVENT,args[2]),args[1],CommonUtil.getPartitionDate(buffer.toString()));
        return path.toString();
    }

}

