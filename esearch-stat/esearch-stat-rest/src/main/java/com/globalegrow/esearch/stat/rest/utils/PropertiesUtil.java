package com.globalegrow.esearch.stat.rest.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * <pre>
 *
 *  File: PropertiesUtil.java
 *
 *  Copyright (c) 2016, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2016年8月9日				chengmo				Initial.
 *
 * </pre>
 */
public class PropertiesUtil {

    private static Logger log = LogManager.getLogger(PropertiesUtil.class);

    public static synchronized Map<String, String> loadPropertiesFile(String file) {
        Map<String, String> ht = new LinkedHashMap<String, String>();
        Properties prop = new OrderedProperties();
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            prop.load(in);

            for (String key : prop.stringPropertyNames()) {
                ht.put(key.trim(), prop.getProperty(key).trim());
            }
        } catch (Exception ex) {
            log.error("Load properties file [" + file + "] error due to:" + ex.toString());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                }
                in = null;
            }
        }
        return ht;
    }

    private static class OrderedProperties extends Properties {
        private static final long serialVersionUID = 1L;
        private final LinkedHashSet<Object> keys = new LinkedHashSet<Object>();

        @Override
        public Enumeration<Object> keys() {
            return Collections.<Object>enumeration(keys);
        }

        @Override
        public Object put(Object key, Object value) {
            keys.add(key);
            return super.put(key, value);
        }

        @Override
        public Set<Object> keySet() {
            return keys;
        }

        @Override
        public Set<String> stringPropertyNames() {
            Set<String> set = new LinkedHashSet<String>();
            for (Object key : this.keys) {
                set.add((String) key);
            }
            return set;
        }
    }

    public static Properties loadProperties(String file) {
        InputStream is = null;
        Properties props = new Properties();
        try {
            is = PropertiesUtil.class.getResourceAsStream(file);
            if (is == null) {
                return props;
            }
            props.load(is);
        } catch (IOException e) {
            log.error("Reading properties file error!" + e.toString());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error("Closing InputStream occurs error due to:" + e.toString());
                }
            }
        }
        return props;
    }
}
