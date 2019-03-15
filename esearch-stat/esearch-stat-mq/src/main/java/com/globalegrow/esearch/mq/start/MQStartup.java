package com.globalegrow.esearch.mq.start;

import com.globalegrow.esearch.mq.bootstrap.ServerBootstrap;
import com.globalegrow.esearch.mq.consumer.ConsumerInitiator;
import com.globalegrow.esearch.mq.util.log.ELog;
import com.globalegrow.esearch.mq.util.log.LogFactory;

/**
 * <pre>
 *
 *  File: MQStartup.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018年07月19日			Nieruiqun				Initial.
 *
 * </pre>
 */
public class MQStartup {

    private static final ELog log = LogFactory.getLog(MQStartup.class);

    public static void main(String[] args) {
        log.info("esearch-stat-mq server is starting...");
        try {
            ServerBootstrap.start();
            ConsumerInitiator.start();
            log.info("esearch-stat-mq server is started.");
        } catch (Exception e) {
            log.error("esearch-stat-mq server failed to start due to {0}.", e);
            System.exit(0);
        }
    }

}
