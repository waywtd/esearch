package com.globalegrow.esearch.stat.event.common;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * <pre>
 * 
 *  File: HDFSWriter.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2017年4月19日				chengmo				Initial.
 *
 * </pre>
 */
public class HDFSWriter
{
    private static final String utf8 = "UTF-8";
    protected DataOutputStream out;

  //在指定位置新建一个文件，并写入字符  
    public static void WriteToHDFS(String file, String words) throws IOException  
    {  
        Configuration conf = new Configuration();  
        FileSystem fs = FileSystem.get(URI.create(file), conf);  
        Path path = new Path(file);  
        FSDataOutputStream out = fs.create(path);   //创建文件  
  
        //两个方法都用于文件写入，好像一般多使用后者  
        out.write(words.getBytes(utf8));  
          
        out.close();  
    }  
}
