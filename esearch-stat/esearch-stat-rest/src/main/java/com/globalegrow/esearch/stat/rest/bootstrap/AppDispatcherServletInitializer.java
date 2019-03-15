package com.globalegrow.esearch.stat.rest.bootstrap;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * <pre>
 *
 *  File: AppDispatcherServletInitializer.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *     系统初始化入口
 *
 *  Revision History
 *  Date,                   Who,                    What;
 *  2018年07月11日           nieruiqun               Initial.
 *
 * </pre>
 */
public class AppDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    /**
     *  设置spring容器入口
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{ApplicationConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{AppWebMvcConfig.class};
    }

    /**
     *  设置DispatcherServlet 拦截路径
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
