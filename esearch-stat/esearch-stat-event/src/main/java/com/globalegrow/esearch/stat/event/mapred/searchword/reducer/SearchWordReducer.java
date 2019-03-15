package com.globalegrow.esearch.stat.event.mapred.searchword.reducer;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import com.globalegrow.esearch.constant.Constant;
import com.globalegrow.esearch.stat.event.bean.SearchWordBean;
import com.globalegrow.esearch.util.MD5Util;
import com.globalegrow.esearch.util.StringUtils;


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
 *  2017年4月1日				lizhaohui				Initial.
 *
 * </pre>
 */
public class SearchWordReducer extends Reducer<Text,Text,NullWritable,SearchWordBean>
{

    /**
     * 输出对象
     */
    private MultipleOutputs<NullWritable, SearchWordBean> out;
    
    private SearchWordBean searchWordBean;
    
    @Override
    protected void setup(Reducer<Text, Text, NullWritable, SearchWordBean>.Context context) throws IOException,
            InterruptedException
    {
        super.setup(context);
        out = new MultipleOutputs<NullWritable, SearchWordBean>(context);
    }
    
    @Override
    protected void reduce(Text key, Iterable<Text> values, Reducer<Text, Text, NullWritable, SearchWordBean>.Context context)
            throws IOException, InterruptedException
    {
        if(StringUtils.isNotEmpty(key.toString()))
        {
            String[] line=key.toString().split(Constant.HIVE_SEPERATE);
            searchWordBean=new SearchWordBean();
            searchWordBean.setSearchWord(line[4]);
            searchWordBean.setMd5(MD5Util.getMD5(line[4]));
            searchWordBean.setDateTime(line[5]);
            searchWordBean.setvWhCode(line[6]);
            Iterator<Text> iter=values.iterator();
            int ubamount=0;
            int searchTime=0;
            while(iter.hasNext())
            {
                ubamount=Integer.parseInt(iter.next().toString());
                searchTime++;
            }
            searchWordBean.setUbamount(ubamount);
            searchWordBean.setSearchTimes(searchTime);
            String partitionDate=getPartitionDate(line[5]);
            out.write(NullWritable.get(), searchWordBean, Constant.PATH_KEYWORD + Constant.SEPERATE+"ubcd="+line[0]+Constant.SEPERATE
                    + partitionDate + Constant.SEPERATE );
        }
    }

    @Override
    public void run(Reducer<Text, Text, NullWritable, SearchWordBean>.Context context) throws IOException,
            InterruptedException
    {
        super.run(context);
    }

    @Override
    protected void cleanup(Reducer<Text, Text, NullWritable, SearchWordBean>.Context context) throws IOException,
            InterruptedException
    {
        super.cleanup(context);
        if(out!=null)
        {
            out.close();
        }
    }
    
    private String getPartitionDate(String timeLocal){
        StringBuffer buffer=new StringBuffer();
        String[] timeLocals=timeLocal.split("-");
        buffer.append("year=").append(timeLocals[0]).append("/").append("month=").append(timeLocals[1]).append("/").append("day=").append(timeLocals[2]);
        return buffer.toString();
    }
}