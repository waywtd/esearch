package com.globalegrow.esearch.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 * <pre>
 *
 *  File: com.globalegrow.esearch.hive.udf.ConverSingleUDF
 *
 *  Copyright (c) ConverSingleUDF, globalegrow.com All Rights Reserved.
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
public class ConverSingleUDF extends UDF
{
    public String evaluate(Text t1)
    {
        String source = t1.toString();
        if (source.contains("\\x22")) {
            source = source.replace("\\x22", "\"");
        }
        return source;
    }
}
