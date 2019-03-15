package com.globalegrow.esearch.stat.rest.bootstrap;

import com.globalegrow.esearch.stat.rest.utils.PropertiesUtil;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *
 *  File: JettyServer.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,                   Who,                    What;
 *  2018年07月12日           nieruiqun               Initial.
 *
 * </pre>
 */
public class JettyServer {

    public static Map<String,String> properties = new HashMap<>();
    private static final String DEFAULT_PORT = "8080";

    private static void initProperties()
    {
        URL url = JettyServer.class.getClass().getResource("/rest.properties");
        File file = new File(url.getPath());
        properties = PropertiesUtil.loadPropertiesFile(file.getPath());
    }

    public void run() throws Exception {
        initProperties();
        Server server = new Server(getQueuedThreadPool());
        ServerConnector connector = new ServerConnector(server);
        int port = Integer.parseInt(properties.getOrDefault("server.port", DEFAULT_PORT));
        connector.setPort(port);
        server.setConnectors(new Connector[]{connector});
        server.setHandler(servletContextHandler(webApplicationContext()));
        server.start();
        server.join();
    }

    private QueuedThreadPool getQueuedThreadPool() {
        int maxThreads = Integer.parseInt(properties.getOrDefault("server.max.thread", "500"));
        int minThreads = Integer.parseInt(properties.getOrDefault("server.min.thread", "10"));
        int idleTimeout = Integer.parseInt(properties.getOrDefault("server.idle.timeout", "60000"));
        QueuedThreadPool threadPool = new QueuedThreadPool(maxThreads, minThreads, idleTimeout);
        threadPool.setName("jetty-server-thread-pool");
        return threadPool;
    }

    private ServletContextHandler servletContextHandler(WebApplicationContext context) {
        ServletContextHandler handler = new ServletContextHandler();
        handler.setContextPath("/");
        handler.addServlet(new ServletHolder(new DispatcherServlet(context)), "/*");
        handler.addEventListener(new ContextLoaderListener(context));
        return handler;
    }

    private WebApplicationContext webApplicationContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(ApplicationConfig.class);
        return context;
    }

}
