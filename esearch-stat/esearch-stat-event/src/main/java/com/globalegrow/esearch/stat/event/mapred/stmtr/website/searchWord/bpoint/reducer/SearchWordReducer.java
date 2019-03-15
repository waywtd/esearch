package com.globalegrow.esearch.stat.event.mapred.stmtr.website.searchWord.bpoint.reducer;

import com.globalegrow.esearch.constant.Constant;
import com.globalegrow.esearch.constant.PathConstant;
import com.globalegrow.esearch.stat.event.bean.SearchNWordBean;
import com.globalegrow.esearch.util.CommonUtil;
import com.globalegrow.esearch.util.MD5Util;
import com.globalegrow.esearch.util.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Iterator;

/**
 * <pre>
 * 
 *  File: SearchWordReducer.java
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
public class SearchWordReducer extends Reducer<Text,Text,NullWritable,Text>
{
    
    /**
     * 输出对象
     */
    private MultipleOutputs<NullWritable, Text> out;
    
    @SuppressWarnings("unused")
    private SearchNWordBean searchNWordBean;
    
    private String siteCode = "";
    
    private String alias="";
    
    @Override
    public void setup(Reducer<Text, Text, NullWritable, Text>.Context context) throws IOException,
            InterruptedException
    {
        super.setup(context);
        Configuration conf = context.getConfiguration();
        siteCode = conf.get(Constant.SITE_CODE);
        alias = conf.get(Constant.ALIAS);
        out = new MultipleOutputs<NullWritable, Text>(context);
    }
    
    @Override
    public void reduce(Text key, Iterable<Text> values,Reducer<Text, Text, NullWritable, Text>.Context context) throws IOException, InterruptedException
    {
        String[] line=key.toString().split(Constant.HIVE_SEPERATE);
        if(StringUtils.isNotEmpty(key.toString()) && line.length>=4)
        {
            StringBuffer md5buffer=new StringBuffer();
            StringBuffer buffer=new StringBuffer();
            
            try
            {
                md5buffer.append(line[0]).append(Constant.MD5_SEPERATE).append(line[1]).append(Constant.MD5_SEPERATE).append(line[2]).append(Constant.MD5_SEPERATE).append(line[3]);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            Iterator<Text> iter=values.iterator();
            int ubamount=0;
            int searchTime=0;
            while(iter.hasNext())
            {
                String[] arr=iter.next().toString().split(Constant.HIVE_SEPERATE);
                ubamount=Integer.parseInt(arr[0]);
                buffer.append(arr[1]).append(Constant.HIVE_SEPERATE);
                searchTime++;
            }
            StringBuffer bufferResult=new StringBuffer();
            bufferResult.append(line[0].toLowerCase()).append(Constant.HIVE_SEPERATE)
                        .append(Integer.parseInt(line[1])).append(Constant.HIVE_SEPERATE)
                        .append(MD5Util.getMD5(md5buffer.toString())).append(Constant.HIVE_SEPERATE)
                        .append(searchTime).append(Constant.HIVE_SEPERATE)
                        .append(ubamount).append(Constant.HIVE_SEPERATE)
                        .append("www".equals(line[3])?"en":line[3]).append(Constant.HIVE_SEPERATE)
                        .append(line[2]).append(Constant.HIVE_SEPERATE)
                        .append(buffer.toString()).append(Constant.HIVE_SEPERATE);
            
            
            String path=CommonUtil.outputPath(MessageFormat.format(PathConstant.PATH_B_POINT_KEYWORD,alias),siteCode,CommonUtil.getPartitionDate(line[2]));
            out.write(NullWritable.get(), new Text(bufferResult.toString()),path);
        }
    }
}