package com.shoppurs.customers.model;

import java.util.List;

public class MyCart {
	
	private int prodQty;
	private String shopCode,prodCode,dbName,dbUserName,dbPassword;
	private MyProduct myProduct;
	private List<MyProduct> myProductList;
	
	
	
	public String getShopCode() {
		return shopCode;
	}
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}
	public String getProdCode() {
		return prodCode;
	}
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}
	public int getProdQty() {
		return prodQty;
	}
	public void setProdQty(int prodQty) {
		this.prodQty = prodQty;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getDbUserName() {
		return dbUserName;
	}
	public void setDbUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}
	public String getDbPassword() {
		return dbPassword;
	}
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	public List<MyProduct> getMyProductList() {
		return myProductList;
	}
	public void setMyProductList(List<MyProduct> myProductList) {
		this.myProductList = myProductList;
	}
	public MyProduct getMyProduct() {
		return myProduct;
	}
	public void setMyProduct(MyProduct myProduct) {
		this.myProduct = myProduct;
	}
	
	

}
