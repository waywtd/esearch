package com.globalegrow.esearch.stat.event;

import java.text.MessageFormat;


/**
 * <pre>
 * 
 *  File: JsonTest.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2017年6月26日				lizhaohui				Initial.
 *
 * </pre>
 */
public class JsonTest
{

    public static void main(String[] args)
    {
        /*VersionDate version=new VersionDate(1,1498320000l);
        JSONObject json=(JSONObject) JSONObject.parse(version.toString());
        System.out.println(version);
        System.out.println(json.get("version"));
        System.out.println(json.get("date"));*/
        String str="/esearch/esearch-stat/data/hdp-ods/event/{0}/checkout/stmtr_check_out_daily";
        System.out.println(MessageFormat.format(str, "gearbest"));
        
    }
    
}