package com.shoppurs.shop.model;

public class ProductQty {
	
	private String prodCode,prodBarCode,isBarCodeAvailable;
	private int prodId,qty;
	
	
	
	
	public String getIsBarCodeAvailable() {
		return isBarCodeAvailable;
	}
	public void setIsBarCodeAvailable(String isBarCodeAvailable) {
		this.isBarCodeAvailable = isBarCodeAvailable;
	}
	public int getProdId() {
		return prodId;
	}
	public void setProdId(int prodId) {
		this.prodId = prodId;
	}
	public String getProdBarCode() {
		return prodBarCode;
	}
	public void setProdBarCode(String prodBarCode) {
		this.prodBarCode = prodBarCode;
	}
	public String getProdCode() {
		return prodCode;
	}
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	
	

}
