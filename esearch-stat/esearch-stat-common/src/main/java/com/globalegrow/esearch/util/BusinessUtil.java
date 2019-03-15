package com.globalegrow.esearch.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.globalegrow.esearch.constant.Constant;
import com.globalegrow.esearch.constant.FieldConstant;

import java.util.*;

/**
 * <pre>
 * 
 *  File: BusinessUtil.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2017年6月13日				lizhaohui				Initial.
 *
 * </pre>
 */
public class BusinessUtil
{

    /**
     * 获取ubamount大于0且不为空
     * 
     * @param fieldValue fieldValue ubcta={"ubamount":"9935"}
     * @return
     */
    public static boolean validateUbamount(String fieldValue)
    {
        boolean flag = false;
        if (StringUtils.isNotEmpty(fieldValue) && getSearchUbamount(fieldValue) > 0)
        {
            flag = true;
        }
        return flag;
    }

    /**
     * 获取搜索时 搜出的商品数量
     * 
     * @param fieldValue ubcta={"ubamount":"9935"}
     * @return
     */
    public static Long getSearchUbamount(String fieldValue)
    {
        Long result = 0l;
        StringBuffer ubamount = new StringBuffer();
        JSONObject json = CommonUtil.getJSON(fieldValue);
        List<String> ubamountList = Arrays.asList(json.getString(Constant.UBAMOUNT_AT).split(","));
        for (String at : ubamountList)
        {
            ubamount.append(at);
        }
        result=Long.parseLong(ubamount.toString());
        return result;
    }
    
    /**
     * 解析ubct filter值
     * 搜索框 ubcta={"ubamount":"9935"}
     * 点击商品详情 ubcta={"rank":1,"sckw":"dress"}
     *         filter={"view":36,"page":"1"}
     * @param fieldValue
     * @return
     */
    public static Map<String,String> getFieldValue(String fieldValue)
    {
        Map<String,String> mapData=new HashMap<String,String>();
        if(StringUtils.isNotEmpty(fieldValue)){
            JSONObject json = CommonUtil.getJSON(fieldValue);
            if(StringUtils.isNotEmpty(json)){
                //ubcta={"ubamount":"9935"}
                if(json.containsKey(Constant.AT))
                {
                    long ubamount=getSearchUbamount(fieldValue);
                    mapData.put(Constant.AT, String.valueOf(ubamount));
                }
                //ubcta={"rank":1,"sckw":"dress"}
                if(json.containsKey(Constant.RANK))
                {
                    mapData.put(Constant.RANK,json.getString(Constant.RANK));
                }
                if(json.containsKey(Constant.UBCKW_SCKW))
                {
                    mapData.put(Constant.UBCKW,json.getString(Constant.UBCKW_SCKW).toLowerCase());
                }
                if(json.containsKey(Constant.FILTER_PAGE))
                {
                    mapData.put(Constant.FILTER_PAGE,json.getString(Constant.FILTER_PAGE));
                }
            }
        }
        return mapData;
    }
    
    /**
     * 如果是曝光 走ubcta
     *     点击走sku
     * @param skuMap
     * @return
     */
    public static List<String> getSkuList(Map<String, String> skuMap)
    {
        List<String> listSku = new ArrayList<String>();
        if (StringUtils.isNotEmpty(skuMap.get(Constant.UBCTA)) && Constant.ie.equals(skuMap.get(Constant.UBCT)))
        {
            listSku.addAll(getUBCTASku(skuMap.get(Constant.UBCTA)) );
        }
        else if (StringUtils.isNotEmpty(skuMap.get(Constant.SKU)) && Constant.ic.equals(skuMap.get(Constant.UBCT)))
        {
            listSku.add(skuMap.get(Constant.SKU));
        }
        return listSku;
    }
    
    /**
     * 如果是曝光 走ubcta
     *     点击走sku
     * @param skuMap
     * @return
     */
    public static List<String> getSkuListEvent(Map<String, String> skuMap)
    {
        List<String> listSku = new ArrayList<String>();
        if (StringUtils.isNotEmpty(skuMap.get(Constant.SKU_LIST)))
        {
            listSku.addAll(Arrays.asList(skuMap.get(Constant.SKU_LIST).split(Constant.SEMICOLON)));
        }
        else if (StringUtils.isNotEmpty(skuMap.get(Constant.SKU)) && Constant.ic.equals(skuMap.get(Constant.GLB_T)))
        {
            listSku.add(skuMap.get(Constant.SKU));
        }
        return listSku;
    }

