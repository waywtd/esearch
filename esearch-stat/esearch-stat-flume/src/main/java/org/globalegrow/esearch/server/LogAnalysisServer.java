package org.globalegrow.esearch.server;

import com.alibaba.fastjson.JSONObject;
import org.globalegrow.esearch.constant.Constants;
import org.globalegrow.esearch.constant.NginxFieldConstant;
import org.globalegrow.esearch.constant.PatternConstants;
import org.globalegrow.esearch.constant.TimeZoneConstants;
import org.globalegrow.esearch.utils.CommonUtil;
import org.globalegrow.esearch.utils.DateUtil;
import org.globalegrow.esearch.utils.StringUtils;
import org.globalegrow.esearch.utils.TimeZoneUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *
 *  File: LogAnalysisServer.java
 *
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,                   Who,                    What;
 *  2017年11月07日              lizhaohui               Initial.
 *
 * </pre>
 */
public class LogAnalysisServer
{

    private static final Logger LOGGER = LoggerFactory.getLogger(LogAnalysisServer.class);
    private static final Set<String> IP_CACHE = new HashSet<>();

    private static SimpleDateFormat bjSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Pattern PATTERN = Pattern.compile("\\&ip=[0-9\\.]+\\&");
    private static SimpleDateFormat londonSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static {
        bjSdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        londonSdf.setTimeZone(TimeZone.getTimeZone("Europe/WET"));

        // 公司出口IP 国内
        IP_CACHE.add("218.18.158.248");
        IP_CACHE.add("123.58.32.16");
        IP_CACHE.add("123.58.32.28");
        IP_CACHE.add("123.58.48.210");
        IP_CACHE.add("123.58.48.214");
        IP_CACHE.add("123.58.46.0");
        IP_CACHE.add("123.58.46.28");
        IP_CACHE.add("120.132.112.210");
        IP_CACHE.add("120.132.112.214");
        IP_CACHE.add("210.21.233.224");
        IP_CACHE.add("210.21.233.226");
        IP_CACHE.add("210.21.233.227");
        IP_CACHE.add("210.21.233.228");
        IP_CACHE.add("113.106.73.226");
        IP_CACHE.add("113.106.73.224");
        IP_CACHE.add("113.106.73.227");
        IP_CACHE.add("113.106.73.228");
        IP_CACHE.add("113.106.73.229");
        IP_CACHE.add("113.106.73.230");
        IP_CACHE.add("58.250.17.62");
        IP_CACHE.add("112.95.165.176");
        IP_CACHE.add("112.95.165.177");
        IP_CACHE.add("112.95.165.178");
        IP_CACHE.add("112.95.165.179");
        IP_CACHE.add("112.95.165.180");
        IP_CACHE.add("112.95.165.181");
        IP_CACHE.add("112.95.165.182");
        IP_CACHE.add("218.26.254.241");

        // 公司出口IP 国外
        IP_CACHE.add("103.251.36.216");
        IP_CACHE.add("137.59.103.142");
        IP_CACHE.add("150.242.228.126");
        IP_CACHE.add("150.242.230.194");
        IP_CACHE.add("43.231.187.5");
        IP_CACHE.add("43.231.187.6");
        IP_CACHE.add("114.113.243.226");
        IP_CACHE.add("114.113.243.227");
        IP_CACHE.add("114.113.243.228");
        IP_CACHE.add("114.113.243.86");
        IP_CACHE.add("103.206.188.9");
        IP_CACHE.add("103.206.188.202");
        IP_CACHE.add("43.229.116.33");
        IP_CACHE.add("103.91.158.89");
        IP_CACHE.add("103.91.158.232");
    }

