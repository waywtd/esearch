package com.globalegrow.esearch.hive.udf;

import com.alibaba.fastjson.JSONObject;
import com.globalegrow.esearch.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *
 *  File: com.globalegrow.esearch.hive.udf.GetGoodsIdUDF
 *
 *  Copyright (c) GetTypeUDF, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/10/13				nieruiqun				Initial.
 *
 * <pre>
 */
public class GetAddCartTypeUDF extends UDF {

    private static final String REGEX = "@@";
    final Pattern gearbestCatIdPattern = Pattern.compile("-c_[0-9]+");

    public String evaluate(Text text, Text ubcta, Text s, Text ubcd) {
        String url = text.toString();
        String info = ubcta.toString();
        String pageType = s.toString();
        String siteCode = ubcd.toString();
        String result = "other" + REGEX + "NILL" + REGEX + 0 + REGEX + "NILL" + REGEX + 1;
        try {
            if (StringUtils.isNotBlank(url) && StringUtils.isNotBlank(siteCode)) {
                String params = StringUtils.substringAfter(url, "?");
                url = StringUtils.substringBefore(url, "?");
                String page = CommonUtil.getPageNum(url, params);
                if ("10002".equals(siteCode)) {
                    result = getGearbestType(url, page, info, pageType);
                } else if ("10007".equals(siteCode)) {
                    result = getRosegalType(url, page);
                } else if ("10013".equals(siteCode)) {
                    result = getZafulType(url, page);
                }
            }
        } catch (Exception e) {}
        return result;
    }

    private String getGearbestType(String url, String page, String info, String pageType) {
        String result = "other" + REGEX + "NILL" + REGEX + 0 + REGEX + "NILL" + REGEX + 1;
        Matcher matcher = gearbestCatIdPattern.matcher(url);
        if (url.contains("-_gear")) {
            JSONObject json = parseObject(info);
            String keyword = (null == json) ? "" : json.getString("sckw");
            keyword = StringUtils.isNotBlank(keyword) ? keyword.toLowerCase().trim() : CommonUtil.keywordDecoder(StringUtils.substringBetween(url, ".com/", "-_gear").replace("-", " "));
            String catId = StringUtils.substringBetween(url, "c_", "/");
            catId = StringUtils.isNotBlank(catId) && StringUtils.isNumeric(catId) ? catId : "0";
            String searchType = StringUtils.isNotBlank(json.getString("sk")) ? json.getString("sk").trim() : "Z";
            result = "b02" + REGEX + keyword + REGEX + catId + REGEX + searchType + REGEX + page;
        } else if (matcher.find()) {
            String catId = matcher.group(0).replace("-c_", "");
            result = "b01" + REGEX + "NILL" + REGEX + catId + REGEX + "NILL" + REGEX + page;
        } else if (StringUtils.isNotBlank(pageType) && !"nill".equals(pageType.toLowerCase())) {
            result = pageType + REGEX + "NILL" + REGEX + 0 + REGEX + "NILL" + REGEX + page;
        } else if (url.contains("flash-sale.html")) {
            result = "b06" + REGEX + "NILL" + REGEX + 0 + REGEX + "NILL" + REGEX + page;
        } else if (url.contains("/zone-deals.html")) {
            result = "b16" + REGEX + "NILL" + REGEX + 0 + REGEX + "NILL" + REGEX + page;
        } else if (url.contains("/clearance.html")){
            result = "b05" + REGEX + "NILL" + REGEX + 0 + REGEX + "NILL" + REGEX + page;
        } else if (url.contains("/gadget-deals.html")) {
            result = "b09" + REGEX + "NILL" + REGEX + 0 + REGEX + "NILL" + REGEX + page;
        } else if (url.contains("/promotion-")) {
            result = "b03" + REGEX + "NILL" + REGEX + 0 + REGEX + "NILL" + REGEX + page;
        } else if (url.contains("/new-arrival.html")) {
            result = "new-arrival" + REGEX + "NILL" + REGEX + 0 + REGEX + "NILL" + REGEX + page;
        } else if (url.contains("/presale.html")) {
            result = "b08" + REGEX + "NILL" + REGEX + 0 + REGEX + "NILL" + REGEX + page;
        } else if (url.contains("/new-products")) {
            result = "b04" + REGEX + "NILL" + REGEX + 0 + REGEX + "NILL" + REGEX + page;
        } else if (url.contains("/explore/")) {
            result = "b11" + REGEX + "NILL" + REGEX + 0 + REGEX + "NILL" + REGEX + page;
        } else if (url.contains("/new-user.html")) {
            result = "newUser" + REGEX + "NILL" + REGEX + 0 + REGEX + "NILL" + REGEX + page;
        } else if (url.contains("/cart/")) {
            result = "d01" + REGEX + "NILL" + REGEX + 0 + REGEX + "NILL" + REGEX + page;
        } else if (url.contains("user.gearbest.com") || url.contains("userm.gearbest.com")) {
            result = "e" + REGEX + "NILL" + REGEX + 0 + REGEX + "NILL" + REGEX + page;
        } else if (url.contains("/pp_")) {
            result = "c" + REGEX + "NILL" + REGEX + 0 + REGEX + "NILL" + REGEX + page;
        } else if (url.contains("/activity")) {
            result = "activity" + REGEX + "NILL" + REGEX + 0 + REGEX + "NILL" + REGEX + page;
        } else if (url.contains("/brand")) {
            result = "b07" + REGEX + "NILL" + REGEX + 0 + REGEX + "NILL" + REGEX + page;
        } else if (url.contains("gearbest")) {
            result = "gearbest" + REGEX + "NILL" + REGEX + 0 + REGEX + "NILL" + REGEX + page;
        } else if (url.contains("http")) {
            result = "external" + REGEX + StringUtils.substringBetween(url, "://", "/") + REGEX + 0 + REGEX + "NILL" + REGEX + 1;
        } else if (url.contains("android-app")) {
            result = "android-app" + REGEX + "NILL" + REGEX + 0 + REGEX + "NILL" + REGEX + 1;
        } else if ("NILL".equals(url)) {
            result = "nill" + REGEX + "NILL" + REGEX + 0 + REGEX + "NILL" + REGEX + 1;
        }
        return  result;
    }

    private String getRosegalType(String url, String page) {
        String result = "other" + REGEX + "NILL" + REGEX + "0" + REGEX + "1";

        return  result;
    }

    private String getZafulType(String url, String page) {
        String result = "other" + REGEX + "NILL" + REGEX + "0" + REGEX + "1";

        return  result;
    }

    private JSONObject parseObject(String content){
        try{
            return JSONObject.parseObject(content);
        } catch (Exception e) {
            return null;
        }
    }

}
