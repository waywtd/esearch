package com.globalegrow.esearch.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.globalegrow.esearch.constant.Constant;

import java.net.URLDecoder;

/**
 * <pre>
 * 
 *  File: JsonUtil.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2017年3月15日				lizhaohui				Initial.
 *
 * </pre>
 */
public class JsonUtil
{

    /**
     * @Title: getJSON
     * @Description: TODO（数据解码)
     * @param @param value
     * @param @return
     * @param @throws UnsupportedEncodingException 参数说明
     * @return JSONObject 返回类型
     */
    public static JSONObject getJSON(String value) throws Exception
    {
        value = URLDecoder.decode(value, Constant.UTF8);
        return JSONObject.parseObject(value);
    }

    public static boolean isJson(String value)
    {
        try
        {
            getJSON(value);
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }
    
    public static boolean isJsonArr(String value)
    {
        try
        {
            getJSONArr(value);
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    public static JSONArray getJSONArr(String value) throws Exception
    {
        value = URLDecoder.decode(value, Constant.UTF8);
        return JSONObject.parseArray(value);
    }
}