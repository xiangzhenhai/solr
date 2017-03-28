package com.wdcloud.solr.action;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wdcloud.solr.entity.ParamModel;
import com.wdcloud.solr.entity.Result;
import com.wdcloud.solr.service.DocService;
import com.wdcloud.solr.util.PropertyPlaceholder;

/**
 * 文件名称： action.CoreAction.java</br>
 * 初始作者： xiangzhenhai</br>
 * 创建日期： 2017年2月9日</br>
 * 功能说明： 查询实现类 <br/>
 */
@Controller
@RequestMapping("/cores/{coreName}/docs")
public class DocAction {

	private Logger logger = LoggerFactory.getLogger(DocAction.class);

	// 获取solr服务地址
	public static final String solrURL = PropertyPlaceholder.getProperty("solrURL");

	@Autowired
	private DocService docService;

	/**
	 * 方法描述: [CoreAction类测试用]</br>
	 * 初始作者: xiangzhenhai<br/>
	 * 创建日期: 2017年2月9日-下午3:50:45<br/>
	 * 开始版本: 1.0.0<br/>
	 */
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ResponseBody
	public Result queryTest() {
		return new Result(true, "this is a CoreAction.java test message", null);
	}

	/**
	 * 方法描述: [通过id查询单个索引数据]</br>
	 * 初始作者: xiangzhenhai<br/>
	 * 创建日期: 2017年2月13日-下午2:01:03<br/>
	 * 开始版本: 1.0.0<br/>
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Result queryById(@PathVariable String coreName, @PathVariable String id) {

		Result result = new Result();

		// 验证输入参数
		if (StringUtils.isBlank(coreName) || StringUtils.isBlank(id)) {
			result.setSuccess(false);
			result.setMessage("参数异常，请检查参数");
		} else {
			// 调用服务获取输出参数
			result = docService.queryById(solrURL, coreName, id);
		}

		return result;
	}

	/**
	 * 方法描述: [设置搜索条件，查询数据]</br>
	 * 初始作者: xiangzhenhai<br/>
	 * 创建日期: 2017年2月9日-下午3:48:10<br/>
	 * 开始版本: 1.0.0<br/>
	 */
	@RequestMapping(value = "/query", method = RequestMethod.POST)
	@ResponseBody
	public Result query(@PathVariable String coreName, @RequestBody ParamModel paramModel) {

		Result result = new Result();

		// 验证输入参数
		if (StringUtils.isBlank(coreName)) {
			result.setSuccess(false);
			result.setMessage("参数异常，请检查参数");
		} else {

			// 组装输入参数
			if (paramModel.getHlPre() == null) {
				paramModel.setHlPre("<font style=\"color:red;\">");
			}
			if (paramModel.getHlPost() == null) {
				paramModel.setHlPost("</font>");
			}
			if (StringUtils.isBlank(paramModel.getHlKey())) {
				paramModel.setHlKey("id");
			}

			// 调用服务获取输出参数
			result = docService.query(solrURL, coreName, paramModel);
		}

		return result;
	}

	/**
	 * 方法描述: [写入数据]</br>
	 * 初始作者: xiangzhenhai<br/>
	 * 创建日期: 2017年2月13日-上午10:19:03<br/>
	 * 开始版本: 1.0.0<br/>
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Result add(@PathVariable String coreName, @RequestBody ParamModel paramModel) {

		Result result = new Result();

		// 验证输入参数
		if (StringUtils.isBlank(coreName) || paramModel.getAddMap() == null && paramModel.getAddList() == null) {
			result.setSuccess(false);
			result.setMessage("参数异常，请检查参数");
		} else {
			// 调用服务获取输出参数
			result = docService.add(solrURL, coreName, paramModel);
		}

		return result;
	}

	/**
	 * 方法描述: [更新数据]</br>
	 * 初始作者: xiangzhenhai<br/>
	 * 创建日期: 2017年2月13日-下午2:18:06<br/>
	 * 开始版本: 1.0.0<br/>
	 */
	@RequestMapping(method = RequestMethod.PUT)
	@ResponseBody
	public Result update(@PathVariable String coreName, @RequestBody ParamModel paramModel) {

		Result result = new Result();

		// 验证输入参数
		if (StringUtils.isBlank(coreName) || paramModel.getUpdateMap() == null && paramModel.getUpdateList() == null) {
			result.setSuccess(false);
			result.setMessage("参数异常，请检查参数");
		} else {
			// 调用服务获取输出参数
			result = docService.update(solrURL, coreName, paramModel);
		}

		return result;
	}

	/**
	 * 方法描述: [删除数据]</br>
	 * 初始作者: xiangzhenhai<br/>
	 * 创建日期: 2017年2月13日-上午11:09:40<br/>
	 * 开始版本: 1.0.0<br/>
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	@ResponseBody
	public Result delete(@PathVariable String coreName, @RequestBody ParamModel paramModel) {

		Result result = new Result();

		// 验证输入参数
		if (StringUtils.isBlank(coreName)
				|| StringUtils.isBlank(paramModel.getId()) && StringUtils.isBlank(paramModel.getDeleteQuery())) {
			result.setSuccess(false);
			result.setMessage("参数异常，请检查参数");
		} else {
			// 调用服务获取输出参数
			result = docService.delete(solrURL, coreName, paramModel);
		}

		return result;
	}

}
