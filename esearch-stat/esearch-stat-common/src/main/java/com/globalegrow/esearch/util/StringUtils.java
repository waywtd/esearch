package com.globalegrow.esearch.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.globalegrow.esearch.constant.Constant;

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
    
    /**
     * 校验字符串，热搜词字符串只能是数字 字母 特殊字符 \001 \002这种字符不能通过，是属于hive的分隔符 
     * @param msg
     */
    public static boolean validateStr(String msg)
    {
        boolean rs=false;
        if(StringUtils.isNotEmpty(msg)){
            String regEx="^[\\w+~!@#$%^&*()\\[\\]\\_+-=`;',./<>?:\"]*[\\w+\\s~!@#$%^&*()\\[\\]\\_+-=`;',./<>?:\"]*[\\s\\w+~!@#$%^&*()\\[\\]\\_+-=`;',./<>?:\"]*$";
            Pattern pattern = Pattern.compile(regEx);
            Matcher matcher = pattern.matcher(msg);
            rs = matcher.matches();
        }
        return rs;
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
    
    public static boolean isNotEmpty(Object obj)
    {
        return !isEmpty(obj);
    }
    
    public static String join(List<String> list){
        StringBuilder builder=new StringBuilder();
        if(list!=null && list.size()>0){
            int last=list.size()-1;
            for(int i=0;i<list.size();i++){
                builder.append(list.get(i));
                if(i<last){
                    builder.append(Constant.SEMICOLON);
                }
            }
        }
        return builder.toString();
    }
    
    public static void main(String[] args)
    {
        List<String> list=new ArrayList<String>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("r");
        System.out.println(join(list));
    }
}
