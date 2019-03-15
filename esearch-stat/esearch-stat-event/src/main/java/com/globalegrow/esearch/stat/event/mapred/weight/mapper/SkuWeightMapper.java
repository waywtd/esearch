package com.globalegrow.esearch.stat.event.mapred.weight.mapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.alibaba.fastjson.JSONObject;
import com.globalegrow.esearch.constant.Constant;
import com.globalegrow.esearch.constant.FieldConstant;
import com.globalegrow.esearch.util.CommonUtil;
import com.globalegrow.esearch.util.DateUtil;
import com.globalegrow.esearch.util.JsonUtil;
import com.globalegrow.esearch.util.MapUtil;
import com.globalegrow.esearch.util.StringUtils;

/**
 * <pre>
 * 
 *  File: ExposureMapper.java
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
public class SkuWeightMapper extends Mapper<LongWritable, Text, Text, Text>
{

    /**
     * 结果value
     */
    private Text values = null;

    /**
     * key值
     */
    private Text word = null;
    
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
        word = new Text();
        values = new Text();
        try
        {
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
                        case Constant.UBCS:
                            data.put(Constant.UBCS, value);
                            break;
                        case Constant.UBCCK:
                            data.put(Constant.UBCCK, value);
                            break;
                        case Constant.UBCTA:
                            data.put(Constant.UBCTA, value);
                            break;
                        case Constant.UBCTD:
                            data.put(Constant.UBCTD, value);
                            break;
                        case Constant.UBCT:
                            data.put(Constant.UBCT, value);
                            break;
                        case Constant.PAGEMODULE:
                            data.put(Constant.PAGEMODULE, value);
                            break;
                        case Constant.SKUINFO:
                            data.put(Constant.SKUINFO, value);
                            break;
                        default:
                            break;
                    }
                }
                
                if(StringUtils.isNotEmpty(vWhCode))
                {
                    data.put(Constant.UBCCK, vWhCode);
                }
                
                    //曝光 浏览
               if((Constant.ic.equals(data.get(Constant.UBCT)) || Constant.ie.equals(data.get(Constant.UBCT)))
                             &&
                  (Constant.RCMD.equals(data.get(Constant.PAGEMODULE)) || Constant.PRUDUCT.equals(data.get(Constant.PAGEMODULE))))
                {
                    List<String> skuList = getSkuList(data);
                    StringBuffer buffer=null;
                    for (String sku : skuList)
                    {
                        buffer=new StringBuffer();
                        buffer.append(sku).append(Constant.HIVE_SEPERATE).append(data.get(Constant.UBCCK)).append(Constant.HIVE_SEPERATE).append(data.get(Constant.UBCD)).append(Constant.HIVE_SEPERATE).append(data.get(FieldConstant.TIME_LOCAL)).append(Constant.HIVE_SEPERATE);
                        word.set(buffer.toString());
                        values.set(buffer.toString() + "_ubct" + data.get(Constant.UBCT));
                        context.write(word, values);
                    }
                }
               //ubct=ic， ubctd=ADT/ADF，pagemodule=purchase 购物车或收藏夹
               if((Constant.ADT.equals(data.get(Constant.UBCTD)) || Constant.ADF.equals(data.get(Constant.UBCTD)))
                           &&
                   (Constant.ic.equals(data.get(Constant.UBCT))))
               {
                   Map<String,String> skuinfoMap=getSku(data.get(Constant.SKUINFO));
                   StringBuffer buffer=null;
                   if(StringUtils.isNotEmpty(skuinfoMap) && skuinfoMap.size()>0 && skuinfoMap.get("amount")!=null && skuinfoMap.get("sku")!=null)
                   {
                       buffer=new StringBuffer();
                       buffer.append(skuinfoMap.get("sku")).append(Constant.HIVE_SEPERATE).append(data.get(Constant.UBCCK)).append(Constant.HIVE_SEPERATE).append(data.get(Constant.UBCD)).append(Constant.HIVE_SEPERATE).append(data.get(FieldConstant.TIME_LOCAL)).append(Constant.HIVE_SEPERATE);
                       word.set(buffer.toString());
                       values.set(buffer.toString()+skuinfoMap.get("amount")+Constant.HIVE_SEPERATE+ Constant._UBCTD+data.get(Constant.UBCTD)); 
                       context.write(word, values);
                   }
               }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private List<String> getSkuList(Map<String,String> skuMap)
    {
        List<String> listSku=new ArrayList<String>();
        if(StringUtils.isNotEmpty(skuMap.get(Constant.UBCS)))
        {
            listSku.addAll(Arrays.asList(skuMap.get(Constant.UBCS).split(Constant.JOINER)));
        }
        else if (StringUtils.isNotEmpty(skuMap.get(Constant.UBCTA)))
        {
            listSku.addAll(Arrays.asList(skuMap.get(Constant.UBCTA).split(Constant.JOINER)));
        }else if(StringUtils.isNotEmpty(skuMap.get(Constant.SKUINFO)))
        {
            Map<String,String> skuinfoMap=getSku(skuMap.get(Constant.SKUINFO));
            if(skuinfoMap.size()>0)
            {
                listSku.add(skuinfoMap.get("sku"));
            }
        }
        return listSku;
    }
    
    private Map<String,String> getSku(String skuinfo)
    {
        Map<String,String> skuinfoMap=new HashMap<String,String>();
        if (JsonUtil.isJson(skuinfo))
        {
            JSONObject jsonObject = CommonUtil.getJSON(skuinfo);
            for (Entry<String, Object> entryValue : jsonObject.entrySet())
            {
                skuinfoMap.put("sku", entryValue.getKey().toString());
                JSONObject valueString = JSONObject.parseObject(entryValue.getValue().toString());
                for (Entry<String, Object> entry : valueString.entrySet())
                {
                    if (((String) entry.getKey()).toString().equals("amount"))
                    {
                        skuinfoMap.put("amount", entry.getValue().toString());
                    }
                    if (((String) entry.getKey()).toString().equals("category"))
                    {
                        skuinfoMap.put("category", entry.getValue().toString());
                    }
                }
            }
        }
        return skuinfoMap;
    }
    
    @Override
    protected void cleanup(Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException,
            InterruptedException
    {
        super.cleanup(context);
    }
}