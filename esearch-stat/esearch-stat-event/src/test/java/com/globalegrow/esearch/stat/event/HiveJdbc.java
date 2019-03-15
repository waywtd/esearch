package com.globalegrow.esearch.stat.event;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <pre>
 * 
 *  File: HiveJsbc.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2017年9月20日				lizhaohui				Initial.
 *
 * </pre>
 */
public class HiveJdbc
{

    private static String Driver = "org.apache.hive.jdbc.HiveDriver";
    private static String URL = "jdbc:hive2://cdh-search02:10000/es_gearbest_10002";
    private static String username = "";
    private static String password = "";

    public static void main(String[] args)
    {
        Connection conn = null;

        try
        {
            Class.forName(Driver);
            conn = DriverManager.getConnection(URL, username, password);
            Statement stat=conn.createStatement();
            String sql01="select * from stmtr_click_sku_daily";
            ResultSet rs=stat.executeQuery(sql01);
            while(rs.next()){
                System.out.println(rs.getString(1)+"-->"+rs.getString(2)+"-->"+rs.getString(3));
            }
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }finally{
            if(conn!=null){
                try
                {
                    conn.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}