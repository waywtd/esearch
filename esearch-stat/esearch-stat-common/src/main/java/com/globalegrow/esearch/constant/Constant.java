package com.globalegrow.esearch.constant;

/**
 * <pre>
 * 
 *  File: SkuDriver.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,                   Who,                    What;
 *  2017年3月24日              lizhaohui               Initial.
 *
 * </pre>
 */
public class Constant {
    
    public static final String SITE_CODE="siteCode";
    
    public static final String SITE="site";
    
    public static final String ALIAS="alias"; 
    
    public static final String KEY="key";
    
    public static final String VALUE="value";
    
    public static final String SKU_LIST="skuList";
    
    public static final String CHECK_OUT="checkout";
    
    public static final String YEAR="year";
    
    public static final String MONTH="month";
    
    public static final String DAY="day";
    
    public static final String RESULT="result";
    
    public static final String DOMAIN = "domain";

    public static final String PATH = "path";

    /**
     * user_id埋点
     */
    public static final String GLB_USER_ID="glb_u";
    
    public static final String GLB_SESSION_ID="glb_oi";
    
    public static final String GLB_COOKIE_ID="glb_od";
    
    /**
     * 商品详情页 - 来源于那个列表页 
     * 不带分页
     */
    public static final String PL="pl";
    
	/**
	 * jsession ID
	 */
	public static final String UBCI = "ubci";
	/**
	 * 页面类型
	 */
	public static final String UBCPT = "ubcpt";
	/**
	 * 分类ID
	 */
	public static final String UBCSC = "ubcsc";
	/**
	 * sku
	 */
	public static final String UBCS = "ubcs";
	/**
	 * 仓库信息编号
	 */
	public static final String UBCCK = "ubcck";
	/**
	 * skuinfo
	 */
	public static final String SKUINFO = "skuinfo";
	
	/**
	 * amount
	 */
	public static final String AMOUNT = "amount";
	
	/**
	 * amount
	 */
	public static final String AMOUNT_PAM = "pam";
	/**
	 * 搜索词
	 */
	public static final String UBCKW = "ubckw";
	
	/**
	 * 搜索词
	 */
	public static final String GLB_UBCKW_SCKW = "glb_sckw";
	
	/**
	 * 搜索词
	 */
	public static final String UBCKW_SCKW = "sckw";
	
	/**
	 * view
	 */
	public static final String FILTER_VIEW = "view";
	
	/**
	 * view
	 */
	public static final String FILTER_PAGE = "page";
	/**
	 * 子事件属性
	 */
	public static final String UBCTA = "ubcta";
	
	/**
	 * 用户登录的 session USER_ID
	 */
	public static final String UBCU = "ubcu";
	/**
	 * ubcd
	 */
	public static final String UBCD = "ubcd";
	
	/**
	 * ubcd
	 */
	public static final String UBCD_D = "glb_d";
	/**
	 * 事件类型
	 */
	public static final String UBCT = "ubct";
	
	/**
	 * 事件类型
	 */
	public static final String UBCT_T = "glb_t";
	/**
	 * 页面大类
	 */
	public static final String SPCB = "spcb";
	
	/**
	 * 页面大类
	 */
	public static final String SPCB_B = "b";
	/**
	 * 页面 小类
	 */
	public static final String SPCS = "spcs";
	
	/**
	 * 页面 小类
	 */
	public static final String SPCS_B002 = "b002";
	/**
	 * 外部来源渠道
	 */
	public static final String outerurl = "outerurl";
	/**
	 * 详情内部来源
	 */
	public static final String innerurl = "innerurl";
	/**
	 * 子事件详情
	 */
	public static final String UBCTD = "ubctd";
	
	/**
	 * 子事件详情
	 */
	public static final String UBCTD_X = "glb_x";
	/**
	 * 子事件详情搜索
	 */
	public static final String UBCTDS = "s";
	/**
	 * 事件子类型
	 */
	public static final String UBCICS = "ubcics";
	/**
	 * 页面位置
	 */
	public static final String UBCPL = "ubcpl";
	/**
	 * 页码
	 */
	public static final String UBCPN = "ubcpn";
	/**
	 * 当前页面总数量
	 */
	public static final String UBCTNP = "ubctnp";
	/**
	 * 点击位置
	 */
	public static final String UBCCP = "ubccp";
	/**
	 * 当前时间戳
	 */
	public static final String UBCCT = "ubcct";
	
