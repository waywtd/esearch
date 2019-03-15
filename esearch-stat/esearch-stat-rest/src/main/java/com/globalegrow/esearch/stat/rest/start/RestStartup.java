package com.globalegrow.esearch.stat.rest.start;

import com.globalegrow.esearch.stat.rest.bootstrap.JettyServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <pre>
 *
 *  File: RestStartup.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,                   Who,                    What;
 *  2018年07月05日           nieruiqun               Initial.
 *
 * </pre>
 */
public class RestStartup {

    private static final Logger log = LogManager.getLogger(RestStartup.class);

    public static void main(String[] args) {
        log.info("esearch-stat-rest server is starting...");
        try {
            new JettyServer().run();
            log.info("esearch-stat-rest server is started.");
        } catch (Exception e) {
            log.error("esearch-stat-rest server failed to start due to {0}.", e);
            System.exit(0);
        }
    }




}
