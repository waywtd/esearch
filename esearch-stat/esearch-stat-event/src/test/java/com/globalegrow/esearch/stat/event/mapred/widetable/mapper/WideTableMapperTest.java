package com.globalegrow.esearch.stat.event.mapred.widetable.mapper;

import com.globalegrow.esearch.constant.Constant;
import com.globalegrow.esearch.util.MapUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.Text;
import org.junit.Test;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *
 *  File: WideTableMapperTest.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018年9月26日			nieruiqun				Initial.
 *
 * </pre>
 */
public class WideTableMapperTest {

    private String line = "66.249.83.81^A^1538110800.193^A^GET^A^/_ubc.gif?glb_t=ie&glb_tm=1538110798279&glb_oi=3b606c7775b65c4086da164823988570&glb_d=10002&glb_b=c&glb_p=p009731154715&glb_plf=m&glb_dc=1303&glb_ksku=278941702&glb_skuInfo={%22sku%22:%22278941702%22,%22pam%22:1,%22pc%22:%2211240%22,%22k%22:%221433363%22,%22price%22:5.91}&glb_w=56&glb_od=foywicpklcqg1538110798223&glb_osr_referrer=originalurl&glb_osr_landing=https%3A%2F%2Fm-es.gearbest.com%2Fheadsets%2Fpp_009731154715.html%3Fwid%3D1433363&glb_cl=https%3A%2F%2Fm-es.gearbest.com%2Fheadsets%2Fpp_009731154715.html%3Fwid%3D1433363^A^200^A^372^A^Mozilla/5.0 (Linux; Android 4.2.1; en-us; Nexus 5 Build/JOP40D) AppleWebKit/535.19 (KHTML, like Gecko; googleweblight) Chrome/38.0.1025.166 Mobile Safari/535.19^A^DORREGO^A^AR^A^";

    @Test
    public void map() throws Exception {
        // 转义url
        line = URLDecoder.decode(line, "UTF-8");

        Text tValue = new Text();
        //根据空格将这一行切分成单词
        String[] strings = line.split(Constant.EVENTSEPERATE);
        for(String str : strings){
            if(str.contains("_ubc.gif?")){
                String info = StringUtils.substringAfter(str, "_ubc.gif?");
                if(StringUtils.isNotBlank(info)){
                    String [] infos = info.split("&");
                    Map<String, String> map = MapUtil.getWideTableMap();
                    for(String message : infos){
                        String mapKey = StringUtils.lowerCase(StringUtils.substringBefore(message,Constant.EQUAL_SIGN));
                        String mapValue = StringUtils.trim(StringUtils.substringAfter(message,Constant.EQUAL_SIGN));
                        map.put(mapKey, StringUtils.isBlank(mapValue) ? "NILL" : mapValue);
                    }
                    map.put("ip", strings[0]);
                    map.put("city", strings[strings.length-2]);
                    map.put("country", strings[strings.length-1]);
                    System.out.println(map);
                    final List<String> list = new ArrayList<>(map.size());
                    map.forEach( (k, v) ->  list.add(v) );
                    tValue.set(String.join(Constant.HIVE_SEPERATE, list));
                    System.out.println(tValue);
                }
            }
        }
    }

}