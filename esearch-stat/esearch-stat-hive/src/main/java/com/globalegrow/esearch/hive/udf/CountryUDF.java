package com.globalegrow.esearch.hive.udf;

import com.globalegrow.esearch.util.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * <pre>
 *
 *  File: com.globalegrow.esearch.hive.udf.CountryUDF
 *
 *  Copyright (c) CountryUDF, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/5/16				lizhaohui				Initial.
 *
 * <pre>
 */

public class CountryUDF extends UDF
{
    public String evaluate(Text t1)
    {
        String result = "www";
        if (StringUtils.isNotEmpty(t1))
        {
            result=getURLCountry(t1.toString());
        }
        return result;
    }

    public String getURLCountry(String httpUrl)
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

    public static void main(String[] args)
    {
        CountryUDF udf=new CountryUDF();
        String str="https://it.zaful.com/";
        System.out.println(udf.evaluate(new Text(str)));
    }
}

