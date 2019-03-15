package com.globalegrow.esearch.stat.event.bean;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

import com.globalegrow.esearch.constant.Constant;

/**
 * <pre>
 * 
 *  File: SearchWordBean.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2017年4月1日				lizhaohui				Initial.
 *
 * </pre>
 */
public class SearchNWordBean implements Writable
{

    /**
     * 热搜词
     */
    private String searchWord;
    
    /**
     * 分类id
     */
    private Integer catId=0;
    
    /**
     * searchWord字符的MD5值
     */
    private String md5;
    
    /**
     * 被搜索的次数
     */
    private int searchTimes;
    
    /**
     * 显示商品总数
     */
    private int ubamount;
    
    /**
     * 日期
     */
    private String dateTime;
    
    private String sourceLog;
    
    public SearchNWordBean()
    {
        super();
    }
    
    public SearchNWordBean(String searchWord,Integer catId,int searchTimes, String md5, int ubamount, String dateTime,String sourceLog)
    {
        super();
        this.searchWord = searchWord;
        this.catId=catId;
        this.searchTimes=searchTimes;
        this.md5 = md5;
        this.ubamount = ubamount;
        this.dateTime = dateTime;
        this.sourceLog=sourceLog;
    }

    @Override
    public void write(DataOutput out) throws IOException
    {
        out.writeUTF(searchWord);
        out.writeInt(catId);
        out.writeInt(searchTimes);
        out.writeUTF(md5);
        out.writeInt(ubamount);
        out.writeUTF(dateTime);
        out.writeUTF(sourceLog);
    }

    @Override
    public void readFields(DataInput in) throws IOException
    {
        this.searchWord=in.readUTF();
        this.catId=in.readInt();
        this.searchTimes=in.readInt();
        this.md5=in.readUTF();
        this.ubamount=in.readInt();
        this.dateTime=in.readUTF();
        this.sourceLog=in.readUTF();
    }

    public String getSearchWord()
    {
        return searchWord;
    }

    public void setSearchWord(String searchWord)
    {
        this.searchWord = searchWord;
    }

    public String getMd5()
    {
        return md5;
    }

    public void setMd5(String md5)
    {
        this.md5 = md5;
    }

    public int getUbamount()
    {
        return ubamount;
    }

    public void setUbamount(int ubamount)
    {
        this.ubamount = ubamount;
    }

    public String getDateTime()
    {
        return dateTime;
    }

    public void setDateTime(String dateTime)
    {
        this.dateTime = dateTime;
    }
    
    public int getSearchTimes()
    {
        return searchTimes;
    }

    public void setSearchTimes(int searchTimes)
    {
        this.searchTimes = searchTimes;
    }
    
    public Integer getCatId()
    {
        return catId;
    }

    public void setCatId(Integer catId)
    {
        this.catId = catId;
    }
    
    public String getSourceLog()
    {
        return sourceLog;
    }

    public void setSourceLog(String sourceLog)
    {
        this.sourceLog = sourceLog;
    }

    @Override
    public String toString()
    {
        StringBuffer buffer=new StringBuffer();
        buffer.append(searchWord).append(Constant.HIVE_SEPERATE)
              .append(catId).append(Constant.HIVE_SEPERATE)
              .append(md5).append(Constant.HIVE_SEPERATE)
              .append(searchTimes).append(Constant.HIVE_SEPERATE)
              .append(ubamount).append(Constant.HIVE_SEPERATE)
              .append(dateTime).append(Constant.HIVE_SEPERATE)
              .append(sourceLog).append(Constant.HIVE_SEPERATE);
        return buffer.toString();
    }
}