	/**
	 * 商品所属分类Id
	 */
	public static final String UBCCSKU = "ubccsku";
	/**
	 * 购物车收藏数量
	 */
	public static final String UBAMOUNT = "ubamount";
	
	/**
	 * 购物车收藏数量
	 */
	public static final String UBAMOUNT_AT = "at";
	/**
	 * 分类id
	 */
	public static final String CATEGORY = "category";
	
	/**
	 * 分类
	 */
	public static final String CATEGORY_PC = "pc";
	/**
	 * 页面停留时间
	 * 
	 */
	public static final String wtime = "wtime";
	
	/**
	 * 页面模块
	 * 
	 */
	public static final String PAGEMODULE = "pagemodule";
	
	/**
	 * 页面模块
	 * 
	 */
	public static final String PAGEMODULE_PM = "glb_pm";
	
	/**
     * 产品页
     * 
     */
    public static final String PAGEMODULE_MP = "mp";
	
	/**
	 * 搜索点击位置
	 */
	public static final String UBCES = "ubces";
	
	/**
	 * ubcis=s 搜索
	 */
	public static final String s = "s";

	/**
	 * 搜索点击时间
	 */
	public static final String TIMESTAMP = "timestamp";

	/**
	 * 定义数据分隔符
	 */
	public static final String EVENTSEPERATE = "\\^A\\^";
	
	public static final String NGINX_SPLIT_SYMBOL = "^A^";
	
	public static final String EQUAL_SIGN = "=";

	public static final String UBCDPATTERN = "^\\d+$";
	
	public static final String CLOUD_MONITOR = "cloud_monitor";
    
    public static final String S_LOGSSS_COM = "s.logsss.com";

	/**
	 * 定义hive分隔符
	 */
	public static final String HIVE_SEPERATE = "\001";
	
	public static final String MD5_SEPERATE = "#";
	
	public static final String AD = "ad";
	
	public static final String RCMD = "rcmd";
	
	public static final String RCMD_MR = "mr";
	
	public static final String PRUDUCT = "product";
	
	public static final String PRUDUCT_MP = "mp";
	
	public static final String PURCHASE = "purchase";
	
	

	/**
	 * 站点 EB id everything buying
	 */

	public static final String SKUAMOUNT = "amount";
	public static final String SKUCOLOUR = "colour";

	
	/**
	 * 日期格式
	 */
	public static final String DDMMMYYYYHHMMSS = "dd/MMM/yyyy:HH:mm:ss Z";
	public static final String YYYYMMDD = "yyyy/MM/dd";
	public static final String YMD = "yyyyMMdd";
	public static final String smssimple = "yyyy-MM-dd";
	public static final String YMDH = "yyyy-MM-dd-HH";
	public static final String DATE_FORMAT_HIVE = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 编码格式
	 */
	public static final String UTF8 = "UTF-8";
	/**
	 * 状态码 200
	 */
	public static final String STATUESUCCEED = "^A^200^A^";
	
	/**
	 * 列表页数据
	 */
	public static final String LIST_DATA = "^A^1^A^";
	
	/**
     * 详细页数据
     */
	public static final String DETAIL_DATA = "^A^2^A^";
	
	public static final String ICON = "favicon.ico";
	/**
	 * 正则表达式 截取用户行为埋点值
	 */
	public static final String REGUBC = "(.*?)=(.*?)&";
	/**
	 * 正则表达式 截取request 数据
	 */
	public static final String REGREQUEST = "_ubc.gif?.*HTTP";
	/**
	 * 正则表达式 截取请求日期
	 */
	public static final String REGDATE = "\\[.*\\]";
	/**
	 * 商品曝光
	 */
	public static final String UBCT_IE = "ubct=ie";
	/**
	 * 商品曝光
	 */
	public static final String _UBCTIE = "_ubctie";
	/**
	 * 点击
	 */
	public static final String UBCT_IC = "ubct=ic";
	/**
	 * 点击
	 */
	public static final String _UBCTIC = "_ubctic";
	
	
	public static final String _UBCTD = "_ubctd";
	
	public static final String _UBCTD_ADT = "_ubctdADT";
	
	public static final String _UBCTD_ADF = "_ubctdADF";

	
	
