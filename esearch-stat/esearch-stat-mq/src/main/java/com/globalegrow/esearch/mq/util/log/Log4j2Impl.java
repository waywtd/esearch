package com.globalegrow.esearch.mq.util.log;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.spi.ExtendedLoggerWrapper;

import java.text.MessageFormat;

/**
 * <pre>
 * 
 *  File: Log4j2Impl.java
 * 
 *  Copyright (c) 2016, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2016年11月30日				qinhuaping				Initial.
 *
 * </pre>
 */
public class Log4j2Impl implements ELog
{
    private static final String FQCN = Log4j2Impl.class.getName();
    private ExtendedLoggerWrapper log;

    public Log4j2Impl(Class<?> clazz)
    {
        Logger logger = LogManager.getLogger(clazz);

        // if (logger instanceof AbstractLogger) {
        // log = new Log4j2AbstractLoggerImpl((AbstractLogger) logger);
        // } else {
        // log = new Log4j2LoggerImpl(logger);
        // }

        log = new ExtendedLoggerWrapper((ExtendedLogger) logger, logger.getName(), logger.getMessageFactory());
    }

    public boolean isDebugEnabled()
    {
        return log.isDebugEnabled();
    }

    public boolean isDebugEnabled(int level)
    {
        return log.isDebugEnabled() || Level.DEBUG.intLevel() <= level;
    }
    
    public boolean isTraceEnabled(int level)
    {
        return log.isDebugEnabled() || Level.TRACE.intLevel() <= level;
    }
    
    public void warn(String str)
    {
        log.logIfEnabled(FQCN, Level.WARN, null, str);
    }

    public void warn(String str, Throwable e)
    {
        log.logIfEnabled(FQCN, Level.WARN, null, str, e);
    }

    public void warn(String pattern, Object... arguments)
    {
        log.logIfEnabled(FQCN, Level.WARN, null, format(pattern, arguments));
    }
    
    public void warn(String pattern,Throwable e, Object... arguments)
    {
        log.logIfEnabled(FQCN, Level.WARN, null, format(pattern, arguments),e);
    }

    @Override
    public void info(String str)
    {
        log.logIfEnabled(FQCN, Level.INFO, null, str);
    }
    
    public void info(Object obj)
    {
        log.logIfEnabled(FQCN, Level.INFO,null,obj,null);
    }
    
    public void info(String s, Throwable e)
    {
        log.logIfEnabled(FQCN, Level.INFO, null, s, e);
    }

    public void info(String pattern, Object... arguments)
    {
        log.logIfEnabled(FQCN, Level.INFO, null, format(pattern, arguments));
    }
    
    public void info(String pattern,Throwable e, Object... arguments)
    {
        log.logIfEnabled(FQCN, Level.INFO, null, format(pattern, arguments),e);
    }

    public void debug(String str)
    {
        log.logIfEnabled(FQCN, Level.DEBUG, null, str);
    }

    public void debug(String str, Throwable e)
    {
        log.logIfEnabled(FQCN, Level.DEBUG, null, str, e);
    }

    public void debug(String pattern, Object... arguments)
    {
        log.logIfEnabled(FQCN, Level.DEBUG, null, format(pattern, arguments));
    }

    public void debug(String pattern,Throwable e, Object... arguments)
    {
        log.logIfEnabled(FQCN, Level.DEBUG, null, format(pattern, arguments),e);
    }
    
    public void error(String str, Throwable e)
    {
        log.logIfEnabled(FQCN, Level.ERROR, null, str, e);
    }

    public void error(String str)
    {
        log.logIfEnabled(FQCN, Level.ERROR, null, str);
    }
    
    public void error(Object obj)
    {
        log.logIfEnabled(FQCN, Level.ERROR,null,obj,null);
    }

    public void error(String pattern, Object... arguments)
    {
        log.logIfEnabled(FQCN, Level.ERROR, null, format(pattern, arguments));
    }

    public void error(String pattern,Throwable e, Object... arguments)
    {
        log.logIfEnabled(FQCN, Level.ERROR, null, format(pattern, arguments),e);
    }
    
    public void trace(String str)
    {
        log.logIfEnabled(FQCN, Level.TRACE, null, str);
    }

    public void all(String str)
    {
        log.logIfEnabled(FQCN, Level.ALL, null, str);
    }

    private static String format(String pattern, Object... arguments)
    {
        return MessageFormat.format(pattern, arguments);
    }
}
