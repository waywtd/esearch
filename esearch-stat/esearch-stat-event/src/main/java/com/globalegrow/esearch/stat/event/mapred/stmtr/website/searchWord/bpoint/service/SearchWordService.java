package com.globalegrow.esearch.stat.event.mapred.stmtr.website.searchWord.bpoint.service;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.globalegrow.esearch.constant.Constant;
import com.globalegrow.esearch.constant.FieldConstant;
import com.globalegrow.esearch.util.BusinessUtil;
import com.globalegrow.esearch.util.StringUtils;

/**
 * <pre>
 * 
 *  File: SearchWordService.java
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
public class SearchWordService
{

    public static Map<String, String> bPoint(Map<String, String> eventMap)
    {
        Map<String, String> resultMap = new HashMap<String, String>();
        /**
         * 热搜词规则。
         * 1 点击事件
         * 2 搜索词sckw不能为空，且过滤掉特殊字符
         * 3 当前页不能为空，用来判断属于哪个国家站点。
         * 4 搜索出数量必须要大于0 at>0
         */
        if (Constant.ic.equals(eventMap.get(Constant.GLB_T))
                && Constant.search.equals(eventMap.get(Constant.GLB_X))
                && StringUtils.isNotEmpty(eventMap.get(Constant.GLB_SCKW))
                && StringUtils.isNotEmpty(eventMap.get(Constant.GLB_CL))
                && StringUtils.isNotEmpty(eventMap.get(Constant.AT))
                && StringUtils.validateStr(eventMap.get(Constant.GLB_SCKW))
                && Long.parseLong(eventMap.get(Constant.AT)) > 0
                && BusinessUtil.filterCountry(eventMap))
        {
            resultMap.putAll(coalesceResult(eventMap));
        }
        /**
         * 分类的sku 被点击之后才能区分出来。
         */
        else if (Constant.ic.equals(eventMap.get(Constant.GLB_T))
                && StringUtils.validateStr(eventMap.get(Constant.SCKW))
                && Constant.MP.equals(eventMap.get(Constant.GLB_PM))
                && StringUtils.isNotEmpty(eventMap.get(Constant.GLB_CL))
                && StringUtils.isNotEmpty(eventMap.get(Constant.PC))
                && StringUtils.isNotEmpty(eventMap.get(Constant.SCKW))
                && BusinessUtil.filterCountry(eventMap))
        {
            resultMap.putAll(coalesceResult(eventMap));
        }
        return resultMap;
    }

    private static Map<String, String> coalesceResult(Map<String, String> eventMap)
    {
        Map<String, String> resultMap = new HashMap<String, String>();
        StringBuffer buffer = new StringBuffer();
        StringBuffer bufferValue = new StringBuffer();
        buffer.append(eventMap.get(Constant.GLB_SCKW)==null?eventMap.get(Constant.SCKW):eventMap.get(Constant.GLB_SCKW)).append(Constant.HIVE_SEPERATE)
                .append(eventMap.get(Constant.PC) == null ? "0" : eventMap.get(Constant.PC)).append(Constant.HIVE_SEPERATE)
                .append(eventMap.get(FieldConstant.TIME_LOCAL)).append(Constant.HIVE_SEPERATE)
                .append(BusinessUtil.getCountry(eventMap));
        bufferValue.append(eventMap.get(Constant.AT)==null?"0":eventMap.get(Constant.AT)).append(Constant.HIVE_SEPERATE).append(JSONObject.toJSONString(eventMap));
        resultMap.put(Constant.KEY, buffer.toString());
        resultMap.put(Constant.VALUE, bufferValue.toString());
        return resultMap;
    }
    
    
}