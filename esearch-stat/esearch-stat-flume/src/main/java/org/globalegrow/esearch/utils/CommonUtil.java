package org.globalegrow.esearch.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.globalegrow.esearch.constant.PatternConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <pre>
 * 
 *  File: CommonUtil.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2017年11月9日				lizhaohui				Initial.
 *
 * </pre>
 */
public class CommonUtil
{

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtil.class);
    
    private static ReentrantLock lock = new ReentrantLock();
    static Map<String,String> appCache = new HashMap<String,String>();
    static {

        appCache.put("gearbest","10002"); //gearbest gearbestsoa
        appCache.put("rosegal","10007"); //rg
        appCache.put("gamiss","10019"); //gamiss
        appCache.put("everbuying","10001"); //everbuying
        appCache.put("zaful","10013"); //zaful
    }
    /**
     * @Title: getData
     * @Description: TODO(截取上班日志的数据)
     * @param @param value
     * @param @return 参数说明
     * @return String 返回类型
     * @throws 方法异常
     */
    public static String getData(String value)
    {
        try
        {
            int i = value.indexOf("gif?");
            int j = value.indexOf("HTTP");
            String url = value.substring(i + 4, j - 1) + "&";
            String result=java.net.URLDecoder.decode(url, "utf-8");
            return result;
        }
        catch (Exception e)
        {
            LOGGER.debug("value:" + value);
        }
        return null;
    }
    
    /**
     * @Title: getMatcher
     * @Description: TODO(获取 compile格式的 matcher)
     * @param @param event
     * @param @param compile
     * @param @return 参数说明
     * @return Matcher 返回类型
     * @throws 方法异常
     */
    public static Matcher getMatcher(String event, String compile)
    {
        if (StringUtils.isNotEmpty(event))
        {
            Pattern p = Pattern.compile(compile);
            Matcher m = p.matcher(event);
            return m;
        }
        else
        {
            return null;
        }
    }
    
    public static String getValueByPattern(String regEx, String valuesStr)
    {
        Pattern pattern = null;
        Matcher matcher = null;
        try
        {
            lock.lock();
            pattern = Pattern.compile(regEx);;
            matcher = pattern.matcher(valuesStr);
            if (matcher.find())
            {
                // 匹配成功
                return matcher.group(0);
            }
        }
        catch (Exception e)
        {
            LOGGER.error(e.toString(), e);
        }
        finally
        {
            lock.unlock();
        }
        return null;
    }
    public static String getValueByField(String field, String valuesStr){
        try
        {
            lock.lock();
            int index = valuesStr.indexOf(field);
            if(index != -1){
                String valueS = valuesStr.substring(index);
                int inda = valueS.indexOf("&");
                String bStr = "";
                if(inda == -1){
                    bStr = valueS.toLowerCase();
                }else{
                    bStr = valueS.substring(0,inda).toLowerCase();
                }
               Iterator<String> it = appCache.keySet().iterator();
                while(it.hasNext()){
                    String name = it.next();
                    if(bStr.contains(name)){
                        return appCache.get(name);
                    }
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error(e.toString(), e);
        }
        finally
        {
            lock.unlock();
        }
        return null;
    }
    public static void main(String [] args){
        String a = CommonUtil.getValueByPattern(PatternConstants.GEARBEST_CL_CAT_ID_PATTREN,"https://www.gearbest.com/xiaomi-_gear/c_11992/");
        System.out.println(a);
    }

}