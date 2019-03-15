package com.globalegrow.esearch.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <pre>
 *
 *  File: com.globalegrow.esearch.hive.udf.MD5UDF
 *
 *  Copyright (c) MD5UDF, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/7/18				nieruiqun				Initial.
 *
 * <pre>
 */
public class MD5UDF extends UDF {

    public String evaluate(String plainText) {
        StringBuffer buf = new StringBuffer("");
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());

            byte[] b = md.digest();
            for (int offset = 0; offset < b.length; offset++) {
                int i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return buf.toString();
    }

}
