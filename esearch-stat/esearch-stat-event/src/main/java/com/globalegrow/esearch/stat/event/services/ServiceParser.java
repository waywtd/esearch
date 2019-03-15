package com.globalegrow.esearch.stat.event.services;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import com.globalegrow.esearch.constant.Constant;
import com.globalegrow.esearch.constant.MethodConstant;
import com.globalegrow.esearch.services.ServiceModule;
import com.globalegrow.esearch.stat.event.mapred.stmtr.ods.checkout.server.CheckoutEventServer;
import com.globalegrow.esearch.stat.event.mapred.stmtr.ods.checkout.server.UserLoginEventServer;
import com.globalegrow.esearch.stat.event.mapred.stmtr.ods.clickcategory.server.ClickCategoryEventService;
import com.globalegrow.esearch.stat.event.mapred.stmtr.ods.clicksku.service.ClickSkuEventService;
import com.globalegrow.esearch.stat.event.mapred.stmtr.ods.searchbox.service.SearchBoxEventService;
import com.globalegrow.esearch.stat.event.mapred.stmtr.website.loginInfo.apoint.server.LoginInfoEventServer;
import com.globalegrow.esearch.stat.event.mapred.stmtr.website.searchWord.bpoint.service.SearchWordService;
import com.globalegrow.esearch.stat.event.mapred.stmtr.website.weight.bpoint.service.SkuWeightBPointService;
import com.globalegrow.esearch.util.BusinessUtil;
import com.globalegrow.esearch.util.CommonUtil;
import com.globalegrow.esearch.util.StringUtils;

/**
 * <pre>
 * 
 *  File: ServiceParser.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2017年9月14日				lizhaohui				Initial.
 *
 * </pre>
 */
public class ServiceParser
{

    /**
     * 解析url参数
     * @param inputValue hdfs一行日志
     * @param site 站点
     * @return
     */
    public static Map<String, String> searchBoxEvent(String inputValue, String site,String siteCode)
    {
        Map<String, String> data = new HashMap<String, String>();
        if (StringUtils.isNotEmpty(site))
        {
            data.put(Constant.SITE, site);
        }
        data.put(Constant.SITE_CODE, siteCode);
        try
        {
            String line = new String(inputValue.toString().getBytes(), Constant.UTF8);
            if (line.contains(Constant.STATUESUCCEED) && (!line.contains(Constant.ICON)) && line.contains(CommonUtil.getURLHostName(site)))
            {
                data.putAll(BusinessUtil.initMap(site, siteCode, line));
                String event = CommonUtil.getData(line);
                if (StringUtils.isNotEmpty(event))
                {
                    Matcher matcher = CommonUtil.getMatcher(event, Constant.REGUBC);
                    data.putAll(ServiceModule.getUrlParmas(matcher));
                }
            }
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return data;
    }
    
    public static Map<String, String> searchBoxEvent01(String inputValue, String site,String siteCode,String partition)
    {
        Map<String, String> data = new HashMap<String, String>();
        if (StringUtils.isNotEmpty(site))
        {
            data.put(Constant.SITE, site);
        }
        data.put(Constant.SITE_CODE, siteCode);
        try
        {
            String line = new String(inputValue.toString().getBytes(), Constant.UTF8);
            if (line.contains(Constant.STATUESUCCEED))
            {
                data.putAll(BusinessUtil.initMap(site, siteCode, line,partition));
                String event = CommonUtil.getData(line);
                if (StringUtils.isNotEmpty(event))
                {
                    Matcher matcher = CommonUtil.getMatcher(event, Constant.REGUBC);
                    data.putAll(ServiceModule.getUrlParmas(matcher));
                }
            }
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return data;
    }
    
    public static Map<String, String> searchBoxEvent02(String inputValue,String siteCode,String partition)
    {
        Map<String, String> data = new HashMap<String, String>();
        data.put(Constant.SITE_CODE, siteCode);
        try
        {
            String line = new String(inputValue.toString().getBytes(), Constant.UTF8);
            if (line.contains(Constant.STATUESUCCEED))
            {
                data.putAll(BusinessUtil.initMap01(siteCode, line,partition));
                String event = CommonUtil.getData(line);
                if (StringUtils.isNotEmpty(event))
                {
                    Matcher matcher = CommonUtil.getMatcher(event, Constant.REGUBC);
                    data.putAll(ServiceModule.getUrlParmas(matcher));
                }
            }
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return data;
    }
    
    
    public static Map<String,String> searchBoxEvent(String inputValue, String site,String siteCode,String partition){
        Map<String, String> data = new HashMap<String, String>();
        if (StringUtils.isNotEmpty(site))
        {
            data.put(Constant.SITE, site);
        }
        data.put(Constant.SITE_CODE, siteCode);
        try
        {
            String line = new String(inputValue.toString().getBytes(), Constant.UTF8);
            if (line.contains(Constant.STATUESUCCEED))
            {
                data.putAll(BusinessUtil.initMap(site, siteCode, line,partition));
                String event = CommonUtil.getData(line);
                if (StringUtils.isNotEmpty(event))
                {
                    Matcher matcher = CommonUtil.getMatcher(event, Constant.REGUBC);
                    data.putAll(ServiceModule.getUrlParmas(matcher));
                }
            }
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return data;
    }
    
    /**
     * 判断具体业务逻辑
     * @param eventMap url具体参数
     * @param method 业务
     * @return
     */
    public static Map<String,String> mapperEmit(Map<String,String> eventMap,String method)
    {
        Map<String,String> emitMap=new HashMap<String,String>();
        switch (method)
        {
            //搜索框事件
            case MethodConstant.SEARCH_BOX_EVENT:
                emitMap.putAll(SearchBoxEventService.searchBoxEvent(eventMap));
                break;
            case MethodConstant.CLICK_SKU_EVENT:
                emitMap.putAll(ClickSkuEventService.clickSkuEvent(eventMap));
                break;
            case MethodConstant.CLICK_CATEGORY_EVENT:
                emitMap.putAll(ClickCategoryEventService.clickCategoryEvent(eventMap));
                break;
            case MethodConstant.CHECK_OUT_EVENT:
                emitMap.putAll(CheckoutEventServer.checkOutEvent(eventMap));
                break;
            case MethodConstant.USER_LOGIN_EVENT:
                emitMap.putAll(UserLoginEventServer.userLoginEvent(eventMap));
                break;
            case MethodConstant.SEARCH_WORD_B_POINT:
                emitMap.putAll(SearchWordService.bPoint(eventMap));
                break;
            case MethodConstant.SKU_WEIGHT_B_POINT:
                emitMap.putAll(SkuWeightBPointService.bPoint(eventMap));
                break;
            case MethodConstant.SKU_WEIGHT_A_POINT:
                /*emitMap.putAll(SkuWeightAPointService.aPoint(eventMap));*/
                break;
            case MethodConstant.LOGIN_INFO_A_POINT:
                emitMap.putAll(LoginInfoEventServer.aPoint(eventMap));
                break;
            default:
                break;
        }
        return emitMap;
    }
}