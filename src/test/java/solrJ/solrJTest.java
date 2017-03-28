package solrJ;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;

/**
 * 文件名称： solrJ.solrJTest.java</br>
 * 初始作者： xiangzhenhai</br>
 * 创建日期： 2017年2月15日</br>
 * 功能说明： solrj的测试类 <br/>
 */
public class solrJTest {

	// solr url
	public static final String solrURL = "http://localhost:8080/solr";
	// solr core名称
	public static final String coreName = "legal_education_platform";

	@Test
	public void main() {

		// queryById();
		query();
		// add();
		// update();
		// delete();
	}

	/**
	 * 方法描述: [测试通过id查询solr数据]</br>
	 * 初始作者: xiangzhenhai<br/>
	 * 创建日期: 2017年2月15日-上午11:07:24<br/>
	 * 开始版本: 1.0.0<br/>
	 */
	public void queryById() {

		try {
			SolrClient client = new HttpSolrClient(solrURL);
			SolrDocument solrDocument = client.getById(coreName, "1");
			System.out.println(JSONObject.toJSON(solrDocument));
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 方法描述: [测试设置搜索条件，查询数据]</br>
	 * 初始作者: xiangzhenhai<br/>
	 * 创建日期: 2017年2月15日-上午11:36:19<br/>
	 * 开始版本: 1.0.0<br/>
	 */
	public void query() {

		SolrQuery solrQuery = new SolrQuery();

		// 设置搜索内容
		solrQuery.setQuery("content:测试");
		// 设置过虑条件
		// solrQuery.addFilterQuery();
		// 设置起始位置和搜索结果数量
		solrQuery.setRows(null);
		solrQuery.setStart(null);
		// 设置搜索结果排序
		// solrQuery.addSort("sort", SolrQuery.ORDER.desc);
		// 设置输出字段
		solrQuery.setParam("fl", "id,content,title");
		// 设置高亮
		solrQuery.setHighlight(true).setHighlightSimplePre("<span class='red'>").setHighlightSimplePost("</span>");
		solrQuery.setParam("hl.fl", "content");// 设置高亮字段

		// 搜索数据
		try {
			SolrClient client = new HttpSolrClient(solrURL);
			QueryResponse response = client.query(coreName, solrQuery);
			SolrDocumentList docs = response.getResults();

			System.out.println("文档个数：" + docs.getNumFound());
			System.out.println("查询时间：" + response.getQTime());

			Map<String, Map<String, List<String>>> map = response.getHighlighting();
			for (SolrDocument s : docs) {
				s.setField("content", map.get(s.getFieldValue("id").toString()).get("content").get(0));
				System.out.println(JSONObject.toJSON(s));
			}
			System.out.println(JSONObject.toJSON(docs));

			// 通过bean获取搜索结果(需要创建实体类)
			// List<SolrtestBean> list = response.getBeans(SolrtestBean.class);

		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 方法描述: [测试写入数据到solr]</br>
	 * 初始作者: xiangzhenhai<br/>
	 * 创建日期: 2017年2月15日-上午11:43:03<br/>
	 * 开始版本: 1.0.0<br/>
	 */
	public void add() {

		SolrClient client = new HttpSolrClient(solrURL + "/" + coreName);

		// 单条数据写入
		SolrInputDocument doc = new SolrInputDocument();

		doc.addField("id", "1");
		doc.addField("context", "单条数据写入");
		doc.addField("updateTime", new Date());
		doc.addField("sort", 1);

		// 单条数据通过实体类写入
		// SolrtestBean bean = new SolrtestBean();
		// bean.setContext("333333333");
		// bean.setId("8");

		try {
			UpdateResponse rspDoc = client.add(doc);
			// UpdateResponse rsp = client.addBean(bean);
			System.out.println("UpdateResponse result:" + rspDoc.getStatus() + " Qtime:" + rspDoc.getQTime());
			UpdateResponse rspcommit = client.commit();
			System.out.println(
					"commit doc to index" + " result:" + rspcommit.getStatus() + " Qtime:" + rspcommit.getQTime());
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 多条数据写入
		List<SolrInputDocument> docList = new ArrayList<SolrInputDocument>();
		SolrInputDocument doc1 = new SolrInputDocument();
		doc1.addField("id", "2");
		doc1.addField("context", "多条数据写入");
		doc1.addField("updateTime", new Date());
		doc1.addField("sort", 2);
		docList.add(doc);

		// 多条数据通过实体类写入
		// List<SolrtestBean> beanList = new ArrayList<SolrtestBean>();
		// SolrtestBean bean = new SolrtestBean();
		// bean.setContext("333333333");
		// bean.setId("8");
		// beanList.add(bean);

		try {
			UpdateResponse rspDocs = client.add(docList);
			// UpdateResponse rspDocs = client.addBeans(beanList);
			System.out.println("UpdateResponse result:" + rspDocs.getStatus() + " Qtime:" + rspDocs.getQTime());
			UpdateResponse rspcommit = client.commit();
			System.out.println(
					"commit doc to index" + " result:" + rspcommit.getStatus() + " Qtime:" + rspcommit.getQTime());
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 方法描述: [测试更新solr数据]</br>
	 * 初始作者: xiangzhenhai<br/>
	 * 创建日期: 2017年2月15日-上午11:59:30<br/>
	 * 开始版本: 1.0.0<br/>
	 */
	public void update() {
		SolrClient client = new HttpSolrClient(solrURL + "/" + coreName);

		// 这里只给出单条数据更新测试，多条数据更新与多条数据写入相似，就不写了
		// 更新参数说明：set–更新一个字段，add–添加一个字段，inc–在原有值的基础上增加
		SolrInputDocument doc = new SolrInputDocument();
		Map<String, Object> contextMap = new HashMap<String, Object>();
		contextMap.put("set", "更新后的context");
		Map<String, Object> sortMap = new HashMap<String, Object>();
		sortMap.put("inc", 3);

		doc.addField("id", "1");
		doc.addField("context", contextMap);
		doc.addField("sort", sortMap);

		try {
			UpdateResponse rspDoc = client.add(doc);
			System.out.println("UpdateResponse result:" + rspDoc.getStatus() + " Qtime:" + rspDoc.getQTime());
			UpdateResponse rspcommit = client.commit();
			System.out.println(
					"commit doc to index" + " result:" + rspcommit.getStatus() + " Qtime:" + rspcommit.getQTime());
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 方法描述: [测试删除solr数据]</br>
	 * 初始作者: xiangzhenhai<br/>
	 * 创建日期: 2017年2月15日-下午2:39:38<br/>
	 * 开始版本: 1.0.0<br/>
	 */
	public void delete() {
		try {

			SolrClient client = new HttpSolrClient(solrURL + "/" + coreName);

			// 根据id删除solr数据
			client.deleteById("20");

			// 根据query删除solr数据，这里删除context含有"测试"的solr数据（前提是已经添加中文分词器，否则只会删除context为"测试"的solr数据）
			client.deleteByQuery("content:555");

			client.commit();
			UpdateResponse rsp = new UpdateResponse();
			rsp.getStatus();

		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
