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
public class SearchWordBean implements Writable
{

    /**
     * 仓库code
     */
    private String vWhCode;
    
    /**
     * 热搜词
     */
    private String searchWord;
    
    /**
     * searchWord字符的MD5值
     */
    private String md5;
    
    private int searchTimes;
    
    /**
     * 显示商品总数
     */
    private int ubamount;
    
    /**
     * 日期
     */
    private String dateTime;
    
    public SearchWordBean()
    {
        super();
    }
    
    public SearchWordBean(String vWhCode,String searchWord,int searchTimes, String md5, int ubamount, String dateTime)
    {
        super();
        this.vWhCode=vWhCode;
        this.searchWord = searchWord;
        this.searchTimes=searchTimes;
        this.md5 = md5;
        this.ubamount = ubamount;
        this.dateTime = dateTime;
    }

    @Override
    public void write(DataOutput out) throws IOException
    {
        out.writeUTF(vWhCode);
        out.writeUTF(searchWord);
        out.writeInt(searchTimes);
        out.writeUTF(md5);
        out.writeInt(ubamount);
        out.writeUTF(dateTime);
    }

    @Override
    public void readFields(DataInput in) throws IOException
    {
        this.vWhCode=in.readUTF();
        this.searchWord=in.readUTF();
        this.searchTimes=in.readInt();
        this.md5=in.readUTF();
        this.ubamount=in.readInt();
        this.dateTime=in.readUTF();
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

    public String getvWhCode()
    {
        return vWhCode;
    }

    public void setvWhCode(String vWhCode)
    {
        this.vWhCode = vWhCode;
    }

    @Override
    public String toString()
    {
        StringBuffer buffer=new StringBuffer();
        buffer.append(vWhCode).append(Constant.HIVE_SEPERATE).append(searchWord).append(Constant.HIVE_SEPERATE).append(md5).append(Constant.HIVE_SEPERATE).append(searchTimes).append(Constant.HIVE_SEPERATE).append(ubamount).append(Constant.HIVE_SEPERATE).append(dateTime);
        return buffer.toString();
    }
}