	/**
	 * HIVE 结束标识符
	 */
	public static final String HIVE_END_SEPERATE = "#+@_";

	//http url 
	public static final String WWW="www";

	/**
	 * hive存储路径
	 * 
	 */
	public static final String PATH_SKUWEIGHT = "/esearch/esearch-stat/data/common/bdp-weight/sku/stat_glb_sku_weight_daily";
	
	public static final String PATH_KEYWORD = "/esearch/esearch-stat/data/common/bdp-keyword/sku/stat_glb_searchword_daily";
	
	public static final String PATH_USER_LIST_BUY = "/esearch/esearch-stat/data/common/bdp-sales/sku/stmtr_glb_userbuy_list_daily";
	
	public static final String PATH_USER_DETAIL_BUY = "/esearch/esearch-stat/data/common/bdp-sales/sku/stmtr_glb_userbuy_detail_daily";
	
	public static final String PATH_KEYWORD_CATEGORY = "/esearch/esearch-stat/data/common/bdp-keyword-category/sku/stat_glb_searchword_category_daily";
	
	public static final String PATH_USER_INFO = "/esearch/esearch-stat/data/common/bdp-userinfo/stmtr_glb_user_info";
	                                                       

	/**
	 * 分隔符
	 * 
	 */
	public static final String SEPERATE = "/";
	/**
	 * 连接符
	 * 
	 */
	public static final String JOINER = "_";
	
	public static final String IN_THE_LINE = "-";
	
	/**
	 * 分号 ;
	 */
	public static final String SEMICOLON=";";
	
	/**
	 * 时区
	 * 
	 */
	public static final String UTC = "UTC";
	/**
	 * 截取日志的 数据
	 * 
	 */
	public static final String request = "request";
	/** 
	 * @Fields timeMatcher : TODO(匹配 11/Jul/2016:00:03:42 +0000) 
	 */ 
	public static final String timeMatcher="\\d\\d/\\w*/\\d{4}:(\\d{2}:){2}\\d\\d\\s\\+\\d{4}";
	
	
	
	/** start new  **/
	
	/**
	 * 行为类型 fv是首次访问，ic是点击，ie是曝光
	 */
	public static final String GLB_T = "glb_t";
	/**
	 * user_id 用户网站编码，一般为几位的数字
	 */
	public static final String GLB_U = "glb_u";
	/**
	 * 搜索结果词 
	 *     1.搜索结果页面对应的搜索词 
	 *     2.搜索框回车后对应的实际搜索词，不一定是输入词
	 */
	public static final String GLB_SCKW = "glb_sckw";
	
	public static final String SCKW = "sckw";
	/**
	 * 搜索分类 搜索输入栏旁边的物品分类，只有GB有，其他网站该字段等于0
	 */
	public static final String GLB_SC = "glb_sc";
	/**
	 * 域名对应的编号
	 */
	public static final String GLB_D = "glb_d";
	/**
	 * 国家对应的编号
	 */
	public static final String GLB_DC = "glb_dc";
	/**
     * 当前时间戳
     */
	public static final String GLB_TM = "glb_tm";
	/**
     * 页面大类
     * 目前存在的页面大类中的一种
     */
	public static final String GLB_B = "glb_b";
	/**
     * 页面小类
     * 页面大类下面的页面小类
     */
	public static final String GLB_S = "glb_s";
	
	/**
     * 商品详情
     * 包含商品的SKU、数量信息等
     */
	public static final String GLB_SKUINFO = "glb_skuinfo";
	/**
     * 内部sku
     */
	public static final String SKU = "sku";
	/**
     * 商品数量
     * 加购加收藏等等涉及的商品数量
     */
	public static final String PAM = "pam";
	/**
     * 商品分类
     * 商品所属底层分类的主分类
     */
	public static final String PC = "pc";
	/**
     * 仓库信息
     * 暂时针对行为收集仓库信息，点击商品时收集商品对应仓库信息，
     *  点击非商品区域收集当前页面仓库或者默认仓库信息
     */
	public static final String K = "k";
	/**
     * 子事件详情
     * 页面详细点击的词
     */
	public static final String GLB_X = "glb_x";
	/**
     * 搜索输入词
     * 用户自主输入的词，为输入完成时显示的词汇，如果用户直接点击推荐商品，该字段为空。
     */
	public static final String GLB_SIWS = "glb_siws";
	/**
     * 搜索类型
     * 分为联想搜索、历史搜索和推荐搜索等类型
     */
	public static final String GLB_SK = "glb_sk";
	/**
     * 搜索点击位置
     * 联想搜索或者推荐搜索的实际点击排列第几位
     */
	public static final String GLB_SL = "glb_sl";
	/**
     * 页面停留时间
     * 在某页面的停留时间统计，单位是毫秒，小数点后面不计
     */
	public static final String GLB_W = "glb_w";
	
