package com.globalegrow.esearch.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

public class ValidateUtil {
	/**
	 * 验证网站代码数据是否合法
	 * @param data
	 * @return
	 */
	public static boolean ubcdTest(String data) {
		String pattern = "^[0-9]*[1-9][0-9]*$";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(data);
		boolean result = m.matches();
		return result;
	}
}
