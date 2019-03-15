package com.globalegrow.esearch.stat.event.mapred.stmtr.ods.checkout.server;

import java.util.HashMap;
import java.util.Map;

import com.globalegrow.esearch.constant.Constant;
import com.globalegrow.esearch.constant.FieldConstant;
import com.globalegrow.esearch.constant.UDCBConstant;
import com.globalegrow.esearch.util.StringUtils;

/**
 * <pre>
 * 
 *  File: UserLoginEventServer.java
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
public class UserLoginEventServer
{

    public static Map<String,String> userLoginEvent(Map<String,String> eventMap)
    {
        //glb_t:ic glb_x:search 必须是执行的国家站点
        Map<String,String> resultMap=new HashMap<String,String>();
        if(UDCBConstant.GEARBEST_10002.equals(eventMap.get(Constant.GLB_D)) &&
                StringUtils.isNotEmpty(eventMap.get(Constant.GLB_U)) && 
                StringUtils.isNotEmpty(eventMap.get(Constant.GLB_OI)))
        {
            StringBuffer buffer=new StringBuffer();
            buffer.append(eventMap.get(Constant.GLB_U)).append(Constant.HIVE_SEPERATE)
                  .append(eventMap.get(Constant.GLB_OD)).append(Constant.HIVE_SEPERATE)
                  .append(eventMap.get(FieldConstant.TIME_LOCAL)).append(Constant.HIVE_SEPERATE);
            resultMap.put(Constant.KEY,buffer.toString());
        }
        return resultMap;
    }
    
}