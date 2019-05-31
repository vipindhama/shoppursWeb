package com.shoppurs.shop.model;

import java.util.List;

public class ProductComboOffer {

	private int id,prodId;
	private String offerName,status,startDate,endDate,userName,dbName,dbPassword,dbUserName;
	
	private List<ProductComboDetails> productComboOfferDetails;
	
	
	
	public List<ProductComboDetails> getProductComboOfferDetails() {
		return productComboOfferDetails;
	}
	public void setProductComboOfferDetails(List<ProductComboDetails> productComboOfferDetails) {
		this.productComboOfferDetails = productComboOfferDetails;
	}
	
	
	
	public int getProdId() {
		return prodId;
	}
	public void setProdId(int prodId) {
		this.prodId = prodId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOfferName() {
		return offerName;
	}
	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
