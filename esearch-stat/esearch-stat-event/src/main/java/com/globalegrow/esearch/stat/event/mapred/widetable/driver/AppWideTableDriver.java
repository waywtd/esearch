package com.globalegrow.esearch.stat.event.mapred.widetable.driver;

import com.globalegrow.esearch.stat.event.mapred.widetable.mapper.AppWideTableMapper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppWideTableDriver extends Configured implements Tool {

    final Long MAP_SPLIT_SIZE = 256 * 1024 * 1024L*2;
    private static Logger log = LogManager.getLogger(AppWideTableDriver.class);

    public static void main(String[] args) throws Exception {
        ToolRunner.run(new AppWideTableDriver(), args);
    }

    @Override
    public int run(String[] args) throws Exception {
        if (args == null || args.length < 4) {
            log.error("ERROR missing parameter:" + args);
            throw new IllegalArgumentException("missing parameter!");
        }

        String inputPath = args[1];
        String outputPath = args[2];
        String queue = args[3];

        Configuration conf = new Configuration();
        conf.set("mapred.job.queue.name", queue);
        Job job = Job.getInstance(conf,"app-wide-table-" + args[0] + "-job");
        job.getConfiguration().set("mapreduce.input.fileinputformat.split.maxsize",MAP_SPLIT_SIZE.toString());

        Path out = new Path(outputPath);
        FileSystem fileSystem = FileSystem.get(conf);
        fileSystem.delete(out, true);

        job.setJarByClass(AppWideTableDriver.class);
        job.setMapperClass(AppWideTableMapper.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);
        job.setNumReduceTasks(0);

        FileInputFormat.setInputDirRecursive(job, true);
        FileInputFormat.addInputPaths(job, inputPath);
        TextInputFormat.setMinInputSplitSize(job, MAP_SPLIT_SIZE);
        FileOutputFormat.setOutputPath(job, out);

        int runFlag = job.waitForCompletion(true) ? 0 : 1;

        return runFlag;
    }

}
