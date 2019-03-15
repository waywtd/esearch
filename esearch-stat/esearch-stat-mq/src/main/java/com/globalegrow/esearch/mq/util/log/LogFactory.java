package com.globalegrow.esearch.mq.util.log;

/**
 * <pre>
 * 
 *  File: LogFactory.java
 * 
 *  Copyright (c) 2016, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2016年11月30日			qinhuaping				Initial.
 *
 * </pre>
 */
public class LogFactory
{
    public static ELog getLog(Class<?> clazz)
    {
        return new Log4j2Impl(clazz);
    }
}

