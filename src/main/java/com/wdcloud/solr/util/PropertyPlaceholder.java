package com.wdcloud.solr.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * 文件名称： com.wdcloud.solr.util.PropertyPlaceholder.java</br>
 * 初始作者： xiangzhenhai</br>
 * 创建日期： 2017年2月14日</br>
 * 功能说明： 重写PropertyPlaceholderConfigurer，读取properties文件属性 <br/>
 */
public class PropertyPlaceholder extends PropertyPlaceholderConfigurer {

	private static Map<String, String> propertyMap;

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
			throws BeansException {
		super.processProperties(beanFactoryToProcess, props);
		propertyMap = new HashMap<String, String>();
		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			String value = props.getProperty(keyStr);
			propertyMap.put(keyStr, value);
		}
	}

	// static method for accessing context properties
	public static String getProperty(String name) {
		return propertyMap.get(name);
	}
}