    /**
     * 根据sku json获取商品
     * 格式:{"sku":"191813902","pam":1,"pc":"12313","k":"21"}
     * @param skuinfo
     * @return
     */
    public static Map<String, String> getSku(String skuinfo)
    {
        Map<String, String> skuinfoMap = new HashMap<String, String>();
        try
        {
            if (StringUtils.isNotEmpty(skuinfo) && JsonUtil.isJson(skuinfo))
            {
                JSONObject jsonObject = CommonUtil.getJSON(skuinfo);
                if(StringUtils.isNotEmpty(jsonObject) && jsonObject.containsKey(Constant.SKU) && jsonObject.containsKey(Constant.AMOUNT_PAM)
                            && jsonObject.containsKey(Constant.CATEGORY_PC))
                {
                    skuinfoMap.put(Constant.SKU, jsonObject.getString(Constant.SKU));
                    skuinfoMap.put(Constant.AMOUNT, jsonObject.getString(Constant.AMOUNT_PAM));
                    skuinfoMap.put(Constant.CATEGORY, jsonObject.getString(Constant.CATEGORY_PC));
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return skuinfoMap;
    }
    
    public static Map<String, String> getSkuInfo(String skuinfo)
    {
        Map<String, String> skuinfoMap = new HashMap<String, String>();
        try
        {
            if (StringUtils.isNotEmpty(skuinfo) && JsonUtil.isJson(skuinfo))
            {
                JSONObject jsonObject = CommonUtil.getJSON(skuinfo);
                if(StringUtils.isNotEmpty(jsonObject))
                {
                    if(StringUtils.isNotEmpty(jsonObject.getString(Constant.SKU))){
                        skuinfoMap.put(Constant.SKU, jsonObject.getString(Constant.SKU));
                    }
                    if(StringUtils.isNotEmpty(jsonObject.getString(Constant.PAM)))
                    {
                        skuinfoMap.put(Constant.PAM, jsonObject.getString(Constant.PAM));
                    }
                    if(StringUtils.isNotEmpty(jsonObject.getString(Constant.PC))){
                        skuinfoMap.put(Constant.PC, jsonObject.getString(Constant.PC));
                    }
                    if(StringUtils.isNotEmpty(jsonObject.getString(Constant.K))){
                        skuinfoMap.put(Constant.K, jsonObject.getString(Constant.K));
                    }
                    if(StringUtils.isNotEmpty(jsonObject.getString(Constant.VIEW))){
                        skuinfoMap.put(Constant.VIEW, jsonObject.getString(Constant.VIEW));
                    }
                    if(StringUtils.isNotEmpty(jsonObject.getString(Constant.PAGE))){
                        skuinfoMap.put(Constant.PAGE, jsonObject.getString(Constant.PAGE));
                    }
                    if(StringUtils.isNotEmpty(jsonObject.getString(Constant.SORT))){
                        skuinfoMap.put(Constant.SORT, jsonObject.getString(Constant.SORT));
                    }
                    if(StringUtils.isNotEmpty(jsonObject.getString(Constant.RANK))){
                        skuinfoMap.put(Constant.RANK, jsonObject.getString(Constant.RANK));
                    }
                    if(StringUtils.isNotEmpty(jsonObject.getString(Constant.SCKW))){
                        skuinfoMap.put(Constant.SCKW, jsonObject.getString(Constant.SCKW));
                    }
                    if(StringUtils.isNotEmpty(jsonObject.getString(Constant.AT))){
                        skuinfoMap.put(Constant.AT, jsonObject.getString(Constant.AT));
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return skuinfoMap;
    }
    
    
    /**
     *  [{\"pam\":\"1\",\"k\":\"21\",\"pc\":\"11293\",\"sku\":\"215046201\"},{\"pam\":\"2\",\"k\":\"21\",\"pc\":\"11294\",\"sku\":\"213081501\"}]
     *  将上述json中的sku取出来并排序
     * @param skuinfo
     * @return
     */
    public static Map<String, String> getSkuInfoArr(String skuinfo)
    {
        Map<String, String> skulistMap = new HashMap<String, String>();
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isNotEmpty(skuinfo) && JsonUtil.isJsonArr(skuinfo))
        {
            List<String> skuList = new ArrayList<String>();
            JSONArray jsonArray = CommonUtil.getJSONArr(skuinfo);
            JSONObject jsonObject = null;
            for (int i = 0; i < jsonArray.size(); i++)
            {
                jsonObject = jsonArray.getJSONObject(i);
                skuList.add(jsonObject.getString(Constant.SKU));
            }
            builder.append(StringUtils.join(skuList));
            skulistMap.put(Constant.SKU_LIST, builder.toString());
        }
        return skulistMap;
    }

    /**
     * 根据ubcta数据获取sku曝光数据
     * 格式:[{"mrlc":"C_1","sku":"143635306"},{"mrlc":"E_2","sku":"211879703"},{"mrlc":"A_1","sku":"212625704"},{"mrlc":"A_1","sku":"194022501"},{"mrlc":"A_1","sku":"205630901"}]
     * @param ubcta
     * @return
     */
    public static List<String> getUBCTASku(String ubcta)
    {
        if (ubcta.contains("\\x22"))
        {
            ubcta = ubcta.replace("\\x22", "\"");
        }
        List<String> skuList = new ArrayList<String>();
        if(ubcta.startsWith("[") && ubcta.endsWith("]")){
            List<JSONObject> jsonList = JSONArray.parseArray(ubcta, JSONObject.class);
            for (JSONObject json : jsonList)
            {
                skuList.add(json.getString(Constant.SKU));
            }
        }
        return skuList;
    }
    
    public static Map<String,String> initMap(String site,String siteCode,String line)
    {
        Map<String, String> data = new HashMap<String, String>();
        data.put(Constant.SITE, site);
        data.put(Constant.SITE_CODE, siteCode);
        Map<String, String> eventMap = new LinkedHashMap<String, String>();
        MapUtil.getEventmap(line, eventMap);
        data.putAll(eventMap);
        data.put(FieldConstant.TIME_LOCAL,DateUtil.convertDate(eventMap.get(FieldConstant.TIME_LOCAL), "yyyy-MM-dd"));
        return data;
    }
    
    public static Map<String,String> initMap(String site,String siteCode,String line,String partition)
    {
        Map<String, String> data = new HashMap<String, String>();
        data.put(Constant.SITE, site);
        data.put(Constant.SITE_CODE, siteCode);
        Map<String, String> eventMap = new LinkedHashMap<String, String>();
        MapUtil.getEventmap(line, eventMap);
        data.putAll(eventMap);
        data.put(FieldConstant.TIME_LOCAL,partition);
        return data;
    }
    
    public static Map<String,String> initMap01(String siteCode,String line,String partition)
    {
        Map<String, String> data = new HashMap<String, String>();
        data.put(Constant.SITE_CODE, siteCode);
        Map<String, String> eventMap = new LinkedHashMap<String, String>();
        MapUtil.getEventmap(line, eventMap);
        data.putAll(eventMap);
        data.put(FieldConstant.TIME_LOCAL,partition);
        return data;
    }
    
    public static Map<String,String> getFilterField(String fieldValue)
    {
        Map<String,String> dataMap=new HashMap<String,String>();
        
        return dataMap;
    }
    
    public static boolean filterCountry(Map<String, String> eventMap)
    {
        boolean flag = false;
        if (StringUtils.isNotEmpty(eventMap.get(Constant.GLB_CL)) && StringUtils.isNotEmpty(eventMap.get(Constant.SITE)))
        {
            String country = CommonUtil.getURLHostName(eventMap.get(Constant.GLB_CL)).split("\\.")[0];
            if (eventMap.get(Constant.SITE).contains(country))
            {
                flag = true;
            }
        }
        return flag;
    }
    
    public static String getCountry(Map<String, String> eventMap)
    {
        String country = "en";
        if (StringUtils.isNotEmpty(eventMap.get(Constant.GLB_CL)))
        {
            country = CommonUtil.getURLHostName(eventMap.get(Constant.GLB_CL)).split("\\.")[0];
        }
        return country;
    }

    
    public static void main(String[] args) throws Exception
    {
    }
}