package com.globalegrow.esearch.stat.event.mapred.stmtr.ods.searchbox.service;

import com.globalegrow.esearch.constant.Constant;
import com.globalegrow.esearch.constant.FieldConstant;
import com.globalegrow.esearch.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * 
 *  File: SearchBoxEventService.java
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
public class SearchBoxEventService
{
    public static Map<String,String> searchBoxEvent(Map<String,String> eventMap)
    {
        //glb_t:ic glb_x:search 必须是执行的国家站点
        Map<String,String> resultMap=new HashMap<String,String>();
        if(StringUtils.isNotEmpty(eventMap.get(Constant.GLB_D)) &&
           Constant.ic.equals(eventMap.get(Constant.GLB_T)) &&
           Constant.search.equals(eventMap.get(Constant.GLB_X)))
        {
            StringBuffer buffer=new StringBuffer();
            buffer.append(eventMap.get(Constant.GLB_T)).append(Constant.HIVE_SEPERATE)
                  .append(eventMap.get(Constant.GLB_W)).append(Constant.HIVE_SEPERATE)
                  .append(eventMap.get(Constant.GLB_TM)).append(Constant.HIVE_SEPERATE)
                  .append(eventMap.get(Constant.GLB_SIWS)).append(Constant.HIVE_SEPERATE)
                  .append(eventMap.get(Constant.GLB_SK)).append(Constant.HIVE_SEPERATE)
                  .append(eventMap.get(Constant.GLB_SL)).append(Constant.HIVE_SEPERATE)
                  .append(eventMap.get(Constant.GLB_SC)).append(Constant.HIVE_SEPERATE)
                  .append(eventMap.get(Constant.GLB_X)).append(Constant.HIVE_SEPERATE)
                  .append(eventMap.get(Constant.GLB_OI)).append(Constant.HIVE_SEPERATE)
                  .append(eventMap.get(Constant.GLB_D)).append(Constant.HIVE_SEPERATE)
                  .append(eventMap.get(Constant.GLB_S)).append(Constant.HIVE_SEPERATE)
                  .append(eventMap.get(Constant.GLB_B)).append(Constant.HIVE_SEPERATE)
                  .append(eventMap.get(Constant.GLB_P)).append(Constant.HIVE_SEPERATE)
                  .append(eventMap.get(Constant.GLB_OLK)).append(Constant.HIVE_SEPERATE)
                  .append(eventMap.get(Constant.GLB_U)).append(Constant.HIVE_SEPERATE)
                  .append(eventMap.get(Constant.GLB_OD)).append(Constant.HIVE_SEPERATE)
                  .append(eventMap.get(Constant.GLB_OSR)).append(Constant.HIVE_SEPERATE)
                  .append(eventMap.get(Constant.GCLID)).append(Constant.HIVE_SEPERATE)
                  .append(eventMap.get(Constant.GLB_CL)==null?"":eventMap.get(Constant.GLB_CL)).append(Constant.HIVE_SEPERATE)
                  .append(eventMap.get(Constant.GLB_PL)).append(Constant.HIVE_SEPERATE)
                  .append(eventMap.get(Constant.GLB_SCKW)).append(Constant.HIVE_SEPERATE)
                  .append(eventMap.get(Constant.AT)==null?0:eventMap.get(Constant.AT)).append(Constant.HIVE_SEPERATE)
                  .append(eventMap.get(FieldConstant.TIME_LOCAL)).append(Constant.HIVE_SEPERATE);
            resultMap.put(Constant.KEY,buffer.toString());
        }
        return resultMap;
    }
}

