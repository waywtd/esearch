package com.globalegrow.esearch.stat.event.bean;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

import com.globalegrow.esearch.constant.Constant;

/**
 * <pre>
 * 
 *  File: SkuBean.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2017年3月27日				lizhaohui				Initial.
 *
 * </pre>
 */
public class SkuWeightBean implements Writable
{
    
    /**
     * goodsSn
     */
    private String goodsSn;
    
    /**
     * 仓库编码
     */
    private String vWhCode="";
    
    /**
     * 浏览量
     */
    private Long goodsViewTimes;
    
    /**
     * 购物车数量
     */
    private Long shoppingCartCount;
    
    /**
     * 收藏夹数量
     */
    private Long favoriteCount;
    
    /**
     * 被曝光次数
     */
    private Long goodsClick;
    
    /**
     * 日期
     */
    private String dateTime;
    
    public SkuWeightBean()
    {
        super();
    }
    
    public SkuWeightBean(String goodsSn,String vWhCode, Long goodsViewTimes, Long shoppingCartCount, Long favoriteCount,
            Long goodsClick,String dateTime)
    {
        super();
        this.goodsSn = goodsSn;
        this.vWhCode = vWhCode;
        this.goodsViewTimes = goodsViewTimes;
        this.shoppingCartCount = shoppingCartCount;
        this.favoriteCount = favoriteCount;
        this.goodsClick = goodsClick;
        this.dateTime=dateTime;
    }
    
    public SkuWeightBean(String goodsSn, Long goodsViewTimes, Long shoppingCartCount, Long favoriteCount,
            Long goodsClick,String dateTime)
    {
        super();
        this.goodsSn = goodsSn;
        this.goodsViewTimes = goodsViewTimes;
        this.shoppingCartCount = shoppingCartCount;
        this.favoriteCount = favoriteCount;
        this.goodsClick = goodsClick;
        this.dateTime=dateTime;
    }



    @Override
    public void write(DataOutput out) throws IOException
    {
        out.writeUTF(goodsSn);
        out.writeUTF(vWhCode);
        out.writeLong(goodsViewTimes);
        out.writeLong(shoppingCartCount);
        out.writeLong(favoriteCount);
        out.writeLong(goodsClick);
        out.writeUTF(dateTime);
    }

    @Override
    public void readFields(DataInput in) throws IOException
    {
        this.goodsSn=in.readUTF();
        this.vWhCode=in.readUTF();
        this.goodsViewTimes=in.readLong();
        this.shoppingCartCount=in.readLong();
        this.favoriteCount=in.readLong();
        this.goodsClick=in.readLong();
        this.dateTime=in.readUTF();
    }

    public Long getGoodsViewTimes()
    {
        return goodsViewTimes;
    }

    public void setGoodsViewTimes(Long goodsViewTimes)
    {
        this.goodsViewTimes = goodsViewTimes;
    }

    public Long getShoppingCartCount()
    {
        return shoppingCartCount;
    }

    public void setShoppingCartCount(Long shoppingCartCount)
    {
        this.shoppingCartCount = shoppingCartCount;
    }

    public Long getFavoriteCount()
    {
        return favoriteCount;
    }

    public void setFavoriteCount(Long favoriteCount)
    {
        this.favoriteCount = favoriteCount;
    }

    public Long getGoodsClick()
    {
        return goodsClick;
    }

    public void setGoodsClick(Long goodsClick)
    {
        this.goodsClick = goodsClick;
    }

    public String getGoodsSn()
    {
        return goodsSn;
    }

    public void setGoodsSn(String goodsSn)
    {
        this.goodsSn = goodsSn;
    }

    public String getvWhCode()
    {
        return vWhCode;
    }

    public void setvWhCode(String vWhCode)
    {
        this.vWhCode = vWhCode;
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
        buffer.append(goodsSn).append(Constant.HIVE_SEPERATE).append(vWhCode).append(Constant.HIVE_SEPERATE).append(goodsViewTimes).append(Constant.HIVE_SEPERATE).append(shoppingCartCount).append(Constant.HIVE_SEPERATE).append(favoriteCount).append(Constant.HIVE_SEPERATE).append(goodsClick).append(Constant.HIVE_SEPERATE).append(dateTime);
        return buffer.toString();
    }
}