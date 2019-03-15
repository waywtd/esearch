package com.globalegrow.esearch.stat.event;

import java.io.IOException;

import com.globalegrow.esearch.stat.event.common.HDFSClient;
import com.globalegrow.esearch.stat.event.common.HDFSWriter;

/**
 * <pre>
 * 
 *  File: GetVersion.java
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
public class GetVersion
{

    public static void main(String[] args)
    {
        System.out.println("Usage:\t\n args[0] hadoop inputPath\t\n args[1]:hadoop outputPath\t\n args[2]:siteCode \t\n args[3]:The state abbreviations");
    }

    public static void writeHdfs()
    {
        try
        {
            HDFSWriter.WriteToHDFS("hdfs://ns1/lizhaohui/version", "3");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void readHdfs()
    {
        Integer vesion = Integer.parseInt(HDFSClient.readTxt("hdfs://ns1/lizhaohui/version", 1));
        System.out.println("vesion:" + vesion);
    }
}