package com.globalegrow.esearch.stat.event.mapred.stmtr.ods.clickcategory.server;

import java.util.HashMap;
import java.util.Map;

import com.globalegrow.esearch.constant.Constant;
import com.globalegrow.esearch.constant.FieldConstant;
import com.globalegrow.esearch.constant.UDCBConstant;
import com.globalegrow.esearch.util.CommonUtil;
import com.globalegrow.esearch.util.StringUtils;

/**
 * <pre>
 * 
 *  File: ClickCategoryEventService.java
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
public class ClickCategoryEventService
{
    public static Map<String,String> clickCategoryEvent(Map<String,String> eventMap)
    {
        //glb_t:ic glb_x:search 必须是执行的国家站点
        Map<String,String> resultMap=new HashMap<String,String>();
        if(UDCBConstant.GEARBEST_10002.equals(eventMap.get(Constant.GLB_D)) &&
                Constant.ie.equals(eventMap.get(Constant.GLB_T)) &&
                Constant.B.equals(eventMap.get(Constant.GLB_B)) &&
                StringUtils.isEmpty(eventMap.get(Constant.GLB_UBCTA)) && 
                StringUtils.isNotEmpty(eventMap.get(Constant.GLB_S)) &&
                CommonUtil.isGetStation(CommonUtil.getURLHostName(eventMap.get(Constant.SITE)),eventMap))
        {
            StringBuffer buffer=new StringBuffer();
            buffer.append(eventMap.get(Constant.GLB_T)).append(Constant.HIVE_SEPERATE)
            .append(eventMap.get(Constant.GLB_W)).append(Constant.HIVE_SEPERATE)
            .append(eventMap.get(Constant.GLB_TM)).append(Constant.HIVE_SEPERATE)
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

