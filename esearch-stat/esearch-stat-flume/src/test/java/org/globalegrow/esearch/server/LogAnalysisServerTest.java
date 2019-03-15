package org.globalegrow.esearch.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.globalegrow.esearch.constant.NginxFieldConstant;
import org.globalegrow.esearch.utils.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *
 *  File: LogAnalysisServerTest.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,                   Who,                    What;
 *  2018年09月27日           nieruiqun               Initial.
 *
 * </pre>
 */
public class LogAnalysisServerTest {

    JSONObject nginxLogJson;
    JSONObject appLogJson;

    @Before
    public void init() throws Exception {
        URL nginxLogUrl = this.getClass().getResource("/nginx-log.json");
        URL appLogUrl = this.getClass().getResource("/app-log.json");
        File file = new File(nginxLogUrl.getPath());
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(nginxLogUrl.getPath()));
        byte[] b = new byte[bis.available()];
        bis.read(b);
        nginxLogJson = JSON.parseObject(new String(b));
        bis = new BufferedInputStream(new FileInputStream(appLogUrl.getPath()));
        byte[] b1 = new byte[bis.available()];
        bis.read(b1);
        appLogJson = JSON.parseObject(new String(b1));
        bis.close();
    }

    @Test
    public void getUbcd() throws Exception {
        System.out.println(LogAnalysisServer.getUbcd(nginxLogJson));
        System.out.println(LogAnalysisServer.getUbcd(appLogJson));
    }

    @Test
    public void restHeader() throws Exception {
        System.out.println(LogAnalysisServer.restHeader(nginxLogJson, LogAnalysisServer.getUbcd(nginxLogJson)));
        System.out.println(LogAnalysisServer.restHeader(appLogJson, LogAnalysisServer.getUbcd(appLogJson)));
    }

    @Test
    public void constructLogMsg() throws Exception {
        System.out.println(LogAnalysisServer.constructLogMsg(nginxLogJson));
        System.out.println(LogAnalysisServer.constructLogMsg(appLogJson));
    }

    @Test
    public void getIp() throws Exception {
        String messageType = appLogJson.getString(NginxFieldConstant.MESSAGE_REQPATH);
        String ip =  appLogJson.getString(NginxFieldConstant.MESSAGE_CLIIP);
        if(NginxFieldConstant.MESSAGE_TYPE_APP.equals(messageType)) {
            final Pattern pattern = Pattern.compile("\\&ip=[0-9\\.]+\\&");
            String line = appLogJson.getString(NginxFieldConstant.FULL_REQUEST);
            Matcher matcher = pattern.matcher(line);
            if(matcher.find()) {
                ip = matcher.group().replace("&ip=","").replace("&","");
            }
        }
        System.out.println(ip);
    }


}