package com.globalegrow.esearch.util;

import com.globalegrow.esearch.constant.Constant;

import java.util.LinkedHashMap;
import java.util.Map;

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
public class MapUtil
{

    /**
     * 
     * @Title: getEventmap
     * @Description: 拆分字段
     * @param @param
     *        event
     * @param @param
     *        paramMap
     * @param @return
     *        参数说明
     * @return Map<String,String> 返回类型
     */
    public static Map<String, String> getEventmap(String event, Map<String, String> paramMap)
    {

        String[] array = event.split(Constant.EVENTSEPERATE);

        if (array.length >= 12)
        {
            paramMap.put("remote_addr", array[0].trim());
            paramMap.put("remote_user", array[1].trim());
            paramMap.put("time_local", array[2].trim());
            paramMap.put("request", array[3].substring(15, array[3].length() - 10).trim());
            paramMap.put("status", array[4].trim());
            paramMap.put("body_bytes_sent", array[5].trim());
            paramMap.put("http_referer", array[6].trim());
            paramMap.put("http_user_agent", array[7].trim());
            paramMap.put("http_x_forwarded_for", array[8].trim());
            paramMap.put("http_true_client_ip", array[9].trim());
            paramMap.put("geoip_city_country_code", array[10].trim());
            paramMap.put("geoip_country_name", array[11].trim());
        }
        return paramMap;
    }

