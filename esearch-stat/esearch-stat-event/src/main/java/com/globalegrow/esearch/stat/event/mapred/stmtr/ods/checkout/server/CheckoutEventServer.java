package com.globalegrow.esearch.stat.event.mapred.stmtr.ods.checkout.server;

import com.globalegrow.esearch.constant.Constant;
import com.globalegrow.esearch.constant.FieldConstant;
import com.globalegrow.esearch.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 
 *  File: CheckoutEventServer.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2017年11月24日				lizhaohui				Initial.
 *
 * </pre>
 */
public class CheckoutEventServer
{

    public static List<String> loopList(String buffKey)
    {
        List<String> loopList = new ArrayList<String>();
        String[] arrKey = buffKey.split(Constant.SEMICOLON);
        for (int i = 0; i < arrKey.length; i++)
        {
            loopList.add(arrKey[i]);
        }
        return loopList;
    }
    
    
    public static Map<String,String> checkOutEvent(Map<String,String> eventMap)
    {
        //glb_t:ic glb_x:search 必须是执行的国家站点
        Map<String,String> resultMap=new HashMap<String,String>();
        if(StringUtils.isNotEmpty(eventMap.get(Constant.GLB_D)) &&
                Constant.ic.equals(eventMap.get(Constant.GLB_T)) &&
                Constant.CHECK_OUT.equals(eventMap.get(Constant.UBCTD_X)) && 
                StringUtils.isNotEmpty(eventMap.get(Constant.SKU_LIST))) 
        {
            StringBuffer buffer=null;
            List<String> list=new ArrayList<String>();
            String[] arrSku=eventMap.get(Constant.SKU_LIST).split(Constant.SEMICOLON);
            for(int i=0;i<arrSku.length;i++){
                buffer=new StringBuffer();
                buffer.append(arrSku[i]).append(Constant.HIVE_SEPERATE)
                .append(eventMap.get(Constant.GLB_U)).append(Constant.HIVE_SEPERATE)
                .append(eventMap.get(Constant.GLB_OI)).append(Constant.HIVE_SEPERATE)
                .append(eventMap.get(Constant.GLB_OD)).append(Constant.HIVE_SEPERATE)
                .append(eventMap.get(Constant.GLB_PL)==null?"":eventMap.get(Constant.GLB_PL)).append(Constant.HIVE_SEPERATE)
                .append(eventMap.get(FieldConstant.TIME_LOCAL)).append(Constant.HIVE_SEPERATE);
                list.add(buffer.toString());
            }
            resultMap.put(Constant.KEY,StringUtils.join(list));
        }
        return resultMap;
    }
    
}