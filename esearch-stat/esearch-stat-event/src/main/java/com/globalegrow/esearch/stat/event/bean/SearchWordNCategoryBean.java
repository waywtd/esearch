package com.globalegrow.esearch.stat.event.bean;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

import com.globalegrow.esearch.constant.Constant;

/**
 * <pre>
 * 
 *  File: SearchWordCategoryBean.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2017年4月18日				lizhaohui				Initial.
 *
 * </pre>
 */
public class SearchWordNCategoryBean implements Writable
{

    /**
     * 关键字
     */
    private String keyword;
    
    /**
     * 分类id 对应nginx-log日志的ubccsku字段
     */
    private int catId;
    
    /**
     * keyword#catId
     */
    private String md5;
        
    /**
     * 被点击的次数
     */
    private int clickTimes;
    
    /**
     * 日期
     */
    private String dateTime;
    
    @Override
    public void write(DataOutput out) throws IOException
    {
        out.writeUTF(keyword);
        out.writeInt(catId);
        out.writeUTF(md5);
        out.writeInt(clickTimes);
        out.writeUTF(dateTime);
    }

    @Override
    public void readFields(DataInput in) throws IOException
    {
        this.keyword=in.readUTF();
        this.catId=in.readInt();
        this.md5=in.readUTF();
        this.clickTimes=in.readInt();
        this.dateTime=in.readUTF();
    }
    

    public String getKeyword()
    {
        return keyword;
    }

    public void setKeyword(String keyword)
    {
        this.keyword = keyword;
    }

    public int getCatId()
    {
        return catId;
    }

    public void setCatId(int catId)
    {
        this.catId = catId;
    }

    public String getMd5()
    {
        return md5;
    }

    public void setMd5(String md5)
    {
        this.md5 = md5;
    }

    public int getClickTimes()
    {
        return clickTimes;
    }

    public void setClickTimes(int clickTimes)
    {
        this.clickTimes = clickTimes;
    }

    public String getDateTime()
    {
        return dateTime;
    }

    public void setDateTime(String dateTime)
    {
        this.dateTime = dateTime;
    }

    @Override
    public String toString()
    {
        StringBuffer buffer=new StringBuffer();
        buffer.append(keyword).append(Constant.HIVE_SEPERATE).append(catId).append(Constant.HIVE_SEPERATE)
              .append(md5).append(Constant.HIVE_SEPERATE).append(clickTimes).append(Constant.HIVE_SEPERATE).append(dateTime);
        return buffer.toString();
    }
}