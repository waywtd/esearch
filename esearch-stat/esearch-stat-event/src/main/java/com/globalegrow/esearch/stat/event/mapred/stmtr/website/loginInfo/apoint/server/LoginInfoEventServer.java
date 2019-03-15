package com.globalegrow.esearch.stat.event.mapred.stmtr.website.loginInfo.apoint.server;

import java.util.HashMap;
import java.util.Map;

import com.globalegrow.esearch.constant.Constant;
import com.globalegrow.esearch.constant.FieldConstant;
import com.globalegrow.esearch.constant.UDCBConstant;

/**
 * <pre>
 * 
 *  File: LoginInfoEventServer.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2017年12月6日				lizhaohui				Initial.
 *
 * </pre>
 */
public class LoginInfoEventServer
{

    public static Map<String, String> aPoint(Map<String, String> eventMap)
    {
        Map<String,String> resultMap=new HashMap<String,String>();
        if(UDCBConstant.GEARBEST_10002.equals(eventMap.get(Constant.GLB_D))){
            StringBuffer buffer=new StringBuffer();
            buffer.append(eventMap.get(Constant.GLB_U)==null?"0":eventMap.get(Constant.GLB_U)).append(Constant.HIVE_SEPERATE) //user_id
                  .append(eventMap.get(Constant.GLB_OD)==null?"0":eventMap.get(Constant.GLB_OD)).append(Constant.HIVE_SEPERATE) //cookie_id
                  .append(eventMap.get(Constant.GLB_OI)==null?"0":eventMap.get(Constant.GLB_OI)).append(Constant.HIVE_SEPERATE) //session_id
                  .append(eventMap.get(FieldConstant.TIME_LOCAL));
            resultMap.put(Constant.KEY,buffer.toString());
            resultMap.put(Constant.VALUE,buffer.toString());
        }
        return resultMap;
        
    }
}