package com.globalegrow.esearch.stat.event.common;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.LineReader;

import com.globalegrow.esearch.stat.util.HadoopUtil;

/**
 * <pre>
 * 
 *  File: HDFSClient.java
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
public class HDFSClient
{

    private static class SingletonHolder
    {
        private static final HDFSClient INSTANCE = new HDFSClient();
    }

    public static FileSystem getFS()
    {
        return SingletonHolder.INSTANCE.newFileSystem();
    }
    
    public static String readTxt(String file){
        return readTxt(file,1);
    }

    /**
     * Only read txt file, if file is path return null.
     *
     * @param file
     * @param lines
     * @return
     */
    public static String readTxt(String file, int lines)
    {

        StringBuffer buff = new StringBuffer();
        FSDataInputStream fileInputStream = null;
        FileSystem fileSystem = null;
        try
        {
            fileSystem = getFS();
            Path path = new Path(file);

            if (!fileSystem.exists(path))
            {
                return null;
            }

            if (fileSystem.getFileStatus(path).isDirectory())
            {
                return null;
            }

            fileInputStream = fileSystem.open(path);
            LineReader inLine = new LineReader(fileInputStream, HadoopUtil.getConf());
            Text line = new Text();

            int rowCounter = 0;
            while ((inLine.readLine(line) > 0) && (rowCounter < lines))
            {
                buff.append(line.toString());
                buff.append("\n");
                ++rowCounter;
            }
            inLine.close();
            fileInputStream.close();
        }
        catch (Exception e)
        {
        }
        finally
        {
            closeFS(fileSystem);
        }
        return buff.toString();
    }

    public static void closeFS(FileSystem fs)
    {
        if (fs != null)
        {
            try
            {
                fs.close();
            }
            catch (IOException e)
            {
            }
        }
    }

    private FileSystem newFileSystem()
    {
        return newFileSystem(HadoopUtil.getConf());
    }

    private FileSystem newFileSystem(Configuration conf)
    {
        FileSystem fs = null;
        try
        {
            conf.setBoolean("fs.hdfs.impl.disable.cache", true);
            fs = FileSystem.get(conf);
        }
        catch (IOException e)
        {
        }
        return fs;
    }

}