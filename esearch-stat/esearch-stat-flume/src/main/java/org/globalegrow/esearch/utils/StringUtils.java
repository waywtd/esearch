package org.globalegrow.esearch.utils;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 * 
 *  File: StringUtils.java
 * 
 *  Copyright (c) 2016, globalegrow.com All Rights Reserved.
 * 
 *  String 工具类
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2016年8月26日				lizhaohui				Initial.
 *
 * </pre>
 */
public class StringUtils
{

    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object obj)
    {
        if (obj == null)
            return true;

        if (obj instanceof CharSequence)
            return ((CharSequence) obj).length() == 0;

        if (obj instanceof Collection)
            return ((Collection) obj).isEmpty();

        if (obj instanceof Map)
            return ((Map) obj).isEmpty();

        if (obj instanceof Object[])
        {
            Object[] object = (Object[]) obj;
            if (object.length == 0)
            {
                return true;
            }
            boolean empty = true;
            for (int i = 0; i < object.length; i++)
            {
                if (!isEmpty(object[i]))
                {
                    empty = false;
                    break;
                }
            }
            return empty;
        }
        return false;
    }

    public static boolean isNotEmpty(Object obj)
    {
        return !isEmpty(obj);
    }
    
    /**
     * Remove special char only save letter number and space
     *
     * @param str
     * @return
     */
    public static String removeSpecialChar(String str)
    {
    	if(str == null)
    	{
    		return null;
    	}
    	//String regEx="[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】\\[\\]‘；：”“’。，、？]";   
    	String regEx = "[^a-zA-Z0-9 ]"; 
    	Pattern p = Pattern.compile(regEx);      
    	Matcher m = p.matcher(str);      
    	return m.replaceAll("").trim();
    }

    /**
     * 如果对象为null则转换成特殊字符串
     * @param obj
     * @param special
     * @return
     */
    public static String nullToSpecial(String obj, String special) {
        if(obj == null) {
            obj = special;
        }
        return obj;
    }
}
