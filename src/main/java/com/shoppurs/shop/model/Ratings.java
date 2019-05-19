package com.shoppurs.shop.model;

import java.util.List;

import com.shoppurs.shop.model.requestModel.ShopCode;

public class Ratings {
	
	private int id;
	private float ratings;
	private String remarks,prodCode,custCode,shopCode,dbName,dbPassword,dbUserName;
	
	private List<ShopCode> shopCodeList;
	
	
	
	public List<ShopCode> getShopCodeList() {
		return shopCodeList;
	}
	public void setShopCodeList(List<ShopCode> shopCodeList) {
		this.shopCodeList = shopCodeList;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public float getRatings() {
		return ratings;
	}
	public void setRatings(float ratings) {
		this.ratings = ratings;
	}
	
	
	
	public String getProdCode() {
		return prodCode;
	}
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}
	public String getCustCode() {
		return custCode;
	}
	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}
	public String getShopCode() {
		return shopCode;
	}
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
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
