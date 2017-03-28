package com.wdcloud.solr.entity;

import java.util.List;
import java.util.Map;

/**
 * 文件名称： com.wdcloud.solr.entity.ParamModel.java</br>
 * 初始作者： xiangzhenhai</br>
 * 创建日期： 2017年2月9日</br>
 * 功能说明： 用于接收参数 <br/>
 *
 * =================================================<br/>
 * 修改记录：<br/>
 * 修改作者 日期 修改内容<br/>
 *
 *
 * ================================================<br/>
 * Copyright (c) 2010-2011 .All rights reserved.<br/>
 */
public class ParamModel {

	private String id; // id字符串
	private String content; // 查询内容
	private String[] contentFields; // 查询内容适用字段
	private String deleteQuery;
	private String[] filterQuerys; // 搜索filterQuery列表
	private Map<String, String> sort; // 搜索排序列表
	private Integer start; // 搜索开始位置
	private Integer rows; // 搜索结果数量
	private Map<String, Object> addMap; // 写入solr的数据map
	private List<Map<String, Object>> addList; // 写入solr的数据list
	private Map<String, Object> updateMap; // 更新solr的数据map
	private List<Map<String, Object>> updateList; // 更新solr的数据list
	private boolean hl; // 是否高亮显示搜索内容（highlight）
	private String[] hlFields; // 高亮显示字段
	private String hlPre; // 高亮前缀
	private String hlPost; // 高亮后缀
	private String hlKey; // 高亮显示所需模型主键名称
	private String[] fl; // 搜索结果字段（fieldlist）

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String[] getContentFields() {
		return contentFields;
	}

	public void setContentFields(String[] contentFields) {
		this.contentFields = contentFields;
	}

	public String[] getFilterQuerys() {
		return filterQuerys;
	}

	public void setFilterQuerys(String[] filterQuerys) {
		this.filterQuerys = filterQuerys;
	}

	public String getDeleteQuery() {
		return deleteQuery;
	}

	public void setDeleteQuery(String deleteQuery) {
		this.deleteQuery = deleteQuery;
	}

	public Map<String, String> getSort() {
		return sort;
	}

	public void setSort(Map<String, String> sort) {
		this.sort = sort;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Map<String, Object> getAddMap() {
		return addMap;
	}

	public void setAddMap(Map<String, Object> addMap) {
		this.addMap = addMap;
	}

	public List<Map<String, Object>> getAddList() {
		return addList;
	}

	public void setAddList(List<Map<String, Object>> addList) {
		this.addList = addList;
	}

	public Map<String, Object> getUpdateMap() {
		return updateMap;
	}

	public void setUpdateMap(Map<String, Object> updateMap) {
		this.updateMap = updateMap;
	}

	public List<Map<String, Object>> getUpdateList() {
		return updateList;
	}

	public void setUpdateList(List<Map<String, Object>> updateList) {
		this.updateList = updateList;
	}

	public boolean isHl() {
		return hl;
	}

	public void setHl(boolean hl) {
		this.hl = hl;
	}

	public String[] getHlFields() {
		return hlFields;
	}

	public void setHlFields(String[] hlFields) {
		this.hlFields = hlFields;
	}

	public String getHlKey() {
		return hlKey;
	}

	public void setHlKey(String hlKey) {
		this.hlKey = hlKey;
	}

	public String getHlPre() {
		return hlPre;
	}

	public void setHlPre(String hlPre) {
		this.hlPre = hlPre;
	}

	public String getHlPost() {
		return hlPost;
	}

	public void setHlPost(String hlPost) {
		this.hlPost = hlPost;
	}

	public String[] getFl() {
		return fl;
	}

	public void setFl(String[] fl) {
		this.fl = fl;
	}

}
