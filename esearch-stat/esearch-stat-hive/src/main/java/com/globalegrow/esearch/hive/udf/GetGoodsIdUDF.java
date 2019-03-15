package com.globalegrow.esearch.hive.udf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 * <pre>
 *
 *  File: com.globalegrow.esearch.hive.udf.GetGoodsIdUDF
 *
 *  Copyright (c) CountryUDF, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/07/25				nieruiqun				Initial.
 *
 * <pre>
 */
public class GetGoodsIdUDF extends UDF {

    public String evaluate(Text text) {
        String json = text.toString();
        try {
            Object object = JSON.parseObject(json, Object.class);
            if (object instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) object;
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String goodsSn = jsonObject.getString("sku");
                String whCode = jsonObject.getString("k");
                return goodsSn + "#" + whCode;
            } else if (object instanceof JSONObject){
                JSONObject jsonObject = (JSONObject) object;
                String goodsSn = jsonObject.getString("sku");
                String whCode = jsonObject.getString("k");
                return goodsSn + "#" + whCode;
            } else {
                return json;
            }
        } catch (Exception e) {
            return json;
        }
    }

}
