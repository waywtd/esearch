package com.globalegrow.esearch.stat.util;

import org.apache.hadoop.conf.Configuration;

/**
 * <pre>
 * 
 *  File: HadoopUtil.java
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
public class HadoopUtil
{

    private static Configuration conf = null;
    
    public static Configuration getConf()
    {
        if (conf == null)
        {
            conf = new Configuration();
        }
        return conf;
    }
}

