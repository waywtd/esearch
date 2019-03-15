package com.globalegrow.esearch.stat.rest.bootstrap;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * <pre>
 *
 *  File: ApplicationConfig.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,                   Who,                    What;
 *  2018年07月11日           nieruiqun               Initial.
 *
 * </pre>
 */
@Configuration
@ComponentScan(basePackages = "com.globalegrow.esearch.stat.rest",
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value=EnableWebMvc.class)})
@Import({AppWebMvcConfig.class})
public class ApplicationConfig {
}
