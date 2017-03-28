package com.wdcloud.solr.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 文件名称： com.wdcloud.solr.util.CustomDateUtils.java</br>
 * 初始作者： xiangzhenhai</br>
 * 创建日期： 2017年2月15日</br>
 * 功能说明： 自定义日期工具类 <br/>
 */
public class CustomDateUtils {

	/**
	 * 方法描述: [判断某字符串是否可以转换为日期类型]</br>
	 * 初始作者: xiangzhenhai<br/>
	 * 创建日期: 2017年2月15日-上午9:59:11<br/>
	 * 开始版本: 1.0.0<br/>
	 */
	public static boolean checkIsDate(String dateStr) {

		if (StringUtils.isBlank(dateStr)) {
			return false;
		}

		Date returnDate = null;
		try {
			if (dateStr.length() == 4) {
				returnDate = new SimpleDateFormat("yyyy").parse(dateStr);
			} else if (dateStr.length() == 6) {
				returnDate = new SimpleDateFormat("yyyyMM").parse(dateStr);
			} else if (dateStr.length() == 8) {
				returnDate = new SimpleDateFormat("yyyyMMdd").parse(dateStr);
			} else if (dateStr.length() == 10) {
				returnDate = new SimpleDateFormat("yyyyMMddHH").parse(dateStr);
			} else if (dateStr.length() == 12) {
				returnDate = new SimpleDateFormat("yyyyMMddHHmm").parse(dateStr);
			} else if (dateStr.length() == 14) {
				returnDate = new SimpleDateFormat("yyyyMMddHHmmss").parse(dateStr);
			} else if (dateStr.length() == 17) {
				returnDate = new SimpleDateFormat("yyyyMMddHHmmssSSS").parse(dateStr);
			}
		} catch (Exception e) {
			return false;
		}
		if (returnDate != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 方法描述: [将字符串转换成日期类型]</br>
	 * 初始作者: xiangzhenhai<br/>
	 * 创建日期: 2017年2月15日-上午10:26:47<br/>
	 * 开始版本: 1.0.0<br/>
	 */
	public static Date convertStrToDate(String dateStr) {

		if (StringUtils.isBlank(dateStr)) {
			return null;
		}

		Date returnDate = null;
		try {
			if (dateStr.length() == 4) {
				returnDate = new SimpleDateFormat("yyyy").parse(dateStr);
			} else if (dateStr.length() == 6) {
				returnDate = new SimpleDateFormat("yyyyMM").parse(dateStr);
			} else if (dateStr.length() == 8) {
				returnDate = new SimpleDateFormat("yyyyMMdd").parse(dateStr);
			} else if (dateStr.length() == 10) {
				returnDate = new SimpleDateFormat("yyyyMMddHH").parse(dateStr);
			} else if (dateStr.length() == 12) {
				returnDate = new SimpleDateFormat("yyyyMMddHHmm").parse(dateStr);
			} else if (dateStr.length() == 14) {
				returnDate = new SimpleDateFormat("yyyyMMddHHmmss").parse(dateStr);
			} else if (dateStr.length() == 17) {
				returnDate = new SimpleDateFormat("yyyyMMddHHmmssSSS").parse(dateStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnDate;
	}

	/**
	 * 方法描述: [将map里的所有value值为日期字符串的转为date]</br>
	 * 初始作者: xiangzhenhai<br/>
	 * 创建日期: 2017年2月15日-下午1:27:18<br/>
	 * 开始版本: 1.0.0<br/>
	 */
	public static void mapValuesToDate(Map<String, Object> map) {
		if (map != null) {
			for (String key : map.keySet()) {
				if (map.get(key) instanceof String && checkIsDate((String) map.get(key))) {
					// 如果字符串是日期格式字符串，则转成Date
					map.put(key, convertStrToDate((String) map.get(key)));
				} else if (map.get(key) instanceof Map) {
					// 如果是map，则递归
					mapValuesToDate((Map<String, Object>) map.get(key));
				}
			}
		}
	}
}
