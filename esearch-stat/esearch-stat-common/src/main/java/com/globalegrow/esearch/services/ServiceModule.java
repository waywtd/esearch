package com.globalegrow.esearch.services;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import com.globalegrow.esearch.constant.Constant;
import com.globalegrow.esearch.util.BusinessUtil;
import com.globalegrow.esearch.util.StringUtils;

/**
 * <pre>
 * 
 *  File: ServiceModule.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2017年9月16日				lizhaohui				Initial.
 *
 * </pre>
 */
public class ServiceModule
{
    
     public static Map<String,String> getUrlParmas(Matcher matcher){
        Map<String,String> eventMap=new HashMap<String,String>();
        while (matcher.find())
        {
            String key = matcher.group(1);
            String value = matcher.group(2);
            switch (key)
            {
                case Constant.GLB_D:
                    eventMap.put(Constant.GLB_D, value);
                    break;
                case Constant.GLB_T:
                    eventMap.put(Constant.GLB_T, value);
                    break;
                case Constant.GLB_X:
                    eventMap.put(Constant.GLB_X, value);
                    break;
                case Constant.GLB_W:
                    eventMap.put(Constant.GLB_W, value);
                    break;
                case Constant.GLB_TM:
                    eventMap.put(Constant.GLB_TM, value);
                    break;
                case Constant.GLB_SC:
                    eventMap.put(Constant.GLB_SC, value);
                    break;
                case Constant.GLB_S:
                    eventMap.put(Constant.GLB_S, value);
                    break;
                case Constant.GLB_B:
                    eventMap.put(Constant.GLB_B, value);
                    break;
                case Constant.GLB_SCKW:
                    String valueStr = value;
                    if (StringUtils.validateStr(valueStr.toLowerCase()))
                    {
                        eventMap.put(Constant.GLB_SCKW, valueStr.toLowerCase());
                    }
                    break;
                case Constant.GLB_SIWS:
                    String valueSiws = value;
                    if (StringUtils.validateStr(valueSiws.toLowerCase()))
                    {
                        eventMap.put(Constant.GLB_SIWS, valueSiws.toLowerCase());
                    }
                    break;
                case Constant.GLB_PM:
                    eventMap.put(Constant.GLB_PM, value);
                    break;
                case Constant.GLB_UBCTA:
                    eventMap.putAll(BusinessUtil.getSkuInfo(value));
                    eventMap.putAll(BusinessUtil.getSkuInfoArr(value));
                    break;
                case Constant.GLB_SKUINFO:
                    eventMap.putAll(BusinessUtil.getSkuInfo(value));
                    eventMap.putAll(BusinessUtil.getSkuInfoArr(value));
                    break;
                case Constant.GLB_FILTER:
                    eventMap.putAll(BusinessUtil.getSkuInfo(value));
                    break;
                case Constant.GLB_U:
                    eventMap.put(Constant.GLB_U, value);
                    break;
                case Constant.GLB_OI:
                    eventMap.put(Constant.GLB_OI, value);
                    break;
                case Constant.GLB_OD:
                    eventMap.put(Constant.GLB_OD, value);
                    break;
                case Constant.GLB_CL:
                    eventMap.put(Constant.GLB_CL, value);
                    break;
                case Constant.GLB_PL:
                    eventMap.put(Constant.GLB_PL, value);
                    break;
                case Constant.GLB_OSR:
                    eventMap.put(Constant.GLB_OSR, value);
                    break;
                case Constant.GLB_P:
                    eventMap.put(Constant.GLB_P, value);
                    break;
                case Constant.GLB_OLK:
                    eventMap.put(Constant.GLB_OLK, value);
                    break;
                case Constant.GLB_SL:
                    eventMap.put(Constant.GLB_SL, value);
                    break;
                case Constant.GLB_SK:
                    eventMap.put(Constant.GLB_SK, value);
                    break;
                case Constant.GCLID:
                    eventMap.put(Constant.GCLID, value);
                    break;
                default:
                    break;
            }
        }
        return eventMap;
    }
}