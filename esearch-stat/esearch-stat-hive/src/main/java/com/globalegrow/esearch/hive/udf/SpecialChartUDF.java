package com.globalegrow.esearch.hive.udf;

import com.globalegrow.esearch.util.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *
 *  File: com.globalegrow.esearch.hive.udf.SpecialChartUDF
 *
 *  Copyright (c) SpecialChartUDF, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/5/24				lizhaohui				Initial.
 *
 * <pre>
 */
public class SpecialChartUDF extends UDF
{
    public String evaluate(Text t1)
    {
        String result="";
        if (StringUtils.isNotEmpty(t1))
        {
            result= t1.toString().trim();
            /*String regEx = "[`~!@#$%^&*()\\-+={}':;,\\[\\].<>/?￥%…（）_+|【】‘；：”“’。，、？\\s\\uD83D\\uDD0B]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(result);
            result=m.replaceAll("").trim();*/
            result = removeFourChar(result);
            if(result.length()>95)
            {
                result = result.substring(0,95)+"...";
            }
        }
        return result;
    }

    public static String removeFourChar(String content){
        byte[] conbyte = content.getBytes();
        for(int i = 0;i<conbyte.length;i++){
            if((conbyte[i] & 0xF8) == 0xF0){
                for(int j = 0;j<4;j++){
                    conbyte[i+j]=0x30;
                }
                i += 3;
            }
        }
        content = new String(conbyte);
        return content.replaceAll("0000","");
    }

}