    public static Map<String, String> getWideTableMap()
    {
        Map<String, String> map =  new LinkedHashMap<>();
        // session ID
        map.put("glb_oi", "NILL");
        // USER_ID
        map.put("glb_u", "NILL");
        // 内部来源页面url
        map.put("glb_pl", "NILL");
        // 站点标识
        map.put("glb_d", "NILL");
        //页面大类
        map.put("glb_b", "NILL");
        //页面小类
        map.put("glb_s", "NILL");
        // 页面编码
        map.put("glb_p", "NILL");
        // 行为类型
        map.put("glb_t", "NILL");
        // 子时间属性
        map.put("glb_ubcta", "NILL");
        // 子事件详情
        map.put("glb_x", "NILL");
        //当前时间戳
        map.put("glb_tm", "NILL");
        //页面停留时间
        map.put("glb_w", "NILL");
        // 当前页面url
        map.put("glb_cl", "NILL");
        // 国家编码
        map.put("glb_dc", "NILL");

        //商品详情
        map.put("glb_skuinfo", "NILL");

        //页面模块
        map.put("glb_pm", "NILL");

        //搜索结果数量
        map.put("glb_at", "NILL");
        // 搜索分类
        map.put("glb_sc", "NILL");
        // 搜索结果词
        map.put("glb_sckw", "NILL");
        // 搜索输入词
        map.put("glb_siws", "NILL");
        // 搜索类型
        map.put("glb_sk", "NILL");
        // 搜索位置
        map.put("glb_sl", "NILL");

        // 页面sku
        map.put("glb_ksku", "NILL");

        // 仓库信息
        map.put("glb_k", "NILL");

        // 曝光数据集合
        map.put("glb_bv", "NILL");
        // 商品曝光数据
        map.put("glb_bd", "NILL");

        // cookie ID
        map.put("glb_od", "NILL");
        // 外部来源和着陆页详情url组合
        map.put("glb_osr", "NILL");
        // 外部来源||着陆页url详情
        map.put("glb_ol", "NILL");
        // linkId
        map.put("glb_olk", "NILL");

        map.put("glb_filter", "NILL");

        map.put("glb_plf", "NILL");

        map.put("glb_osr_landing", "NILL");

        map.put("glb_osr_referrer", "NILL");

        map.put("ip", "NILL");
        map.put("city", "NILL");
        map.put("country", "NILL");

        map.put("glb_bts", "NILL");

        map.put("other", "NILL");

        return map;
    }
    //App的宽表的
    public static Map<String, String> getAppWideTableMap() {

        Map<String, String> map =  new LinkedHashMap<>();

        // 广告触及类型，表示用户是通过什么样的广告归因方式激活的 (click、impression)
        map.put("attributed_touch_type", "NILL");
        // 用户第一次归因时间，触点时间（UTC），等同点击时间
        map.put("attributed_touch_time", "NILL");

        // 事件类型 in-app-event
        map.put("event_type", "NILL");
        // 归因类型 推送organic 或regular（代表非自然）
        map.put("attribution_type", "NILL");
        // 事件时间 (UTC)
        map.put("event_time", "NILL");
        // 事件时间 (后台所选择时区)
        map.put("event_time_selected_timezone", "NILL");
        // 事件名称
        map.put("event_name", "NILL");
        // 事件的赋值
        map.put("event_value", "NILL");
        // 币种（SDK 上报币种）
        map.put("currency", "NILL");
        // 币种（后台选择币种）
        map.put("selected_currency", "NILL");

        // 语言
        map.put("language", "NILL");
        // Appsflyer id，第三方给用户的唯一标识
        map.put("appsflyer_device_id", "NILL");
        // user_id
        map.put("customer_user_id", "NILL");

        map.put("country_code", "NILL");
        map.put("city", "NILL");
        map.put("ip", "NILL");

        // app id（包名）
        map.put("app_id", "NILL");
        // app 名称
        map.put("app_name", "NILL");
        // 操作系统
        map.put("platform", "NILL");

        // 用户再营销的转化类型
        map.put("re_targeting_conversion_type", "NILL");
        // 是否为再营销
        map.put("is_retargeting", "NILL");

        // 收益（以后台所选择币种来计算，XXX 为币种ISO 代号）
        map.put("revenue_in_selected_currency", "NILL");
        // 安装的成本
        map.put("cost_per_install", "NILL");

        // 点击时间（UTC）
        map.put("click_time", "NILL");
        // 点击时间（后台所选择时区）
        map.put("click_time_selected_timezone", "NILL");

        // 激活时间（UTC）
        map.put("install_time", "NILL");
        // 激活时间（后台所选择时区）
        map.put("install_time_selected_timezone", "NILL");
        // 代为投放的代理商名称
        map.put("agency", "NILL");
        // 媒体渠道
        map.put("media_source", "NILL");
        // 媒体渠道类型
        map.put("af_channel", "NILL");
        // 关键字
        map.put("af_keywords", "NILL");
        // 广告系列
        map.put("campaign", "NILL");
        // 广告系列id
        map.put("af_c_id", "NILL");
        // 广告组
        map.put("af_adset", "NILL");
        // 广告组ID
        map.put("af_adset_id", "NILL");
        // 广告
        map.put("af_ad", "NILL");
        // 广告id
        map.put("af_ad_id", "NILL");

        // Facebook
        map.put("fb_campaign_id", "NILL");
        map.put("fb_campaign_name", "NILL");
        map.put("fb_adset_name", "NILL");
        map.put("fb_adset_id", "NILL");
        map.put("fb_adgroup_id", "NILL");
        map.put("fb_adgroup_name", "NILL");

        // true or false
        map.put("wifi", "NILL");

        // android_id
        map.put("android_id", "NILL");
        // Android手机串号，手机序列号，唯一
        map.put("imei", "NILL");
        // ios手机串号，手机序列号，唯一
        map.put("idfa", "NILL");
        // Android Advertising id
        map.put("advertising_id", "NILL");
        // mac 地址
        map.put("mac", "NILL");
        // 设备厂牌
        map.put("device_brand", "NILL");
        // 设备型号
        map.put("device_model", "NILL");
        // 操作系统版本
        map.put("os_version", "NILL");
        // Appsflyer SDK 版本
        map.put("sdk_version", "NILL");
        // app 版本
        map.put("app_version", "NILL");
        // 运营商
        map.put("operator", "NILL");
        // 运营商
        map.put("carrier", "NILL");

        // 成本计价模式
        map.put("af_cost_model","NILL");
        map.put("af_cost_value","NILL");
        map.put("af_cost_currency","NILL");
        map.put("cost_in_selected_currency","NILL");
        map.put("af_siteid", "NILL");

        map.put("af_sub1", "NILL");
        map.put("af_sub2", "NILL");
        map.put("af_sub3", "NILL");
        map.put("af_sub4", "NILL");
        map.put("af_sub5", "NILL");
        // 点击的URL
        map.put("click_url", "NILL");

        map.put("http_referrer", "NILL");

        // 下载时间（UTC）
        map.put("download_time", "NILL");
        // 下载时间（后台所选择时区）
        map.put("download_time_selected_timezone", "NILL");
        // 苹果给每一个用户的ID
        map.put("bundle_id", "NILL");

        return map;
    }

}
