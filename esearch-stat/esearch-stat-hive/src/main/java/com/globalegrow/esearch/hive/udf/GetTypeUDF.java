package com.globalegrow.esearch.hive.udf;

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
public class GetTypeUDF extends UDF {

    private static final String REGEX = "@@";
    final Pattern gearbestCatIdPattern = Pattern.compile("-c_[0-9]+");

    public String evaluate(Text text) {
        String url = text.toString();
        String result = "other" + REGEX + "NILL" + REGEX + "0" + REGEX + "0" + REGEX + "1";
        try {
            if(StringUtils.isNotBlank(url)){
                String params = StringUtils.substringAfter(url, "?");
                url = StringUtils.substringBefore(url,"?");
                String page = CommonUtil.getPageNum(url, params);
                String sort = CommonUtil.getSort(params);
                result = getGearbestType(url, page, sort);
            }
        } catch (Exception e) {}
        return result;
    }

    private String getGearbestType(String url, String page, String sort) {
        String result = "other" + REGEX + "NILL" + REGEX + "0" + REGEX + sort + REGEX + page;
        Matcher matcher = gearbestCatIdPattern.matcher(url);
        if(url.contains("-_gear")){
            String keyword = StringUtils.substringBetween(url, ".com/", "-_gear").replace("-", " ");
            String catId = StringUtils.substringBetween(url, "c_", "/");
            catId = StringUtils.isNotBlank(catId) && StringUtils.isNumeric(catId) ? catId : "0";
            result = "b02" + REGEX + CommonUtil.keywordDecoder(keyword) + REGEX + catId + REGEX + sort + REGEX + page;
        } else if (matcher.find()) {
            String catId = matcher.group(0).replace("-c_","");
            result = "b01" + REGEX + "NILL" + REGEX + catId + REGEX + sort + REGEX + page;
        } else if (url.endsWith("gearbest.com/") || url.contains("gearbest.com/tab")) {
            result = "a" + REGEX + "NILL" + REGEX + "0" + REGEX + sort + REGEX + page;
        } else if (url.contains("flash-sale.html")) {
            result = "b06" + REGEX + "NILL" + REGEX + "0" + REGEX + sort + REGEX + page;
        } else if (url.contains("/zone-deals.html")) {
            result = "b16" + REGEX + "NILL" + REGEX + "0" + REGEX + sort + REGEX + page;
        } else if (url.contains("/clearance.html")){
            result = "b05" + REGEX + "NILL" + REGEX + "0" + REGEX + sort + REGEX + page;
        } else if (url.contains("/gadget-deals.html")) {
            result = "b09" + REGEX + "NILL" + REGEX + "0" + REGEX + sort + REGEX + page;
        } else if (url.contains("/promotion-")) {
            result = "b03" + REGEX + "NILL" + REGEX + "0" + REGEX + sort + REGEX + page;
        } else if (url.contains("/new-arrival.html")) {
            result = "new" + REGEX + "NILL" + REGEX + "0" + REGEX + sort + REGEX + page;
        } else if (url.contains("/presale.html")) {
            result = "b08" + REGEX + "NILL" + REGEX + "0" + REGEX + sort + REGEX + page;
        } else if (url.contains("/new-products")) {
            result = "b04" + REGEX + "NILL" + REGEX + "0" + REGEX + sort + REGEX + page;
        } else if (url.contains("/explore/")) {
            result = "b11" + REGEX + "NILL" + REGEX + "0" + REGEX + sort + REGEX + page;
        } else if (url.contains("/new-user.html")) {
            result = "newUser" + REGEX + "NILL" + REGEX + "0" + REGEX + sort + REGEX + page;
        } else if (url.contains("/cart/")) {
            result = "d01" + REGEX + "NILL" + REGEX + "0" + REGEX + sort + REGEX + page;
        } else if (url.contains("user.gearbest.com") || url.contains("userm.gearbest.com")) {
            result = "e" + REGEX + "NILL" + REGEX + "0" + REGEX + sort + REGEX + page;
        } else if (url.contains("/pp_")) {
            result = "c" + REGEX + "NILL" + REGEX + "0" + REGEX + sort + REGEX + page;
        } else if (url.contains("welcome.html")) {
            result = "welcome" + REGEX + "NILL" + REGEX + "0" + REGEX + sort + REGEX + page;
        } else if (url.contains("/activity")) {
            result = "activity" + REGEX + "NILL" + REGEX + "0" + REGEX + sort + REGEX + page;
        } else if (url.contains("/brand")) {
            result = "b07" + REGEX + "NILL" + REGEX + "0" + REGEX + sort + REGEX + page;
        }
        return  result;
    }

}
