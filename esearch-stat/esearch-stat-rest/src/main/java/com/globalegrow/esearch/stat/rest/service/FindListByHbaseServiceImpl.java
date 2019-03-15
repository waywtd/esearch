package com.globalegrow.esearch.stat.rest.service;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.*;

public class FindListByHbaseServiceImpl {

    /**
     *
     * @param tablename 表名
     * @param lang 国家站
     * @param cat 分类id
     * @param amount 1:有结果的，0：无结果的，2：全部
     * @param date1 起始日期
     * @param date1 结束日期
     * @param platform 平台
     * @return 查询到的结果的集合
     */
    public List<Map<String,String>> findDataByCondition(String tablename,String lang,String cat,String amount,String date1,String date2,String platform){
        //指定的配置信息: ZooKeeper
        Configuration conf = new Configuration();
        conf.set("hbase.zookeeper.quorum", "10.40.6.170,10.40.6.171,10.40.6.172");
        try {
            HTable table = new HTable(conf,tablename);
            List<Filter> list = new ArrayList<Filter>();


            SingleColumnValueFilter filter1 = null;
            if(lang != null && !lang.equals("")){    //国家站---
                filter1 = new SingleColumnValueFilter(Bytes.toBytes("cf1"),
                        Bytes.toBytes("lang"), //列名
                        CompareFilter.CompareOp.EQUAL, //比较运算符
                        Bytes.toBytes(lang));
                list.add(filter1);
            }
            SingleColumnValueFilter filter2 = null;
            if(cat != null && !cat.equals("")){      //分类id
                filter2 = new SingleColumnValueFilter(Bytes.toBytes("cf1"),
                        Bytes.toBytes("cat_id"), //列名
                        CompareFilter.CompareOp.EQUAL, //比较运算符
                        Bytes.toBytes(cat));
                list.add(filter2);
            }
            SingleColumnValueFilter filter3 = null;
            if(amount != null && !amount.equals("")){     //放回结果数
                if(Integer.parseInt(amount) == 1){
                    filter3 = new SingleColumnValueFilter(Bytes.toBytes("result"),
                            Bytes.toBytes("amount"), //列名
                            CompareFilter.CompareOp.GREATER, //比较运算符
                            Bytes.toBytes(0));
                }else if(Integer.parseInt(amount) == 0){
                    filter3 = new SingleColumnValueFilter(Bytes.toBytes("result"),
                            Bytes.toBytes("amount"), //列名
                            CompareFilter.CompareOp.EQUAL, //比较运算符
                            Bytes.toBytes(0));
                }else{
                    filter3 = null;
                }
            }
            list.add(filter3);

            SingleColumnValueFilter filter5 = null;
            if(platform != null && !platform.equals("")){      //平台---
                filter5 = new SingleColumnValueFilter(Bytes.toBytes("cf1"),
                        Bytes.toBytes("platform"), //列名
                        CompareFilter.CompareOp.EQUAL, //比较运算符
                        Bytes.toBytes(platform));
            }
            list.add(filter5);

            SingleColumnValueFilter filter4 = null;
            SingleColumnValueFilter filter6 = null;
            if((date1 != null && !date1.equals("")) && (date2 == null && date2.equals(""))){    //日期---
                filter4 = new SingleColumnValueFilter(Bytes.toBytes("cf1"),     //列族
                        Bytes.toBytes("date"), //列名
                        CompareFilter.CompareOp.EQUAL, //比较运算符
                        Bytes.toBytes(date1));
            }else if((date2 != null && !date2.equals("")) && (date1 == null && date1.equals(""))){
                filter4 = new SingleColumnValueFilter(Bytes.toBytes("cf1"),     //列族
                        Bytes.toBytes("date"), //列名
                        CompareFilter.CompareOp.EQUAL, //比较运算符
                        Bytes.toBytes(date2));
            }else if((date2 != null && !date2.equals("")) && (date1 != null && !date1.equals("")) && date1.equals(date2)){
                filter4 = new SingleColumnValueFilter(Bytes.toBytes("cf1"),     //列族
                        Bytes.toBytes("date"), //列名
                        CompareFilter.CompareOp.EQUAL, //比较运算符
                        Bytes.toBytes(date1));
            }else if((date2 != null && !date2.equals("")) && (date1 != null && !date1.equals("")) && !date1.equals(date2)){
                long one = Long.valueOf(date1.replaceAll("[-\\s:]",""));
                long two = Long.valueOf(date2.replaceAll("[-\\s:]",""));
                if(one>two){
                    filter4 = new SingleColumnValueFilter(Bytes.toBytes("cf1"),     //列族
                            Bytes.toBytes("date"), //列名
                            CompareFilter.CompareOp.LESS, //比较运算符
                            Bytes.toBytes(date1));
                    filter6 = new SingleColumnValueFilter(Bytes.toBytes("cf1"),     //列族
                            Bytes.toBytes("date"), //列名
                            CompareFilter.CompareOp.GREATER, //比较运算符
                            Bytes.toBytes(date2));
                }else{
                    filter4 = new SingleColumnValueFilter(Bytes.toBytes("cf1"),     //列族
                            Bytes.toBytes("date"), //列名
                            CompareFilter.CompareOp.LESS, //比较运算符
                            Bytes.toBytes(date2));
                    filter6 = new SingleColumnValueFilter(Bytes.toBytes("cf1"),     //列族
                            Bytes.toBytes("date"), //列名
                            CompareFilter.CompareOp.GREATER, //比较运算符
                            Bytes.toBytes(date1));
                }
            }else{

            }
            list.add(filter4);
            list.add(filter6);

            for(Filter f : list){
                if(f == null){
                    list.remove(f);
                }
            }
            FilterList f=new FilterList(list);
            Scan s = new Scan();
            s.setFilter(f);
            ResultScanner rs = table.getScanner(s);
            List<Map<String,String>> l = new ArrayList<Map<String,String>>();
            for(Result r: rs){
                String keyword = Bytes.toString(r.getValue(Bytes.toBytes("cf1"), Bytes.toBytes("keyword")));
                String click_times = Bytes.toString(r.getValue(Bytes.toBytes("result"), Bytes.toBytes("click_times")));
                String click_times_uv = Bytes.toString(r.getValue(Bytes.toBytes("result"), Bytes.toBytes("click_times_uv")));
                String search_times = Bytes.toString(r.getValue(Bytes.toBytes("result"), Bytes.toBytes("search_times")));
                String search_times_uv = Bytes.toString(r.getValue(Bytes.toBytes("result"), Bytes.toBytes("search_times_uv")));
                amount = Bytes.toString(r.getValue(Bytes.toBytes("result"), Bytes.toBytes("amount")));
                lang = Bytes.toString(r.getValue(Bytes.toBytes("cf1"), Bytes.toBytes("lang")));
                String cat_id = Bytes.toString(r.getValue(Bytes.toBytes("cf1"), Bytes.toBytes("cat_id")));
                String date = Bytes.toString(r.getValue(Bytes.toBytes("cf1"), Bytes.toBytes("date")));
                platform = Bytes.toString(r.getValue(Bytes.toBytes("cf1"), Bytes.toBytes("platform")));
                Map<String,String> m = new HashMap<String,String>();
                m.put("keyword",keyword);
                m.put("click_times",click_times);
                m.put("click_times_uv",click_times_uv);
                m.put("search_times",search_times);
                m.put("search_times_uv",search_times_uv);
                m.put("amount",amount);
                m.put("lang",lang);
                m.put("cat_id",cat_id);
                m.put("date",date);
                m.put("platform",platform);

                l.add(m);
            }
            table.close();
            return l;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
