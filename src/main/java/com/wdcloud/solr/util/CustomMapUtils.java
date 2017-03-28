package com.wdcloud.solr.util;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

/**
 * 文件名称： com.wdcloud.solr.util.MapUtil.java</br>
 * 初始作者： xiangzhenhai</br>
 * 创建日期： 2017年2月10日</br>
 * 功能说明： 自定义map相关的工具方法 <br/>
 */
public class CustomMapUtils {

	/**
	 * 方法描述: [利用org.apache.commons.beanutils 工具类实现 Map --> Bean]</br>
	 * 初始作者: xiangzhenhai<br/>
	 * 创建日期: 2017年2月10日-下午4:02:41<br/>
	 * 开始版本: 1.0.0<br/>
	 */

	public static Object mapToObject(Map<String, Object> map, Object obj) {

		try {
			BeanUtils.populate(obj, map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return obj;
	}

}
