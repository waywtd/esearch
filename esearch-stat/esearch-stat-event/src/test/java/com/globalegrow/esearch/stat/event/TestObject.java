package com.globalegrow.esearch.stat.event;

import java.util.regex.Matcher;

import org.junit.Ignore;
import org.junit.Test;

import com.globalegrow.esearch.constant.Constant;
import com.globalegrow.esearch.util.CommonUtil;
import com.globalegrow.esearch.util.StringUtils;

/**
 * <pre>
 * 
 *  File: TestObject.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2017年3月24日				lizhaohui				Initial.
 *
 * </pre>
 */
public class TestObject
{

    @Ignore
    @Test
    public void test01()
    {
    }
    
    @Ignore
    @Test
    public void test02(){
        String skuinfo="ubct=ic&ubci=et7an6nq1d2d1369afdvdgldd2&cookieid=100141485566880922pcdrufg704303962938636243&ubcd=10014&spcb=c&pagemodule=purchase&ubctd=ADT&skuinfo={%22186415504%22:{%22amount%22:%221%22,%22category%22:%22Jumpsuits%20&%20Rompers%22}}&ubcct=1489615824230&wtime=53489";
        Matcher m = CommonUtil.getMatcher(skuinfo, Constant.REGUBC);
        while (m.find())
        {
            String key = m.group(1);
            String value = m.group(2);
            switch (key){
                case Constant.SKUINFO:
                    System.out.println(value);
                    break;
            }
        }
    }
    
    
    @Test
    public void test03(){
        String str="shirt🤗11283cf141252763ade2123bac935bbc21d75111286";
        String[] strAtt=str.split(Constant.HIVE_SEPERATE);
        for(int i=0;i<strAtt.length;i++){
            System.out.println(StringUtils.validateStr(strAtt[i]));
        }
    }
}