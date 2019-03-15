package com.globalegrow.esearch.constant;
/**
 * <pre>
 * 
 *  File: PathConstant.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2017年9月14日				lizhaohui				Initial.
 *
 * </pre>
 */
public interface PathConstant
{
    String PATH_SEARCH_BOX_EVENT = "/esearch/esearch-stat/data/{0}/hdp-ods/stmtr_search_box_daily";
    
    String PATH_CLICK_SKU_EVENT = "/esearch/esearch-stat/data/common/hdp-ods/stmtr_click_sku_daily";
    
    String PATH_CLICK_CATEGORY_EVENT = "/esearch/esearch-stat/data/common/hdp-ods/stmtr_click_category_daily";
    
    String PATH_CHECK_OUT_EVENT = "/esearch/esearch-stat/data/{0}/checkout/stmtr_check_out_daily";
    
    String PATH_USER_LOGIN_EVENT = "/esearch/esearch-stat/data/{0}/checkout/stmtr_user_login_daily";
    
    String PATH_B_POINT_KEYWORD = "/esearch/esearch-stat/data/{0}/bdp-keyword/stat_glb_searchword_daily";
    
    String PATH_B_POINT_WEIGHT = "/esearch/esearch-stat/data/{0}/bdp-weight/stat_glb_sku_weight_daily";
    
    String PATH_A_POINT_WEIGHT = "/esearch/esearch-stat/data/{0}/bdp-weight/stat_glb_sku_weight_daily";
    
    String PATH_A_POINT_LOGINFO = "/esearch/esearch-stat/data/{0}/bdp-user-info/stmtr_glb_user_info";

    String PATH_WIDE_TABLE = "/esearch/esearch-stat/data/common/hdp_widetable/wide_table_logs_daily";

    String PATH_APP_WIDE_TABLE = "/esearch/esearch-stat/data/common/hdp_widetable/app_wide_table_logs_daily";

}