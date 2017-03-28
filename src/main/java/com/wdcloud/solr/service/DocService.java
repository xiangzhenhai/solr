package com.wdcloud.solr.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.wdcloud.solr.entity.ParamModel;
import com.wdcloud.solr.entity.Result;
import com.wdcloud.solr.util.CustomDateUtils;

/**
 * 文件名称： com.wdcloud.solr.service.QueryService.java</br>
 * 初始作者： xiangzhenhai</br>
 * 创建日期： 2017年2月9日</br>
 * 功能说明： 查询实现类 <br/>
 */
@Service
public class DocService {

	private Logger logger = LoggerFactory.getLogger(DocService.class);

	/**
	 * 方法描述: [通过id查询solr数据]</br>
	 * 初始作者: xiangzhenhai<br/>
	 * 创建日期: 2017年2月15日-上午11:07:31<br/>
	 * 开始版本: 1.0.0<br/>
	 */
	public Result queryById(String solrURL, String coreName, String id) {

		Result result = new Result();

		// 搜索数据
		try {
			SolrClient client = new HttpSolrClient(solrURL);
			SolrDocument solrDocument = client.getById(coreName, id);
			result.setData(JSONObject.toJSON(solrDocument));
			client.close();
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage(e.toString());
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 方法描述: [设置搜索条件，查询数据]</br>
	 * 初始作者: xiangzhenhai<br/>
	 * 创建日期: 2017年2月9日-下午3:48:10<br/>
	 * 开始版本: 1.0.0<br/>
	 */
	public Result query(String solrURL, String coreName, ParamModel paramModel) {

		Result result = new Result();

		SolrQuery solrQuery = new SolrQuery();

		// 设置搜索query
		String query = getQuery(paramModel.getContent(), paramModel.getContentFields());
		solrQuery.setQuery(query);

		// 设置高亮显示
		if (paramModel.isHl() && paramModel.getHlFields() != null && paramModel.getHlFields().length > 0) {
			solrQuery.setHighlight(true).setHighlightSimplePre(paramModel.getHlPre())
					.setHighlightSimplePost(paramModel.getHlPost());
			solrQuery.setParam("hl.fl", StringUtils.join(paramModel.getHlFields(), ","));// 设置高亮字段
		}

		// 设置过虑条件
		if (paramModel.getFilterQuerys() != null && paramModel.getFilterQuerys().length > 0) {
			for (String fq : paramModel.getFilterQuerys()) {
				solrQuery.addFilterQuery(fq);
			}
		}

		// 设置搜索结果排序
		if (paramModel.getSort() != null && !paramModel.getSort().isEmpty()) {
			for (String key : paramModel.getSort().keySet()) {
				if ("asc".equals(paramModel.getSort().get(key))) {
					solrQuery.addSort(key, SolrQuery.ORDER.asc);
				} else if ("desc".equals(paramModel.getSort().get(key))) {
					solrQuery.addSort(key, SolrQuery.ORDER.desc);
				} else {
					logger.info("---------排序方法错误:" + paramModel.getSort().get(key) + "----------");
				}
			}
		}

		// 设置搜索结果字段
		if (paramModel.getFl() != null && paramModel.getFl().length != 0) {
			String flFields = StringUtils.join(paramModel.getFl(), ",");
			solrQuery.setParam("fl", flFields);
		}

		// 设置起始位置和搜索结果数量
		solrQuery.setStart(paramModel.getStart());
		solrQuery.setRows(paramModel.getRows());

		// 搜索数据
		try {
			SolrClient client = new HttpSolrClient(solrURL);
			// METHOD.POST使用post方式提交，避免url过长引起的异常
			QueryResponse rsp = client.query(coreName, solrQuery, METHOD.POST);
			SolrDocumentList docs = rsp.getResults();

			// 组装高亮显示搜索结果
			if (paramModel.isHl() && paramModel.getHlFields() != null && paramModel.getHlFields().length > 0) {

				Map<String, Map<String, List<String>>> hlMap = rsp.getHighlighting();

				for (SolrDocument s : docs) {
					Map<String, List<String>> hlFieldMap = hlMap.get(s.getFieldValue(paramModel.getHlKey()).toString());
					for (String field : hlFieldMap.keySet()) {
						s.setField(field, hlFieldMap.get(field).get(0));
					}
				}
			}

			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("total", docs.getNumFound());
			dataMap.put("docs", JSONObject.toJSON(docs));
			result.setData(dataMap);
			result.setMessage("status:" + rsp.getStatus() + ",Qtime:" + rsp.getQTime());
			client.close();
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage(e.toString());
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 方法描述: [写入数据]</br>
	 * 初始作者: xiangzhenhai<br/>
	 * 创建日期: 2017年2月10日-上午11:35:13<br/>
	 * 开始版本: 1.0.0<br/>
	 */
	public Result add(String solrURL, String coreName, ParamModel paramModel) {

		Result result = new Result();

		Map<String, Object> addMap = paramModel.getAddMap();
		List<Map<String, Object>> addList = paramModel.getAddList();

		try {
			SolrClient client = new HttpSolrClient(solrURL + "/" + coreName);

			if (addMap != null && !addMap.isEmpty()) {
				// 将map里的所有value值为日期字符串的转为date
				CustomDateUtils.mapValuesToDate(addMap);

				// 组装SolrInputDocument
				SolrInputDocument doc = new SolrInputDocument();
				for (String key : addMap.keySet()) {
					doc.addField(key, addMap.get(key));
				}
				// 单条数据写入
				client.add(doc);

			} else if (addList != null && !addList.isEmpty()) {
				// 组装SolrInputDocument列表
				List<SolrInputDocument> docList = new ArrayList<SolrInputDocument>();
				for (Map<String, Object> map : addList) {
					// 将map里的所有value值为日期字符串的转为date
					CustomDateUtils.mapValuesToDate(map);

					SolrInputDocument doc = new SolrInputDocument();
					for (String key : map.keySet()) {
						doc.addField(key, map.get(key));
					}
					docList.add(doc);
				}
				// 批量数据写入
				client.add(docList);

			} else {
				result.setSuccess(false);
				result.setMessage("获取参数失败，请确认输入参数");
				return result;
			}

			// 提交写入数据操作
			UpdateResponse rsp = client.commit();
			result.setMessage("status:" + rsp.getStatus() + ",Qtime:" + rsp.getQTime());

			client.close();

		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage(e.toString());
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 方法描述: [更新数据]</br>
	 * 初始作者: xiangzhenhai<br/>
	 * 创建日期: 2017年2月13日-下午2:19:43<br/>
	 * 开始版本: 1.0.0<br/>
	 */
	public Result update(String solrURL, String coreName, ParamModel paramModel) {

		Result result = new Result();

		Map<String, Object> updateMap = paramModel.getUpdateMap();
		List<Map<String, Object>> updateList = paramModel.getUpdateList();

		try {
			SolrClient client = new HttpSolrClient(solrURL + "/" + coreName);

			if (updateMap != null && !updateMap.isEmpty()) {
				// 将map里的所有value值为日期字符串的转为date
				CustomDateUtils.mapValuesToDate(updateMap);

				// 组装SolrInputDocument
				SolrInputDocument doc = new SolrInputDocument();
				for (String key : updateMap.keySet()) {
					doc.addField(key, updateMap.get(key));
				}
				// 单条数据更新
				client.add(doc);

			} else if (updateList != null && !updateList.isEmpty()) {
				// 组装SolrInputDocument列表
				List<SolrInputDocument> docList = new ArrayList<SolrInputDocument>();
				for (Map<String, Object> map : updateList) {
					// 将map里的所有value值为日期字符串的转为date
					CustomDateUtils.mapValuesToDate(map);

					SolrInputDocument doc = new SolrInputDocument();
					for (String key : map.keySet()) {
						doc.addField(key, map.get(key));
					}
					docList.add(doc);
				}
				// 批量数据更新
				client.add(docList);

			} else {
				result.setSuccess(false);
				result.setMessage("获取参数失败，请确认输入参数");
				return result;
			}

			// 提交更新数据操作
			UpdateResponse rsp = client.commit();
			result.setMessage("status:" + rsp.getStatus() + ",Qtime:" + rsp.getQTime());

			client.close();

		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage(e.toString());
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 方法描述: [删除数据]</br>
	 * 初始作者: xiangzhenhai<br/>
	 * 创建日期: 2017年2月13日-上午11:12:30<br/>
	 * 开始版本: 1.0.0<br/>
	 */
	public Result delete(String solrURL, String coreName, ParamModel paramModel) {

		Result result = new Result();

		try {

			SolrClient client = new HttpSolrClient(solrURL + "/" + coreName);

			// 判断是根据id还是query删除solr数据
			if (StringUtils.isNotBlank(paramModel.getId())) {
				client.deleteById(paramModel.getId());
			} else if (StringUtils.isNotBlank(paramModel.getDeleteQuery())) {
				client.deleteByQuery(paramModel.getDeleteQuery());
			} else {
				result.setSuccess(false);
				result.setMessage("获取参数失败，请确认输入参数");
				return result;
			}

			UpdateResponse rsp = client.commit();
			result.setMessage("status:" + rsp.getStatus() + ",Qtime:" + rsp.getQTime());

			client.close();

		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage(e.toString());
			e.printStackTrace();
		}

		return result;
	}

	// 组装查询条件字符串
	private String getQuery(String content, String[] contentFields) {
		String query = "";

		if (StringUtils.isBlank(content) || contentFields == null || contentFields.length == 0) {
			return "*";
		}

		for (int i = 0; i < contentFields.length; i++) {
			if (i == 0) {
				query += contentFields[i] + ":" + content;
			} else {
				query += " OR " + contentFields[i] + ":" + content;
			}
		}

		return query;
	}
}
