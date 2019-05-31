package com.shoppurs.shop.model;

public class ProductDiscountOffer {

	private int id,prodBuyId,prodFreeId,prodBuyQty,prodFreeQty;
	private String offerName,status,startDate,endDate,userName,dbName,dbPassword,dbUserName;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getProdBuyId() {
		return prodBuyId;
	}
	public void setProdBuyId(int prodBuyId) {
		this.prodBuyId = prodBuyId;
	}
	public int getProdFreeId() {
		return prodFreeId;
	}
	public void setProdFreeId(int prodFreeId) {
		this.prodFreeId = prodFreeId;
	}
	public int getProdBuyQty() {
		return prodBuyQty;
	}
	public void setProdBuyQty(int prodBuyQty) {
		this.prodBuyQty = prodBuyQty;
	}
	public int getProdFreeQty() {
		return prodFreeQty;
	}
	public void setProdFreeQty(int prodFreeQty) {
		this.prodFreeQty = prodFreeQty;
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
