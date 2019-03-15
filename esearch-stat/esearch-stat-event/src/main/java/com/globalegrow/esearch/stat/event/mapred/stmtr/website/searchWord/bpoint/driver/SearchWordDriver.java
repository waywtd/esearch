package com.globalegrow.esearch.stat.event.mapred.stmtr.website.searchWord.bpoint.driver;

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
import com.globalegrow.esearch.stat.event.mapred.stmtr.website.searchWord.bpoint.mapper.SearchWordMapper;
import com.globalegrow.esearch.stat.event.mapred.stmtr.website.searchWord.bpoint.reducer.SearchWordReducer;

/**
 * <pre>
 * 
 *  File: Driver.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2017年12月5日				lizhaohui				Initial.
 *  Buried point
 *  /globalegrow/ngix-log/2017/09/17/10002 temp_search_10002 10002 gearbest 2017 12 09
 *  区分gearbest全球站国家站
 * </pre>
 */
public class SearchWordDriver extends Configured implements Tool
{
    public final Long MAP_SPLIT_SIZE = 256 * 1024 * 1024L;
    
    public static void main(String[] args)
    {
        try
        {
            ToolRunner.run(new SearchWordDriver(), args);
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
                    + "                    args[3]:Site alias \t\n"
                    + "                    args[4]:year partition hive \t\n"
                    + "                    args[5]:month partition hive \t\n"
                    + "                    args[6]:day partition hive \t\n"
                    + "                    args[7]:state abbreviations  \t\n");
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
        Job job = Job.getInstance(conf, "SearchWordDriver bPoint-"+args[2]);
        job.getConfiguration().set("mapreduce.input.fileinputformat.split.maxsize",MAP_SPLIT_SIZE.toString());
        
        Path out = new Path(args[1]);
        
        FileSystem fs = FileSystem.get(conf);
        // 删除中间文件
        fs.delete(out, true);
        
        job.setJarByClass(SearchWordDriver.class);
        job.setMapperClass(SearchWordMapper.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        FileInputFormat.setInputDirRecursive(job, true);
        FileInputFormat.addInputPaths(job, args[0]);
        FileInputFormat.setInputPathFilter(job, TmpFileFilter.class);
        TextInputFormat.setMinInputSplitSize(job, 1024*1024*256*2);
        job.setInputFormatClass(CombineTextInputFormat.class);
        FileOutputFormat.setOutputPath(job, out);
        
        job.setReducerClass(SearchWordReducer.class);
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);
        
        int runflag = job.waitForCompletion(true) ? 0 : 1;
        
       
        return runflag;
    }
}