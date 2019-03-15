package com.globalegrow.esearch.stat.event.filter;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;

/**
 * <pre>
 * 
 *  File: FileFilter.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2017年12月6日				lizhaohui				Initial.
 *
 * </pre>
 */
public class TmpFileFilter  implements PathFilter,Configurable
{

    private Configuration conf;
    @SuppressWarnings("unused")
    private FileSystem fs;
    
    @Override
    public boolean accept(Path path)
    {
        Configuration conf = new Configuration();
        FileSystem fs=null;
        try
        {
            fs=path.getFileSystem(conf);
            FileStatus[] fstatus=fs.listStatus(path);
            for(FileStatus fileStatus:fstatus){
                //小于100兆的数据文件进行过滤掉 说明还没采集完成
                if(fileStatus.getLen()>104857600){
                    return true;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void setConf(Configuration conf)
    {
        this.conf = conf;
    }

    @Override
    public Configuration getConf()
    {
        return this.conf;
    }
}