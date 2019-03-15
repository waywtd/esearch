package com.globalegrow.esearch.hive.udf;

import com.globalegrow.esearch.util.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 * <pre>
 *
 *  File: com.globalegrow.esearch.hive.udf.TrimUDF
 *
 *  Copyright (c) TrimUDF, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/5/17				lizhaohui				Initial.
 *
 * <pre>
 */
public class TrimUDF extends UDF
{
    public String evaluate(Text t1)
    {
        String result = "";
        if (StringUtils.isNotEmpty(t1))
        {
            result=t1.toString().trim();
        }
        return result;
    }
}