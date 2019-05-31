package com.shoppurs.shop.model;

public class ProductComboDetails {
	
	private int id,pcodPcoId,pcodProdId,pcodProdQty;
	private float pcodPrice;
	private String status,startDate,endDate,userName;
	
	
	
	
	public int getPcodProdId() {
		return pcodProdId;
	}
	public void setPcodProdId(int pcodProdId) {
		this.pcodProdId = pcodProdId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPcodPcoId() {
		return pcodPcoId;
	}
	public void setPcodPcoId(int pcodPcoId) {
		this.pcodPcoId = pcodPcoId;
	}
	public int getPcodProdQty() {
		return pcodProdQty;
	}
	public void setPcodProdQty(int pcodProdQty) {
		this.pcodProdQty = pcodProdQty;
	}
	
	public float getPcodPrice() {
		return pcodPrice;
	}
	public void setPcodPrice(float pcodPrice) {
		this.pcodPrice = pcodPrice;
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
	
	

}
