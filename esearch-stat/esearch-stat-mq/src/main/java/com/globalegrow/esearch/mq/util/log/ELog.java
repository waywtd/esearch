package com.globalegrow.esearch.mq.util.log;

/**
 * <pre>
 * 
 *  File: ELog1.java
 * 
 *  Copyright (c) 2016, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2016年4月25日				qinhuaping				Initial.
 *
 * </pre>
 */
public interface ELog
{
    public boolean isDebugEnabled();
    public boolean isDebugEnabled(int level);
    public boolean isTraceEnabled(int level);
    
    public void warn(String str);
    public void warn(String str, Throwable e);
    public void warn(String pattern, Object... arguments);
    public void warn(String pattern, Throwable e, Object... arguments);
    
    public void info(String str);
    public void info(Object str);
    public void info(String s, Throwable e);
    public void info(String pattern, Object... arguments);
    public void info(String pattern, Throwable e, Object... arguments);
    
    public void debug(String str);
    public void debug(String str, Throwable e);
    public void debug(String pattern, Object... arguments);
    public void debug(String pattern, Throwable e, Object... arguments);
    
    public void error(String str);
    public void error(Object obj);
    public void error(String str, Throwable e);
    public void error(String pattern, Object... arguments);
    public void error(String pattern, Throwable e, Object... arguments);
    
    public void trace(String str);
    
    public void all(String str);


}
