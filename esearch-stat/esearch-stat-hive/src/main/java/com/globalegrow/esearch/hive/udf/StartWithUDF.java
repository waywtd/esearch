package com.globalegrow.esearch.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import com.globalegrow.esearch.util.StringUtils;

/**
 * <pre>
 * 
 *  File: StartWithUDF.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2017年9月11日				lizhaohui				Initial.
 *
 * </pre>
 */
public class StartWithUDF extends UDF
{
    public boolean evaluate(Text t1, Text t2)
    {
        boolean flag = false;
        if (StringUtils.isNotEmpty(t1) || StringUtils.isNotEmpty(t2))
        {
            if (t1.toString().startsWith(t2.toString()))
            {
                flag = true;
            }
        }
        return flag;
    }
}