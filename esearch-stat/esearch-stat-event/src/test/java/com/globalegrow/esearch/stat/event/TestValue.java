package com.globalegrow.esearch.stat.event;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONArray;
import com.globalegrow.esearch.constant.Constant;
import com.globalegrow.esearch.util.MapUtil;

/**
 * <pre>
 * 
 *  File: TestValue.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2017年6月26日				lizhaohui				Initial.
 *
 * </pre>
 */
public class TestValue
{
    public static void main(String[] args) throws Exception
    {
       /*String str="2-->150154603100022017-07-04wwwADT99999_ubctdADT";
       System.out.println(str.toString().split(Constant.HIVE_SEPERATE).length);
       System.out.println(str.contains(Constant._UBCTD_ADT) && str.toString().split(Constant.HIVE_SEPERATE).length==6);
       System.out.println(Float.MAX_VALUE);
       
        testBoolean(); 
        testLog();*/
        /*GetDomainName("http://www.gearbest.com");*/
        List<Map<String,String>> list=new ArrayList<Map<String,String>>();
        Map<String,String> map=new HashMap<String,String>();
        map.put("a","1");
        map.put("b","2");
        map.put("c","3");
        map.put("d","4");
        map.put("e","5");
        map.put("f","6");
        list.add(map);
        String json=JSONArray.toJSONString(list);
        List<Map<String,String>> result=(List<Map<String, String>>) JSONArray.parse(json);
        for(Map<String,String> maplist:result){
            System.out.println(maplist.get("a"));
        }
    }
    
    
    public static void testLog(){
        String str="172.31.57.92^A^-^A^[12/Sep/2017:03:54:01 +0000]^A^\"GET /_ubc.gif?glb_t=ic&glb_w=95857&glb_tm=1505188347670&glb_siws=faca&glb_sc=0&glb_x=search&glb_u=17319530&glb_oi=dmkrpsr2342fv22ui46bk24e11&glb_d=10002&glb_s=b03&glb_b=b&glb_olk=759801&glb_od=100021504795355219fa7blma4039513739530037206&glb_osr=ol%3Dhttps%3A%2F%2Fwww.google.com.br%2F%7Chref%3Dhttps%3A%2F%2Fwww.gearbest.com%2F%3Fvip%3D759801%26gclid%3DCj0KCQjw6NjNBRDKARIsAFn3NMrQmaip20e3Krz2tZEZDZdcxp9MXsKJ6e3_LV7uBNmt6Py4pcgYvCsaAhPhEALw_wcB&glb_cl=https%3A%2F%2Fwww.gearbest.com%2Fflash-sale.html&glb_pl=https%3A%2F%2Fwww.gearbest.com%2F%25C3%25B3culos-de-sol-_gear%2F&glb_sckw=Oculos&glb_ubcta=%7B%22at%22%20%3A%20%22402%22%7D HTTP/1.1\"^A^200^A^372^A^\"https://br.gearbest.com/oculos-_gear/\"^A^\"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36\"^A^s.logsss.com^A^201.69.63.106, 2.16.188.197, 186.192.142.151^A^201.69.63.106^A^BR^A^Brazil^A^-^A^-^A^-^A^-";
        Map<String, String> eventmap = new LinkedHashMap<String, String>();
        MapUtil.getEventmap(str, eventmap);
        System.out.println(eventmap);
    }
    
    
    public static void testBoolean(){
        String str="172.31.31.247^A^-^A^[05/Sep/2017:11:22:05 +0000]^A^\"GET /_ubc.gif?glb_t=ic&glb_w=22824&glb_tm=1504610522287&glb_pm=mp&glb_skuinfo={%22sku%22:%22208032901%22,%22pam%22:0,%22pc%22:%2212245%22,%22k%22:0}&glb_ubcta={%22rank%22:16}&glb_sckw=luftbefeuchter&glb_x=sku&glb_filter=%7B%22view%22%3A%2236%22%2C%22page%22%3A%222%22%2C%22sort%22%3Anull%7D&glb_sc=0&glb_u=16602947&glb_oi=t9gpotf823ouostuui6dp8l841&glb_d=10002&glb_s=b02&glb_b=b&glb_od=10002150460980704106675677271691077&glb_osr=ol%3Dhttps%3A%2F%2Fwww.google.de%7Chref%3Dhttps%3A%2F%2Fde.gearbest.com%2F&glb_cl=https%3A%2F%2Fde.gearbest.com%2Fluftbefeuchter-_gear%2F2.html&glb_pl=https%3A%2F%2Fde.gearbest.com%2Fluftbefeuchter-_gear%2F HTTP/1.1\"^A^200^A^372^A^\"https://de.gearbest.com/luftbefeuchter-_gear/2.html\"^A^\"Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:45.0) Gecko/20100101 Firefox/45.0\"^A^s.logsss.com^A^194.113.41.18, 23.215.129.150, 104.121.156.54^A^194.113.41.18^A^DE^A^Germany^A^-^A^-^A^-^A^-";
        System.out.println(str.contains(Constant.STATUESUCCEED) && (!str.contains(Constant.ICON)) && str.contains("www.gearbest.com"));
    }
    
    public static void testHTTP(){
            String pattern = "https://s.logsss.com/_ubc.gif?t=ic&w=10485&tm=1498629620820&skuinfo={\"sku\":\"212551710\",\"pam\":\"100000\",\"pc\":\"12093\",\"k\":\"21\"}&pm=mb&x=ADF&u=15627264&oi=biniacdarninhthpinns4ibbc5&d=10002&b=c&ksku=631691&cl=http://www.gearbest.com/sleeveless-dresses/pp_631691.html&pl=http://www.gearbest.com/dress-_gear/";
                             
            Pattern p = Pattern.compile(pattern);
            String line = "";
            Matcher m = p.matcher(line);

            if(m.find()){
                //匹配结果
                System.out.println(m.group());
            }
        }
    
    
    public static void GetDomainName(String http) throws Exception
    {
        URL url = new URL(http);
        System.out.println("URL 为：" + url.toString());
        System.out.println("协议为：" + url.getProtocol());
        System.out.println("验证信息：" + url.getAuthority());
        System.out.println("文件名及请求参数：" + url.getFile());
        System.out.println("主机名：" + url.getHost());
        System.out.println("路径：" + url.getPath());
        System.out.println("端口：" + url.getPort());
        System.out.println("默认端口：" + url.getDefaultPort());
        System.out.println("请求参数：" + url.getQuery());
        System.out.println("定位位置：" + url.getRef());
    }
}
