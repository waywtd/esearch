package com.globalegrow.esearch.stat.event.mapred.stmtr.ods.clicksku.service;

import com.globalegrow.esearch.constant.Constant;
import com.globalegrow.esearch.constant.FieldConstant;
import com.globalegrow.esearch.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * 
 *  File: ClickSkuEventService.java
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
public class ClickSkuEventService
{
    public static Map<String,String> clickSkuEvent(Map<String,String> eventMap)
    {
        //glb_t:ic glb_x:search 必须是执行的国家站点
        Map<String,String> resultMap=new HashMap<String,String>();
        if(StringUtils.isNotEmpty(eventMap.get(Constant.GLB_D)) &&
                Constant.ic.equals(eventMap.get(Constant.GLB_T)) &&
                Constant.MP.equals(eventMap.get(Constant.GLB_PM)) &&
                StringUtils.isNotEmpty(eventMap.get(Constant.SCKW)) &&
                StringUtils.isNotEmpty(eventMap.get(Constant.SKU)))
        {
            StringBuffer buffer=new StringBuffer();
            buffer.append(eventMap.get(Constant.GLB_T)).append(Constant.HIVE_SEPERATE)
            .append(eventMap.get(Constant.GLB_W)).append(Constant.HIVE_SEPERATE)
            .append(eventMap.get(Constant.GLB_TM)).append(Constant.HIVE_SEPERATE)
            .append(eventMap.get(Constant.GLB_PM)).append(Constant.HIVE_SEPERATE)
            .append(eventMap.get(Constant.SKU)).append(Constant.HIVE_SEPERATE)
            .append(eventMap.get(Constant.PAM)).append(Constant.HIVE_SEPERATE)
            .append(eventMap.get(Constant.PC)).append(Constant.HIVE_SEPERATE)
            .append(eventMap.get(Constant.K)).append(Constant.HIVE_SEPERATE)
            .append(eventMap.get(Constant.RANK)).append(Constant.HIVE_SEPERATE)
            .append(eventMap.get(Constant.SCKW)).append(Constant.HIVE_SEPERATE)
            .append(eventMap.get(Constant.GLB_X)).append(Constant.HIVE_SEPERATE)
            .append(eventMap.get(Constant.VIEW)).append(Constant.HIVE_SEPERATE)
            .append(eventMap.get(Constant.PAGE)).append(Constant.HIVE_SEPERATE)
            .append(eventMap.get(Constant.SORT)).append(Constant.HIVE_SEPERATE)
            .append(eventMap.get(Constant.GLB_SC)).append(Constant.HIVE_SEPERATE)
            .append(eventMap.get(Constant.GLB_OI)).append(Constant.HIVE_SEPERATE)
            .append(eventMap.get(Constant.GLB_D)).append(Constant.HIVE_SEPERATE)
            .append(eventMap.get(Constant.GLB_S)).append(Constant.HIVE_SEPERATE)
            .append(eventMap.get(Constant.GLB_B)).append(Constant.HIVE_SEPERATE)
            .append(eventMap.get(Constant.GLB_U)).append(Constant.HIVE_SEPERATE)
            .append(eventMap.get(Constant.GLB_OD)).append(Constant.HIVE_SEPERATE)
            .append(eventMap.get(Constant.GLB_OSR)).append(Constant.HIVE_SEPERATE)
            .append(eventMap.get(Constant.GCLID)).append(Constant.HIVE_SEPERATE)
            .append(eventMap.get(Constant.GLB_CL)==null?"":eventMap.get(Constant.GLB_CL)).append(Constant.HIVE_SEPERATE)
            .append(eventMap.get(Constant.GLB_PL)).append(Constant.HIVE_SEPERATE)
            .append(eventMap.get(FieldConstant.TIME_LOCAL)).append(Constant.HIVE_SEPERATE);
            resultMap.put(Constant.KEY,buffer.toString());
        }
        return resultMap;
    }
    
}