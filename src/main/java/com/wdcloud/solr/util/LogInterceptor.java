package com.wdcloud.solr.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * 文件名称： com.wdcloud.solr.util.LogInterceptor.java</br>
 * 初始作者： xiangzhenhai</br>
 * 创建日期： 2017年2月14日</br>
 * 功能说明： 日志拦截工具类 <br/>
 */
@Aspect
public class LogInterceptor {

	private Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

	// 定义切点
	public static final String actionPointcut = "execution(* com.wdcloud.solr.action.*Action.*(..))";
	public static final String servicePointcut = "execution(* com.wdcloud.solr.service.*Service.*(..))";

	@Before(actionPointcut)
	public void before(JoinPoint point) {
		logger.info("----------method " + point.getSignature().getDeclaringTypeName() + "."
				+ point.getSignature().getName() + " start----------");
		logger.info("----------param:" + JSONObject.toJSONString(point.getArgs()) + "----------");
	}

	// @After(actionPointcut)
	// public void after(JoinPoint point) {
	// logger.info("----------method " +
	// point.getSignature().getDeclaringTypeName() + "."
	// + point.getSignature().getName() + " end----------");
	// }

	@AfterReturning(pointcut = actionPointcut, returning = "returnValue")
	public void afterReturning(JoinPoint point, Object returnValue) {
		logger.info("----------result:" + JSONObject.toJSONString(returnValue) + "----------");
		logger.info("----------method " + point.getSignature().getDeclaringTypeName() + "."
				+ point.getSignature().getName() + " end----------");
	}

	// 当有多个切点的时候，用"||"连接切点即可
	@AfterThrowing(pointcut = actionPointcut + "||" + servicePointcut, throwing = "exception")
	public void afterThrowing(JoinPoint point, Exception exception) {
		logger.info("----------exception:" + exception.toString() + "----------");
	}

}