    /**
     * 正则获取站点编号
     * @param jsonObject
     * @return
     */
    public static String getUbcd(JSONObject jsonObject) {
        StringBuilder builder = new StringBuilder();
        if(NginxFieldConstant.MESSAGE_TYPE_NGINX.equals(jsonObject.getString(NginxFieldConstant.MESSAGE_REQPATH))) {
            String ubcdKeyValue = CommonUtil.getValueByPattern(PatternConstants.UBCD_PATTREN, jsonObject.getString(NginxFieldConstant.FULL_REQUEST));
            if (StringUtils.isNotEmpty(ubcdKeyValue)) {
                builder.append(ubcdKeyValue.split(Constants.EQUAL_SIGN)[1]);
            }
        } else if(NginxFieldConstant.MESSAGE_TYPE_APP.equals(jsonObject.getString(NginxFieldConstant.MESSAGE_REQPATH))){
            //&app_name=Zaful%20-%20Your%20Way%20to%20Save%20on%20Black%20Friday&
            builder.append(CommonUtil.getValueByField("app_name=",jsonObject.getString(NginxFieldConstant.FULL_REQUEST)));
        }
        return builder.toString();
    }

    /**
     * 重置head参数
     * @param jsonObject
     * @return
     */
    public static Map<String,String> restHeader(JSONObject jsonObject,String domain) {
        Map<String,String> headerMap=new HashMap<>();
        String dateStr=jsonObject.getString(NginxFieldConstant.LOG_TIME);
        Date date= DateUtil.parseStrToDate(TimeZoneUtils.timeConvert(dateStr, TimeZoneConstants.GMT0, TimeZoneConstants.Asia_Shanghai),DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
        headerMap.put(Constants.YEAR, DateUtil.getYear(date).toString());
        headerMap.put(Constants.MONTH, DateUtil.getMonth(date));
        headerMap.put(Constants.DAY, DateUtil.getDay(date));
        headerMap.put(Constants.DOMAIN, domain);
        String messageType = jsonObject.getString(NginxFieldConstant.MESSAGE_REQPATH);
        String ip =  jsonObject.getString(NginxFieldConstant.MESSAGE_CLIIP);
        if(NginxFieldConstant.MESSAGE_TYPE_APP.equals(messageType)) {
            String line = jsonObject.getString(NginxFieldConstant.FULL_REQUEST);
            Matcher matcher = PATTERN.matcher(line);
            if(matcher.find()) {
                ip = matcher.group().replace("&ip=","").replace("&","");
            }
        }

        String logType = "other";
        if(!IP_CACHE.contains(ip) && NginxFieldConstant.MESSAGE_TYPE_NGINX.equals(messageType)) {
            logType = "nginx";
        } else if(!IP_CACHE.contains(ip) && NginxFieldConstant.MESSAGE_TYPE_APP.equals(messageType)) {
            logType = "app";
        } else if(IP_CACHE.contains(ip) && NginxFieldConstant.MESSAGE_TYPE_NGINX.equals(messageType)) {
            logType = "inner-nginx";
        } else if(IP_CACHE.contains(ip) && NginxFieldConstant.MESSAGE_TYPE_APP.equals(messageType)) {
            logType = "inner-app";
        }
        headerMap.put(Constants.LOG_TYPE,logType);
        return headerMap;
    }

    /**
     * 来源于cloud_monitor监控平台的数据
     * 域名为s.logsss.com
     * 两个条件符合正确的行为埋点数据
     * @param json
     * @return
     */
    private static boolean initLog(JSONObject json) {
        return  StringUtils.isNotEmpty(json) && Constants.S_LOGSSS_COM.equals(json.getString(NginxFieldConstant.MESSAGE_REQHOST))
                && Constants.CLOUD_MONITOR.equals(json.getString(NginxFieldConstant.TYPE));
    }

    /**
     * 拼接json格式为行为数据 message_reqmethod full_request HTTP/1.1
     * @param json
     * @return
     */
    private static String constructReqParams(JSONObject json) {
        StringBuilder builder = new StringBuilder();
        if(initLog(json)) {
            builder.append(json.getString(NginxFieldConstant.FULL_REQUEST));
        }
        return builder.toString();
    }

    public static String constructLogMsg(JSONObject jsonObject) {
        StringBuilder builder=new StringBuilder();
        if(initLog(jsonObject)) {
            builder.append(jsonObject.getString(NginxFieldConstant.MESSAGE_CLIIP)==null?"-":jsonObject.getString(NginxFieldConstant.MESSAGE_CLIIP))
                    .append(Constants.NGINX_SPLIT_SYMBOL)
                    .append(jsonObject.getString(NginxFieldConstant.START) == null? "-" : jsonObject.getString(NginxFieldConstant.START))
                    .append(Constants.NGINX_SPLIT_SYMBOL)
                    .append(jsonObject.getString(NginxFieldConstant.MESSAGE_REQMETHOD) == null? "-" : jsonObject.getString(NginxFieldConstant.MESSAGE_REQMETHOD))
                    .append(Constants.NGINX_SPLIT_SYMBOL)
                    .append(LogAnalysisServer.constructReqParams(jsonObject).length() == 0?"-":LogAnalysisServer.constructReqParams(jsonObject))
                    .append(Constants.NGINX_SPLIT_SYMBOL)
                    .append(jsonObject.getString(NginxFieldConstant.MESSAGE_STATUS)==null?"-":jsonObject.getString(NginxFieldConstant.MESSAGE_STATUS))
                    .append(Constants.NGINX_SPLIT_SYMBOL)
                    .append(jsonObject.getString(NginxFieldConstant.MESSAGE_BYTES)==null?"-":jsonObject.getString(NginxFieldConstant.MESSAGE_BYTES))
                    .append(Constants.NGINX_SPLIT_SYMBOL)
                    .append(jsonObject.getString(NginxFieldConstant.MESSAGE_UA)==null?"-":jsonObject.getString(NginxFieldConstant.MESSAGE_UA))
                    .append(Constants.NGINX_SPLIT_SYMBOL)
                    .append(jsonObject.getString(NginxFieldConstant.GEO_CITY)==null?"-":jsonObject.getString(NginxFieldConstant.GEO_CITY))
                    .append(Constants.NGINX_SPLIT_SYMBOL)
                    .append(jsonObject.getString(NginxFieldConstant.GEO_COUNTRY)==null?"-":jsonObject.getString(NginxFieldConstant.GEO_COUNTRY))
                    .append(Constants.NGINX_SPLIT_SYMBOL);
        }
        return builder.toString();
    }

    public static String constructKafkaLogMsg(JSONObject jsonObject) throws UnsupportedEncodingException, ParseException {
        StringBuilder builder=new StringBuilder();
        if(initLog(jsonObject))
        {
            Map<String, String> resultMap = initMessageMap(jsonObject);
            Set<String> keys = jsonObject.keySet();
            for(String key : keys) {
                Object value = jsonObject.get(key);
                if(key == "full_request") {
                    try {
                        value = URLDecoder.decode(convertNull(value), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                    }
                    String str = convertNull(value);
                    if (str.contains("_ubc.gif?")) {
                        value = str.substring(str.indexOf("_ubc.gif?") + "_ubc.gif?".length());
                        String[] split = value.toString().split("&");
                        for (String queryStr : split) {
                            if (!queryStr.contains("=")) continue;
                            String queryKey = org.apache.commons.lang3.StringUtils.substringBefore(queryStr, "=");
                            String queryValue = org.apache.commons.lang3.StringUtils.substringAfter(queryStr, "=");
                            if (resultMap.containsKey(queryKey)) {
                                resultMap.put(queryKey, convertNull(queryValue));
                            }
                            try {
                                disposeSomeAttr(resultMap, queryKey, queryValue);
                            } catch (Exception e) {
                                LOGGER.debug("DisposeSomeAttr ERROR: ", e);
                            }
                        }
                    }
                } else {
                    if(NginxFieldConstant.LOG_TIME.equals(key)) {
                        String bjDate = bjSdf.format(londonSdf.parse(value.toString()));
                        resultMap.put(NginxFieldConstant.LOG_TIME, bjDate);
                        resultMap.put(NginxFieldConstant.LOG_TIME_SRC, value.toString());
                    }else if(resultMap.containsKey(key)) {
                        resultMap.put(key, convertNull(value));
                    }
                }
            }
            builder.append(JSONObject.toJSONString(resultMap));
        }
        return builder.toString();
    }

    private static void disposeSomeAttr(Map<String,String> resultMap, String queryKey, String queryValue) {
        if(queryKey.equals("glb_ubcta")) {
            Object sckw = JSONObject.parseObject(queryValue).getString("sckw");
            resultMap.put("glb_ubcta_sckw", convertNull(sckw));
        } else if (queryKey.equals("glb_skuinfo")) {
            String skuinfoSku = JSONObject.parseObject(queryValue).getString("sku");
            resultMap.put("glb_skuinfo_sku", convertNull(skuinfoSku));
        } else if (queryKey.equals("glb_filter")) {
            JSONObject jsonObject = JSONObject.parseObject(queryValue);
            resultMap.put("glb_filter_view", convertNull(jsonObject.getString("view")));
            resultMap.put("glb_filter_sort", convertNull(jsonObject.getString("sort")));
            resultMap.put("glb_filter_page", convertNull(jsonObject.getString("page")));
        } else if (queryKey.equals("glb_cl")) {
            String searchWord2 = CommonUtil.getValueByPattern(PatternConstants.GEARBEST_CL_SEARCH_WORD_PATTREN,queryValue);
            if(searchWord2 != null) {
                String searchWord = searchWord2.substring(0, searchWord2.length() - 6);
                resultMap.put("glb_cl_search_word", convertNull(searchWord));
            }
            String catId = CommonUtil.getValueByPattern(PatternConstants.GEARBEST_CL_CAT_ID_PATTREN,queryValue);
            if (catId != null) {
                resultMap.put("glb_cl_cat_id", convertNull(catId.substring(2)));
            }
        }
    }

    private static String convertNull(Object obj) {
        String str = null;
        if(obj != null) str = obj.toString();
        return StringUtils.nullToSpecial(str, Constants.DEFAULT_NULL_STRING);
    }

    public static Map<String,String> initMessageMap(JSONObject jsonObject) {
        Map<String,String> initMap = new LinkedHashMap<>();
        initMap.put(NginxFieldConstant.MESSAGE_CLIIP, Constants.DEFAULT_NULL_STRING);
        initMap.put(NginxFieldConstant.LOG_TIME, Constants.DEFAULT_NULL_STRING);
        initMap.put(NginxFieldConstant.MESSAGE_STATUS, Constants.DEFAULT_NULL_STRING);
        putRequestParam(initMap);
        initMap.put(NginxFieldConstant.MESSAGE_BYTES, Constants.DEFAULT_NULL_STRING);
        initMap.put(NginxFieldConstant.REQHDR_REFERER, Constants.DEFAULT_NULL_STRING);
        initMap.put(NginxFieldConstant.MESSAGE_REQHOST, Constants.DEFAULT_NULL_STRING);
        initMap.put(NginxFieldConstant.NETWORK_EDGEIP, Constants.DEFAULT_NULL_STRING);
        initMap.put(NginxFieldConstant.NETPERF_EDGEIP, Constants.DEFAULT_NULL_STRING);
        initMap.put(NginxFieldConstant.GEO_CITY, Constants.DEFAULT_NULL_STRING);
        initMap.put(NginxFieldConstant.GEO_COUNTRY, Constants.DEFAULT_NULL_STRING);
        return initMap;
    }

    private static void putRequestParam(Map<String,String> initMap) {
        initMap.put("glb_oi", Constants.DEFAULT_NULL_STRING);
        // USER_ID
        initMap.put("glb_u", Constants.DEFAULT_NULL_STRING);
        // 内部来源页面url
        initMap.put("glb_pl", Constants.DEFAULT_NULL_STRING);
        // 站点标识
        initMap.put("glb_d", Constants.DEFAULT_NULL_STRING);
        //页面大类
        initMap.put("glb_b", Constants.DEFAULT_NULL_STRING);
        //页面小类
        initMap.put("glb_s", Constants.DEFAULT_NULL_STRING);
        // 页面编码
        initMap.put("glb_p", Constants.DEFAULT_NULL_STRING);
        // 行为类型
        initMap.put("glb_t", Constants.DEFAULT_NULL_STRING);
        // 子时间属性
        initMap.put("glb_ubcta", Constants.DEFAULT_NULL_STRING);
        initMap.put("glb_ubcta_sckw", Constants.DEFAULT_NULL_STRING);
        // 子事件详情
        initMap.put("glb_x", Constants.DEFAULT_NULL_STRING);
        //当前时间戳
        initMap.put("glb_tm", Constants.DEFAULT_NULL_STRING);
        //页面停留时间
        initMap.put("glb_w", Constants.DEFAULT_NULL_STRING);
        // 当前页面url
        initMap.put("glb_cl", Constants.DEFAULT_NULL_STRING);
        initMap.put("glb_cl_cat_id", Constants.DEFAULT_NULL_STRING);
        initMap.put("glb_cl_search_word", Constants.DEFAULT_NULL_STRING);
        // 国家编码
        initMap.put("glb_dc", Constants.DEFAULT_NULL_STRING);

        //商品详情
        initMap.put("glb_skuinfo", Constants.DEFAULT_NULL_STRING);
        initMap.put("glb_skuinfo_sku", Constants.DEFAULT_NULL_STRING);

        //页面模块
        initMap.put("glb_pm", Constants.DEFAULT_NULL_STRING);

        //搜索结果数量
        initMap.put("glb_at", Constants.DEFAULT_NULL_STRING);
        // 搜索分类
        initMap.put("glb_sc", Constants.DEFAULT_NULL_STRING);
        // 搜索结果词
        initMap.put("glb_sckw", Constants.DEFAULT_NULL_STRING);
        // 搜索输入词
        initMap.put("glb_siws", Constants.DEFAULT_NULL_STRING);
        // 搜索类型
        initMap.put("glb_sk", Constants.DEFAULT_NULL_STRING);
        // 搜索位置
        initMap.put("glb_sl", Constants.DEFAULT_NULL_STRING);

        // 页面sku
        initMap.put("glb_ksku", Constants.DEFAULT_NULL_STRING);

        // 仓库信息
        initMap.put("glb_k", Constants.DEFAULT_NULL_STRING);

        // 曝光数据集合
        initMap.put("glb_bv", Constants.DEFAULT_NULL_STRING);
        // 商品曝光数据
        initMap.put("glb_bd", Constants.DEFAULT_NULL_STRING);

        // cookie ID
        initMap.put("glb_od", Constants.DEFAULT_NULL_STRING);
        // 外部来源和着陆页详情url组合
        initMap.put("glb_osr", Constants.DEFAULT_NULL_STRING);
        // 外部来源||着陆页url详情
        initMap.put("glb_ol", Constants.DEFAULT_NULL_STRING);
        // linkId
        initMap.put("glb_olk", Constants.DEFAULT_NULL_STRING);

        initMap.put("glb_filter", Constants.DEFAULT_NULL_STRING);
        initMap.put("glb_filter_view", Constants.DEFAULT_NULL_STRING);
        initMap.put("glb_filter_sort", Constants.DEFAULT_NULL_STRING);
        initMap.put("glb_filter_page", Constants.DEFAULT_NULL_STRING);

        initMap.put("glb_plf", Constants.DEFAULT_NULL_STRING);

        initMap.put("osr_landing", Constants.DEFAULT_NULL_STRING);

        initMap.put("osr_referrer", Constants.DEFAULT_NULL_STRING);

        initMap.put("other", Constants.DEFAULT_NULL_STRING);
    }

}