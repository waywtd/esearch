package com.globalegrow.esearch;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * 
 *  File: ListTest.java
 * 
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 * 
 *  Description:
 *  TODO
 * 
 *  Revision History
 *  Date,					Who,					What;
 *  2017年11月17日				lizhaohui				Initial.
 *
 * </pre>
 */
public class ListTest
{

    public static void main(String[] args)
    {
        List<String> list=new ArrayList<String>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");
        list.add("f");
        System.out.println(list.contains("A"));
    }
    
}