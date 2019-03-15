package com.globalegrow.esearch.stat.event.mapred.widetable.mapper;

import com.globalegrow.esearch.constant.Constant;
import com.globalegrow.esearch.util.MapUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *
 *  File: WideTableMapperTest.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018年9月26日			nieruiqun				Initial.
 *
 * </pre>
 */
public class AppWideTableMapperTest {

    String line = "34.240.29.179^A^1538197200.668^A^GET^A^/_app.gif?re_targeting_conversion_type=&is_retargeting=false&app_id=com.globalegrow.app.gearbest&platform=android&event_type=in-app-event&attribution_type=regular&event_time=2018-09-29%2005%3A00%3A00&event_time_selected_timezone=2018-09-29%2013%3A00%3A00.061%2B0800&event_name=af_search&event_value=%7B%22af_content_type%22%3A%22xiaomi%20mi%20band%203%22%7D&currency=USD&selected_currency=USD&revenue_in_selected_currency=&cost_per_install=&click_time=2018-09-26%2023%3A35%3A04&click_time_selected_timezone=2018-09-27%2007%3A35%3A04.000%2B0800&install_time=2018-09-27%2015%3A28%3A53&install_time_selected_timezone=2018-09-27%2023%3A28%3A53.057%2B0800&agency=&campaign=&media_source=Facebook%20Ads&af_sub1=&af_sub2=&af_sub3=&af_sub4=&af_sub5=&af_siteid=&click_url=&fb_campaign_id=23842959015340379&fb_campaign_name=BR-Android-0817-Apparel&fb_adset_id=23842980609990379&fb_adset_name=09-04-06-Bags%26Shoes-Men%27s%20Shoes-BR-Android-Interest-PPL-M&fb_adgroup_id=23842980610000379&fb_adgroup_name=09-04-06-Bags%26Shoes-Men%27s%20Shoes-BR-Android-Interest-PPL-M&country_code=BR&city=Joinville&ip=177.204.44.212&wifi=true&language=portugu%C3%AAs&appsflyer_device_id=1538062126317-2732719626964212742&customer_user_id=6445063420091744256&android_id=6076b6f41bf4b0ab&imei=&advertising_id=5d8f4343-7344-4c4c-9e00-7adc55742431&mac=&device_brand=motorola&device_model=Moto%20G%20%285%29%20Plus&os_version=7.0&sdk_version=v4.8.11&app_version=3.8.0&operator=TIM&carrier=TIM&http_referrer=&app_name=GearBest%20Online%20Shopping&download_time=2018-09-27%2015%3A28%3A40&download_time_selected_timezone=2018-09-27%2023%3A28%3A40.000%2B0800&af_cost_currency=&af_cost_value=&af_cost_model=&af_c_id=&af_adset=&af_adset_id=&af_ad=&af_ad_id=&af_ad_type=instagram_stories&af_channel=Instagram&af_keywords=&bundle_id=com.globalegrow.app.gearbest&attributed_touch_type=impression&attributed_touch_time=2018-09-26%2023%3A35%3A04^A^200^A^372^A^http-kit/2.0^A^DUBLIN^A^IE^A^";

    @Test
    public void map() throws Exception {
        Text tValue = new Text();
        //根据空格将这一行切分成单词
        String[] strings = line.split(Constant.EVENTSEPERATE);
        for(String str : strings){
            if(str.contains("_app.gif?")){
                String info = StringUtils.substringAfter(str, "_app.gif?");
                if(StringUtils.isNotBlank(info)){
                    String [] infos = info.split("&");
                    Map<String, String> map = MapUtil.getAppWideTableMap();
                    for(String message : infos){
                        message = URLDecoder.decode(message, "UTF-8");
                        String mapKey = StringUtils.substringBefore(message,Constant.EQUAL_SIGN);
                        String mapValue = StringUtils.trim(StringUtils.substringAfter(message,Constant.EQUAL_SIGN));
                        map.put(mapKey, StringUtils.isBlank(mapValue) ? "NILL" : mapValue);
                    }
                    final List<String> list = new ArrayList<>(map.size());
                    System.out.println(map);
                    map.forEach( (k, v) ->  list.add(v) );
                    tValue.set(String.join(Constant.HIVE_SEPERATE, list));
                }
            }
        }
    }

}