	/**
     * glb_ubcta:{"rank":1,"sckw":"shirt"}
     */
	public static final String GLB_UBCTA = "glb_ubcta";
	/**
     * glb_ubcta:{"at" : "2060"}
     * 商品数量
     * 搜索结果页被搜索词的搜索结果数量
     */
	public static final String AT = "at";
	/**
     * glb_ubcta:{"rank":1,"sckw":"shirt"}
     * 橱窗位置
     */
	public static final String RANK = "rank";
	
	/**
     * glb_filter:{"view":"36","page":"1","sort":"relevance"}
     * 
     */
	public static final String GLB_FILTER = "glb_filter";
	/**
     * glb_filter:{"view":"36","page":"1","sort":"relevance"}
     * 一页最多几个商品
     */
	public static final String VIEW = "view";
	/**
     * glb_filter:{"view":"36","page":"1","sort":"relevance"}
     * 当前处于第几页
     */
	public static final String PAGE = "page";
	/**
     * glb_filter:{"view":"36","page":"1","sort":"relevance"}
     * 按照什么排序
     */
	public static final String SORT = "sort";
	
	
	/**
     * 页面模块
     * 页面大类会分模块统计
     */
	public static final String GLB_PM = "glb_pm";
	/**
     * 页面sku
     * 记录商详页、收藏夹、购物车、支付结果页内sku，没有则不记录
     */
	public static final String GLB_KSKU = "glb_ksku";
	/**
     * 当前页面url
     */
	public static final String GLB_CL = "glb_cl";
	/**
     * 上个页面的url
     */
	public static final String GLB_PL = "glb_pl";
	/**
     * 用户会话id
     * 用户sessionID
     */
	public static final String GLB_OI = "glb_oi";
	/**
     * cookie id
     * 用户的cookie id，不清除缓存会一直存在
     */
	public static final String GLB_OD = "glb_od";
	/**
     * 外部来源和着陆页详情url组合
     */
	public static final String GLB_OSR = "glb_osr";
	
	/**
	 * 页码
	 */
	public static final String GLB_P = "glb_p";
	
	/**
	 * linkid 被记录在cookie中，可直接获取
	 */
	public static final String GLB_OLK = "glb_olk";
	
	/**
     * ubct=ic 点击
     */
    public static final String ic = "ic";
    /**
     * ubct=ie 商品曝光
     */
    public static final String ie = "ie";
    /**
     * ubct=fv 首次访问
     */
    public static final String fv = "fv";
    /**
     * ubctd=s 搜索页
     */
    public static final String search = "search";
    /**
     * ubctd=ADT 加入购物车
     */
    public static final String ADT = "ADT";
    /**
     * ubcics=ST 搜索页
     */
    public static final String ST = "ST";
    /**
     * ubctd=ADF 加入收藏夹
     */
    public static final String ADF = "ADF";
    
    /**
     * glb_pm=mp 页面模块值
     */
    public static final String MP = "mp";
    
    /**
     * glb_pm=mr 页面模块值
     */
    public static final String MR = "mr";
    
    /**
     * 文档没有注释
     */
    public static final String GCLID = "gclid";
    
    /**
     * 首页=a
     */
    public static final String A = "a";
    /**
     * 列表页=b
     */
    public static final String B = "b";
    /**
     * 分类页=b01
     */
    public static final String B01 = "b01";
    /**
     * 搜索页=b02
     */
    public static final String B02 = "b02";
    /**
     * 专题页=b03
     */
    public static final String B03 = "b03";
    /**
     * 新品页=b04
     */
    public static final String B04 = "b04";
    /**
     * 清仓页=b05
     */
    public static final String B05 = "b05";
    /**
     * 促销页=b06
     */
    public static final String B06 = "b06";
    /**
     * 品牌页=b07
     */
    public static final String B07 = "b07";
	
	/** end new  **/
}