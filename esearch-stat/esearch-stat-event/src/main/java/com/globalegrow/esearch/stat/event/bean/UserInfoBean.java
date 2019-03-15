package com.globalegrow.esearch.stat.event.bean;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

import com.globalegrow.esearch.constant.Constant;

/**
 * <pre>
 * 
 *  File: UserInfoBean.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2017年8月11日				lizhaohui				Initial.
 *
 * </pre>
 */
public class UserInfoBean implements Writable
{

    private String userId;

    private String cookieId;
    
    

    public UserInfoBean()
    {
    }
    
    public UserInfoBean(String userId, String cookieId)
    {
        super();
        this.userId = userId;
        this.cookieId = cookieId;
    }



    @Override
    public void write(DataOutput out) throws IOException
    {
        out.writeUTF(userId);
        out.writeUTF(cookieId);
    }

    @Override
    public void readFields(DataInput in) throws IOException
    {
        this.userId=in.readUTF();
        this.cookieId=in.readUTF();
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getCookieId()
    {
        return cookieId;
    }

    public void setCookieId(String cookieId)
    {
        this.cookieId = cookieId;
    }

    @Override
    public String toString()
    {
        StringBuffer buffer=new StringBuffer();
        buffer.append(userId).append(Constant.HIVE_SEPERATE).append(cookieId);
        return buffer.toString();
    }
}
