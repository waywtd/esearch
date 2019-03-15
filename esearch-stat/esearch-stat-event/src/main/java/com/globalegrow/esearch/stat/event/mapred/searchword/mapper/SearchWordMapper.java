package com.globalegrow.esearch.stat.event.mapred.searchword.mapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.globalegrow.esearch.constant.Constant;
import com.globalegrow.esearch.constant.FieldConstant;
import com.globalegrow.esearch.util.CommonUtil;
import com.globalegrow.esearch.util.DateUtil;
import com.globalegrow.esearch.util.MapUtil;
import com.globalegrow.esearch.util.StringUtils;

/**
 * <pre>
 * 
 *  File: SearchWordMapper.java
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
public class SearchWordMapper extends Mapper<LongWritable, Text, Text, Text>
{

    /**
     * 结果value
     */
    private Text values = null;

    /**
     * key值
     */
    private Text keyValue = null;
    
    private String vWhCode=null;
    
    @Override
    protected void setup(Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException,
            InterruptedException
    {
        super.setup(context);
        Configuration conf = context.getConfiguration();
        vWhCode=conf.get("v_wh_code");
    }
    
    @Override
    protected void map(LongWritable offset, Text inputValue, Mapper<LongWritable, Text, Text, Text>.Context context)
            throws IOException, InterruptedException
    {
        keyValue = new Text();
        values = new Text();
        String string = new String(inputValue.getBytes(), Constant.UTF8);
        if (string.contains(Constant.STATUESUCCEED) && (!string.contains(Constant.ICON)))
        {
            Map<String,String> data=new HashMap<String,String>();
            Map<String, String> eventmap = new LinkedHashMap<String, String>();
            String line=new String(inputValue.toString().getBytes(),Constant.UTF8);
            MapUtil.getEventmap(line, eventmap);
            data.put(FieldConstant.TIME_LOCAL, DateUtil.convertDate(eventmap.get(FieldConstant.TIME_LOCAL),"yyyy-MM-dd"));
            String event = CommonUtil.getData(line);
            Matcher m = CommonUtil.getMatcher(event, Constant.REGUBC);
            while (m.find())
            {
                String key = m.group(1);
                String value = m.group(2);
                switch (key)
                {
                    case Constant.UBCD:
                        data.put(Constant.UBCD, value);
                        break;
                    case Constant.UBCT:
                        data.put(Constant.UBCT, value);
                        break;
                    case Constant.SPCS:
                        data.put(Constant.SPCS, value);
                        break;
                    case Constant.SPCB:
                        data.put(Constant.SPCB, value);
                        break;
                    case Constant.PAGEMODULE:
                        data.put(Constant.PAGEMODULE, value);
                        break;
                    case Constant.UBCCK:
                        data.put(Constant.UBCCK, value);
                        break;
                    case Constant.UBCKW:
                        String valueStr = value;
                        if (StringUtils.validateStr(valueStr.toLowerCase()))
                        {
                            data.put(Constant.UBCKW, valueStr.toLowerCase());
                        }
                        break;
                    case Constant.UBAMOUNT:
                        data.put(Constant.UBAMOUNT, value);
                        break;
                    default:
                        break;
                }
            }
            
            if(StringUtils.isNotEmpty(vWhCode))
            {
                data.put(Constant.UBCCK, vWhCode);
            }
            
            if(Constant.ic.equals(data.get(Constant.UBCT)) && Constant.SPCS_B002.equals(data.get(Constant.SPCS))
                    &&
               Constant.SPCB_B.equals(data.get(Constant.SPCB)) && Constant.PRUDUCT.equals(data.get(Constant.PAGEMODULE)) 
                   && 
              StringUtils.isNotEmpty(data.get(Constant.UBCKW)) && StringUtils.validateStr(data.get(Constant.UBCKW)))
            {
                StringBuffer buffer=new StringBuffer();
                buffer.append(data.get(Constant.UBCD)).append(Constant.HIVE_SEPERATE).append(data.get(Constant.SPCS))
                      .append(Constant.HIVE_SEPERATE).append(data.get(Constant.SPCB)).append(Constant.HIVE_SEPERATE)
                      .append(data.get(Constant.PAGEMODULE)).append(Constant.HIVE_SEPERATE).append(data.get(Constant.UBCKW))
                      .append(Constant.HIVE_SEPERATE).append(data.get(FieldConstant.TIME_LOCAL)).append(Constant.HIVE_SEPERATE).append(data.get(Constant.UBCCK));
                keyValue.set(buffer.toString());
                values.set(data.get(Constant.UBAMOUNT));
                context.write(keyValue, values);
            }
        }
    }

    @Override
    public void run(Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException, InterruptedException
    {
        super.run(context);
    }

    @Override
    protected void cleanup(Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException,
            InterruptedException
    {
        super.cleanup(context);
    }
}