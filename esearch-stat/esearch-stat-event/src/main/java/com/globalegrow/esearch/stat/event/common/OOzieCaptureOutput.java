package com.globalegrow.esearch.stat.event.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;

/**
 * <pre>
 * 
 *  File: OOzieCaptureOutput.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2017年6月28日				lizhaohui				Initial.
 *
 * </pre>
 */
public class OOzieCaptureOutput
{

    private static final String OOZIE_ACTION_OUTPUT_PROPERTIES = "oozie.action.output.properties";
    
    /**
     * 将变量输出值oozie共享变量
     * 
     * @param dataMap oozie共享变量集合
     * @throws Exception
     */
    public static void captureOutput(Map<String, Integer> dataMap) throws Exception
    {
        String oozieProp = System.getProperty(OOZIE_ACTION_OUTPUT_PROPERTIES);
        if (oozieProp != null)
        {
            File propFile = new File(oozieProp);
            Properties props = new Properties();
            for (Map.Entry<String, Integer> entry : dataMap.entrySet())
            {
                props.setProperty(entry.getKey(), Integer.toString(entry.getValue()));
            }
            OutputStream os = new FileOutputStream(propFile);
            props.store(os, "");
            os.close();
        }
        else
        {
            throw new RuntimeException(OOZIE_ACTION_OUTPUT_PROPERTIES + " System property not defined");
        }
    }
    
}

