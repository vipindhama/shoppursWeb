package com.shoppurs.shop.model;

public class UserLicense {

	private int id,numOfUsers;
	private String shopCode,shopMobile,scheme,purchaseDate,expiryDate,renewdDate,licenseType,userName,dbName,dbUserName,dbPassword;
	private float amount;
	
	
	
	public String getShopMobile() {
		return shopMobile;
	}
	public void setShopMobile(String shopMobile) {
		this.shopMobile = shopMobile;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNumOfUsers() {
		return numOfUsers;
	}
	public void setNumOfUsers(int numOfUsers) {
		this.numOfUsers = numOfUsers;
	}
	public String getShopCode() {
		return shopCode;
	}
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}
	public String getScheme() {
		return scheme;
	}
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}
	public String getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getRenewdDate() {
		return renewdDate;
	}
	public void setRenewdDate(String renewdDate) {
		this.renewdDate = renewdDate;
	}
	public String getLicenseType() {
		return licenseType;
	}
	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
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
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	
	
	
}
