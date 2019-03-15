package com.globalegrow.esearch.stat.event.bean;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

import com.globalegrow.esearch.constant.Constant;

/**
 * <pre>
 * 
 *  File: UsersBuyBean.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2017年7月18日				lizhaohui				Initial.
 *
 * </pre>
 */
public class UsersBuyBean implements Writable
{

    //站点编号
    private String ubcd;
    
    //关键字
    private String ubckw;
    
    //关键字md5加密
    private String md5;
    
    //站点 www.geatbest.com -> www  fr.geartbest.com -> fr
    private String site;
    
    //goodsSn
    private String sku;
    
    //点击第几个
    private Integer rank;
    
    //页数
    private Integer page;
    
    //商品详情页 -- 商品列表页来源
    private String cl;
    
    private String userId;
    
    private String sessionId;
    
    private String cookieId;
    
    /**
     * 日期
     */
    private String dateTime;
    
    public UsersBuyBean()
    {
        super();
    }
    
    public UsersBuyBean(String ubcd, String ubckw,String md5, String site, String sku, Integer rank, Integer page, String cl,String userId,String sessionId,String cookieId,String dateTime)
    {
        super();
        this.ubcd = ubcd;
        this.ubckw = ubckw;
        this.md5=md5;
        this.site = site;
        this.sku = sku;
        this.rank = rank;
        this.page = page;
        this.cl = cl;
        this.userId=userId;
        this.sessionId=sessionId;
        this.cookieId=cookieId;
        this.dateTime=dateTime;
    }



    @Override
    public void write(DataOutput out) throws IOException
    {
        out.writeUTF(ubcd);
        out.writeUTF(ubckw);
        out.writeUTF(md5);
        out.writeUTF(site);
        out.writeUTF(sku);
        out.writeInt(rank);
        out.writeInt(page);
        out.writeUTF(cl);
        out.writeUTF(userId);
        out.writeUTF(sessionId);
        out.writeUTF(cookieId);
        out.writeUTF(dateTime);
    }

    @Override
    public void readFields(DataInput in) throws IOException
    {
        this.ubcd=in.readUTF();
        this.ubckw=in.readUTF();
        this.md5=in.readUTF();
        this.site=in.readUTF();
        this.sku=in.readUTF();
        this.rank=in.readInt();
        this.page=in.readInt();
        this.cl=in.readUTF();
        this.userId=in.readUTF();
        this.sessionId=in.readUTF();
        this.cookieId=in.readUTF();
        this.dateTime=in.readUTF();
    }

    public String getUbcd()
    {
        return ubcd;
    }

    public void setUbcd(String ubcd)
    {
        this.ubcd = ubcd;
    }

    public String getUbckw()
    {
        return ubckw;
    }

    public void setUbckw(String ubckw)
    {
        this.ubckw = ubckw;
    }
    
    public String getMd5()
    {
        return md5;
    }

    public void setMd5(String md5)
    {
        this.md5 = md5;
    }

    public String getSite()
    {
        return site;
    }

    public void setSite(String site)
    {
        this.site = site;
    }

    public String getSku()
    {
        return sku;
    }

    public void setSku(String sku)
    {
        this.sku = sku;
    }

    public Integer getRank()
    {
        return rank;
    }

    public void setRank(Integer rank)
    {
        this.rank = rank;
    }

    public Integer getPage()
    {
        return page;
    }

    public void setPage(Integer page)
    {
        this.page = page;
    }

    public String getCl()
    {
        return cl;
    }

    public void setCl(String cl)
    {
        this.cl = cl;
    }
    
    public String getDateTime()
    {
        return dateTime;
    }
    
    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getSessionId()
    {
        return sessionId;
    }

    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    public String getCookieId()
    {
        return cookieId;
    }

    public void setCookieId(String cookieId)
    {
        this.cookieId = cookieId;
    }

    public void setDateTime(String dateTime)
    {
        this.dateTime = dateTime;
    }
    
    @Override
    public String toString()
    {
        StringBuffer buffer=new StringBuffer();
        buffer.append(ubckw).append(Constant.HIVE_SEPERATE)
              .append(md5).append(Constant.HIVE_SEPERATE)
              .append(site).append(Constant.HIVE_SEPERATE)
              .append(sku).append(Constant.HIVE_SEPERATE)
              .append(rank).append(Constant.HIVE_SEPERATE)
              .append(page).append(Constant.HIVE_SEPERATE)
              .append(cl).append(Constant.HIVE_SEPERATE)
              .append(userId).append(Constant.HIVE_SEPERATE)
              .append(sessionId).append(Constant.HIVE_SEPERATE)
              .append(cookieId).append(Constant.HIVE_SEPERATE)
              .append(dateTime).append(Constant.HIVE_SEPERATE);
        return buffer.toString();
    }
}