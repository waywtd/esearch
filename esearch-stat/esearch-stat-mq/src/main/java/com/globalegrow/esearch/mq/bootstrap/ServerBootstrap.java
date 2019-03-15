package com.globalegrow.esearch.mq.bootstrap;

import com.globalegrow.esearch.mq.util.log.ELog;
import com.globalegrow.esearch.mq.util.log.LogFactory;

import java.util.concurrent.CountDownLatch;

/**
 * <pre>
 * 
 *  File: ServerBootstrap.java
 * 
 *  Copyright (c) 2016, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2016年11月19日			qinhuaping				Initial.
 *
 * </pre>
 */
public class ServerBootstrap
{
    private static ELog log = LogFactory.getLog(ServerBootstrap.class);

    private final CountDownLatch keepAliveLatch = new CountDownLatch(1);
    private Thread keepAliveThread = null;

    private ServerBootstrap()
    {
    }

    private static class SingletonHolder
    {
        private static final ServerBootstrap INSTANCE = new ServerBootstrap();
    }

    public static void close()
    {
        SingletonHolder.INSTANCE.keepAliveLatch.countDown();
    }

    public static void start(String... configLocations)
    {
        SingletonHolder.INSTANCE.initServer(configLocations);
        SingletonHolder.INSTANCE.serverStart();
    }

    private void initServer(String... configLocations)
    {
        if (keepAliveThread != null)
        {
            return;
        }

        keepAliveThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    log.debug("keepAliveLatch wait:" + keepAliveLatch);
                    keepAliveLatch.await();
                }
                catch (InterruptedException e)
                {
                    log.debug("keepAliveLatch wait exception:" + keepAliveLatch);
                }
            }
        }, "server [keepAlive]");
        keepAliveThread.setDaemon(false);

        // Keep this thread alive (non daemon thread) until we shutdown
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                keepAliveLatch.countDown();
                log.debug("close keepAliveLatch:" + keepAliveLatch);
            }
        });
    }

    private void serverStart()
    {
        if (keepAliveThread != null)
        {
            keepAliveThread.start();
        }
    }

}
