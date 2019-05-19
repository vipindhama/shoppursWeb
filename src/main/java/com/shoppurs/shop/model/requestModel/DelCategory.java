package com.shoppurs.shop.model.requestModel;

import java.util.List;

public class DelCategory {
	
	private List<Integer> catIdList;
	
	private String catIds,dbName,dbPassword,dbUserName;
	
	

	public List<Integer> getCatIdList() {
		return catIdList;
	}

	public void setCatIdList(List<Integer> catIdList) {
		this.catIdList = catIdList;
	}

	public String getCatIds() {
		return catIds;
	}

	public void setCatIds(String catIds) {
		this.catIds = catIds;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public String getDbUserName() {
		return dbUserName;
	}

	public void setDbUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}
	
	

}
