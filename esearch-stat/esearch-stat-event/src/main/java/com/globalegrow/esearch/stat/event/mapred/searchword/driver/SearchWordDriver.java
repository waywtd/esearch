package com.globalegrow.esearch.stat.event.mapred.searchword.driver;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.globalegrow.esearch.stat.event.mapred.searchword.mapper.SearchWordMapper;
import com.globalegrow.esearch.stat.event.mapred.searchword.reducer.SearchWordReducer;

/**
 * <pre>
 * 
 *  File: SearchWordDriver.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2017年4月1日				lizhaohui				Initial.
 *
 * </pre>
 */
public class SearchWordDriver extends Configured implements Tool
{

    final Long MAP_SPLIT_SIZE = 256 * 1024 * 1024L;
    
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
        Configuration conf = new Configuration();
        conf.set("mapred.job.queue.name", "daily");
        if(args.length>2)
        {
            conf.set("v_wh_code",args[2]);
        }
        Job job = Job.getInstance(conf, "SearchWordDriver");
        job.getConfiguration().set("mapreduce.input.fileinputformat.split.maxsize",MAP_SPLIT_SIZE.toString());

        Path out = new Path(args[1]);
        job.setJarByClass(SearchWordDriver.class);
        job.setMapperClass(SearchWordMapper.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        FileInputFormat.setInputDirRecursive(job, true);
        FileInputFormat.addInputPaths(job, args[0]);
        TextInputFormat.setMinInputSplitSize(job, 1024*1024*256*2);
        job.setInputFormatClass(CombineTextInputFormat.class);
        job.setNumReduceTasks(5);
        
        job.setReducerClass(SearchWordReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileOutputFormat.setOutputPath(job, out);
        
        int runflag = job.waitForCompletion(true) ? 0 : 1;
        FileSystem fs = FileSystem.get(conf);
        // 删除中间文件
        fs.delete(out, true);
        return runflag;
    }

}