package com.globalegrow.esearch.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.globalegrow.esearch.constant.Constant;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 * 
 *  File: SkuDriver.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,                   Who,                    What;
 *  2017年3月24日              lizhaohui               Initial.
 *
 * </pre>
 */
public class CommonUtil
{
    
    private static ReentrantLock lock = new ReentrantLock();
    private static final Pattern PAGE_PATTERN = Pattern.compile("/[0-9]+.html");
    
    /**
     * @Title: getDate
     * @Description: TODO(截取日期)
     * @param @param value
     * @param @return 参数说明
     * @return String 返回类型
     * @throws ParseException
     */
    public static String getDate(String value) throws ParseException
    {
        Matcher m = getMatcher(value, Constant.REGDATE);
        if (m.find())
        {
            String string = m.group();
            return DateUtil.getTime(string);
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
     */
    public static Matcher getMatcher(String event, String compile)
    {
        Pattern p = Pattern.compile(compile);
        Matcher m = p.matcher(event);
        return m;
    }
    
    
    /**
     * 正则匹配只能有数字 
     * @param str
     * @return
     */
    public static boolean isNumeric(String str)
    {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * @Title: getJSON
     * @Description: TODO（数据解码)
     * @param @param value
     * @param @return
     * @param @throws UnsupportedEncodingException 参数说明
     * @return JSONObject 返回类型
     * @throws UnsupportedEncodingException
     */
    public static JSONObject getJSON(String value)
    {
        JSONObject jsonObject = null;
        if (value.contains("\\x22"))
        {
            value = value.replace("\\x22", "\"");
        }
        try
        {
            value = URLDecoder.decode(value, Constant.UTF8);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        if (JsonUtil.isJson(value))
        {
            jsonObject = JSONObject.parseObject(value);
        }
        return jsonObject;
    }
    
    /**
     * @Title: getJSON
     * @Description: TODO（数据解码)
     * @param @param value
     * @param @return
     * @param @throws UnsupportedEncodingException 参数说明
     * @return JSONObject 返回类型
     * @throws UnsupportedEncodingException
     */
    public static JSONArray getJSONArr(String value)
    {
        JSONArray jsonArray = null;
        if (value.contains("\\x22"))
        {
            value = value.replace("\\x22", "\"");
        }
        try
        {
            value = URLDecoder.decode(value, Constant.UTF8);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        if (JsonUtil.isJsonArr(value))
        {
            jsonArray = JSONArray.parseArray(value);
        }
        return jsonArray;
    }

    /**
     * @Title: getData
     * @Description: TODO(截取上班日志的数据)
     * @param @param value
     * @param @return 参数说明
     * @return String 返回类型
     */
    public static String getData(String value)
    {
        try
        {
            int i = value.indexOf("gif?");
            int j = value.indexOf("HTTP");
            String url = value.substring(i + 4, j - 1) + "&";
            return java.net.URLDecoder.decode(url, "utf-8");
        }
        catch (Exception e)
        {
            System.out.println("CommonUtil getData value:" + value);
        }
        return null;
    }

    /**
     * @Title: getData
     * @Description: TODO(截取上班日志的数据)
     * @param @param value
     * @param @return 参数说明
     * @return String 返回类型
     */
    public static String getDataN(String value)
    {
        try
        {
            int i = value.indexOf("gif?");
            String url = value.substring(i + 4) + "&";
            String result = java.net.URLDecoder.decode(url, "utf-8");
            return result;
        }
        catch (Exception e)
        {
            System.out.println("CommonUtil getDataN value:" + value);
        }
        return null;
    }

    /**
     * 过滤指定网站日志
     * 
     * @param httpurl 日志pl值
     * @param country 指定国家站标识
     * @return
     */
    public static boolean validateSiteCountry(String httpurl, String country)
    {
        boolean result = false;
        if (country.trim().equals(getURLCountry(httpurl)))
        {
            result = true;
        }
        return result;
    }

    /**
     * 根据url获取是哪个国家站点
     * http://www.gearbest.com  -- > www
     * @param httpUrl
     * @return
     */
    public static String getURLCountry(String httpUrl)
    {
        String country = "www";
        if (StringUtils.isNotEmpty(httpUrl))
        {
            try
            {
                URL url = new URL(httpUrl);
                country = url.getHost().split("\\.")[0];
            }
            catch (MalformedURLException e)
            {
                System.out.println("CommonUtil getURLCountry value:" + httpUrl);
            }
        }
        return country;
    }
    
    /**
     * 获取网站主机名
     * http://www.gearbest.com  -- > www.gearbest.com
     *
     * @param httpUrl
     * @return
     */
    public static String getURLHostName(String httpUrl){
        String hostname = "";
        if(httpUrl.contains("ref=")){
            httpUrl=httpUrl.split("ref=")[1];
        }
        if (StringUtils.isNotEmpty(httpUrl))
        {
            try
            {
                URL url = new URL(httpUrl);
                hostname = url.getHost();
            }
            catch (MalformedURLException e)
            {
                System.out.println("CommonUtil getURLHostName value:" + httpUrl);
            }
        }
        return hostname;
    }

    
    /**
     * 获取关键字统计hive路径
     * 
     * @param ubcd 站点标识
     * @param partitionDate 日期分区
     * @param state 国家站点
     * @return
     */
    public static String outputPath(String basePath, String ubcd, String partitionDate, String state)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(basePath).append(Constant.SEPERATE).append("ubcd=").append(ubcd).append(Constant.SEPERATE)
                .append(partitionDate).append(Constant.SEPERATE).append("state=").append(state)
                .append(Constant.SEPERATE);
        return buffer.toString();
    }
    
    /**
     * 获取关键字统计hive路径
     * 
     * @param ubcd 站点标识
     * @param partitionDate 日期分区
     * @return
     */
    public static String outputPath(String basePath, String ubcd, String partitionDate)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(basePath).append(Constant.SEPERATE).append("ubcd=").append(ubcd).append(Constant.SEPERATE)
        .append(partitionDate).append(Constant.SEPERATE);
        return buffer.toString();
    }

    /**
     * hive根据日期进行分区
     * 
     * @param timeLocal
     * @return
     */
    public static String getPartitionDate(String timeLocal)
    {
        StringBuffer buffer = new StringBuffer();
        String[] timeLocals = timeLocal.split("-");
        buffer.append("year=").append(timeLocals[0]).append("/").append("month=").append(timeLocals[1]).append("/")
                .append("day=").append(timeLocals[2]);
        return buffer.toString();
    }
    
    /**
     * 获取指定国家站日志 下订单页面cl字段是不区分国家站点的。
     * @param station 国家网站地址
     * @param cl 当前页面
     * @param eventMap 当前日志
     * @return
     */
    public static boolean isGetStation(String station, String cl, Map<String, String> eventMap)
    {
        boolean flag = false;
        if (cl.contains(station) && eventMap.get("http_referer").contains(station))
        {
            flag = true;
        }
        return flag;
    }
    
    public static boolean isGetStation(String station, Map<String, String> eventMap)
    {
        boolean flag = false;
        if (StringUtils.isNotEmpty(eventMap) && StringUtils.isNotEmpty(eventMap.get(Constant.GLB_CL))
                && eventMap.get(Constant.GLB_CL).contains(station) && eventMap.get("http_referer").contains(station))
        {
            flag = true;
        }
        return flag;
    }
    
    public static boolean isGetStationPL(String station, Map<String, String> eventMap)
    {
        boolean flag = false;
        if (StringUtils.isNotEmpty(eventMap) && StringUtils.isNotEmpty(eventMap.get(Constant.GLB_PL))
                && eventMap.get(Constant.GLB_PL).contains(station))
        {
            flag = true;
        }
        return flag;
    }
    
    public static boolean isCategoryEvent(Map<String,String> eventMap)
    {
        boolean flag=false;
        if(Constant.B01.equals(eventMap.get(Constant.GLB_S)) ||
           Constant.B02.equals(eventMap.get(Constant.GLB_S)) ||
           Constant.B03.equals(eventMap.get(Constant.GLB_S)) ||
           Constant.B04.equals(eventMap.get(Constant.GLB_S)))
        {
            flag=true;
        }
        return flag;
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
        }
        finally
        {
            lock.unlock();
        }
        return null;
    }

    public static String getPageNum(String url, String params) {
        String page = "1";
        Matcher matcher = PAGE_PATTERN.matcher(url);
        if (params.contains("page=")) {
            for (String param : params.split("&")) {
                if (param.contains("page=")) {
                    page = org.apache.commons.lang3.StringUtils.substringAfter(param, "page=");
                    if (org.apache.commons.lang3.StringUtils.isNotBlank(page)) {
                        if (page.contains("?")) {
                            page = org.apache.commons.lang3.StringUtils.substringBefore(page, "?");
                        }
                        if (page.contains("/")) {
                            page = org.apache.commons.lang3.StringUtils.substringBefore(page, "/");
                        }
                    }
                }
            }
        } else if (matcher.find()){
            page = matcher.group(0).replace("/", "").replace(".html", "");
        }
        return  page;
    }

    public static String getSort(String params) {
        String sort = "0";
        if (params.contains("odr=")) {
            for (String param : params.split("&")) {
                if (param.contains("odr=")) {
                    sort = org.apache.commons.lang3.StringUtils.substringAfter(param, "odr=");
                    if (org.apache.commons.lang3.StringUtils.isNotBlank(sort)) {
                        if (sort.contains("?")) {
                            sort = org.apache.commons.lang3.StringUtils.substringBefore(sort, "?");
                        } else if (sort.contains("%3F")) {
                            sort = org.apache.commons.lang3.StringUtils.substringBefore(sort, "%3F");
                        }
                        if (sort.contains("/")) {
                            sort = org.apache.commons.lang3.StringUtils.substringBefore(sort, "/");
                        }
                    }
                }
            }
        }
        return  sort;
    }

    public static String keywordDecoder(String keyword){
        if (org.apache.commons.lang3.StringUtils.isBlank(keyword)) {
            return "";
        }
        keyword = org.apache.commons.lang3.StringUtils.trim(keyword.toLowerCase());
        try {
            return URLDecoder.decode(keyword, "UTF-8");
        } catch (Exception e) {
            return keyword;
        }
    }

    public static void main(String[] args)
    {
        /*String data = getDataN("https://s.logsss.com/_ubc.gif?t=ie&w=1550028&tm=1498633569404&pm=mp&ubcta=[{%22sku%22:%22212551702%22},{%22sku%22:%22212551701%22}]&u=15627264&oi=biniacdarninhthpinns4ibbc5&d=10002&s=b02&b=b&cl=http%3A%2F%2Fwww.gearbest.com%2Fdress-_gear%2F&pl=http%3A%2F%2Fwww.gearbest.com%2Fdress-_gear%2F");
        Matcher m = CommonUtil.getMatcher(data, Constant.REGUBC);
        while (m.find())
        {
            String key = m.group(1);
            String value = m.group(2);
            System.out.println("key:" + key + "\tvalue:" + value);
        }
        System.out.println(data);
        String url="http://www.gearbest.com";
        System.out.println(getURLCountry(url));*/
        String str="https://login.gearbest.com/m-users-a-register.htm?ref=https://www.gearbest.com/microphone/pp_327063.html?lkid=11079385";
        if(str.contains("ref=")){
            str=str.split("ref=")[1];
        }
        System.out.println(getURLHostName(str));
    }

}
