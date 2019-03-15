package com.globalegrow.esearch.stat.event.mapred.stmtr.website.weight.bpoint.service;

import com.alibaba.fastjson.JSONObject;
import com.globalegrow.esearch.constant.Constant;
import com.globalegrow.esearch.constant.FieldConstant;
import com.globalegrow.esearch.util.BusinessUtil;
import com.globalegrow.esearch.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 
 *  File: SkuWeightService.java
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
public class SkuWeightBPointService
{
    public static Map<String, String> bPoint(Map<String, String> eventMap)
    {
        Map<String,String> result=new HashMap<String,String>();
        Map<String, String> resultMap = null;
        List<Map<String,String>> listMap=new ArrayList<Map<String,String>>();
        if((Constant.ic.equals(eventMap.get(Constant.GLB_T)) || Constant.ie.equals(eventMap.get(Constant.GLB_T))) &&
          (Constant.MP.equals(eventMap.get(Constant.GLB_PM)) || Constant.MR.equals(eventMap.get(Constant.GLB_PM)))){
            List<String> skuList=BusinessUtil.getSkuListEvent(eventMap);
            StringBuffer buffer=null;
            StringBuffer bufferValue=null;
            for(String sku:skuList){
                resultMap=new HashMap<String, String>();
                buffer=new StringBuffer();
                bufferValue=new StringBuffer();
                buffer.append(sku).append(Constant.HIVE_SEPERATE)
                      .append(eventMap.get(FieldConstant.TIME_LOCAL)).append(Constant.HIVE_SEPERATE)
                      .append(BusinessUtil.getCountry(eventMap)).append(Constant.HIVE_SEPERATE);
                bufferValue.append(buffer.toString()).append("0").append(Constant.HIVE_SEPERATE)
                           .append("_ubct").append(eventMap.get(Constant.GLB_T));
                resultMap.put(Constant.KEY,buffer.toString());
                resultMap.put(Constant.VALUE,bufferValue.toString());
                listMap.add(resultMap);
            }
            result.put(Constant.RESULT, JSONObject.toJSONString(listMap));
        }else if((Constant.ADT.equals(eventMap.get(Constant.GLB_X)) || Constant.ADF.equals(eventMap.get(Constant.GLB_X))) &&
                StringUtils.isNotEmpty(eventMap.get(Constant.PAM))){
            resultMap=new HashMap<String, String>();
            StringBuffer buffer = new StringBuffer();
            StringBuffer bufferValue = new StringBuffer();
            buffer.append(eventMap.get(Constant.SKU)).append(Constant.HIVE_SEPERATE)
                  .append(eventMap.get(FieldConstant.TIME_LOCAL)).append(Constant.HIVE_SEPERATE)
                  .append(BusinessUtil.getCountry(eventMap)).append(Constant.HIVE_SEPERATE);
            bufferValue.append(buffer.toString()).append(eventMap.get(Constant.PAM))
                       .append(Constant.HIVE_SEPERATE).append(Constant._UBCTD).append(eventMap.get(Constant.GLB_X));
            resultMap.put(Constant.KEY,buffer.toString());
            resultMap.put(Constant.VALUE,bufferValue.toString());
            listMap.add(resultMap);
            result.put(Constant.RESULT, JSONObject.toJSONString(listMap));   
        }
        return result;
    }
}