package org.globalegrow.esearch.constant;
/**
 * <pre>
 * 
 *  File: Constant.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2017年11月10日				lizhaohui				Initial.
 *
 * </pre>
 */
public interface Constants
{

    /**
     * 状态码 200
     */
    String STATUESUCCEED = "^A^200^A^";
    
    
    public static String CONF_UBCD = "ubcd";
    
    /**
     * 正则表达式 截取用户行为埋点值
     */
    String REGUBC = "(.*?)=(.*?)&";
    
    /**
     * ubcd
     */
    String UBCD_D = "glb_d";
    
    String UBCD = "ubcd";
    
    String DOMAIN = "domain";
    
    /**
     * 编码格式
     */
    String UTF8 = "UTF-8";
    
    String CLOUD_MONITOR = "cloud_monitor";
    
    String S_LOGSSS_COM = "s.logsss.com";
    
    String NGINX_SPLIT_SYMBOL = "^A^";
    
    String EQUAL_SIGN = "=";

    String MIDDLELINE = "-";

    String KAFKA_REQUEST_PARAM = "request_param";

    String MESSAGE_REQUERY = "message_reqquery";

    String YEAR = "year";
    String MONTH = "month";
    String DAY = "day";
    String LOG_TYPE = "log_type";

    String DEFAULT_NULL_STRING = "NILL";



}