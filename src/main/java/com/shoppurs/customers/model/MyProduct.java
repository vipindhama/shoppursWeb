package com.shoppurs.customers.model;

import java.util.List;

import com.shoppurs.shop.model.Barcode;

public class MyProduct {
	
	private int prodId,prodCatId, prodSubCatId,prodReorderLevel,prodQoh,action,qty;
	private String shopCode,offerId,prodName,prodCode,prodBarCode,prodDesc,prodHsnCode,prodMfgDate,prodExpiryDate,prodMfgBy,prodImage1,prodImage2,prodImage3,
	               createdBy,updatedBy,createdDate,updatedDate,status;
	private String isBarcodeAvailable,retRetailerId,dbName,dbUserName,dbPassword;
	private float prodCgst,prodIgst,prodSgst,prodWarranty,prodMrp,prodSp;
	
	private List<Barcode> barcodeList;
	
	

	
	public List<Barcode> getBarcodeList() {
		return barcodeList;
	}
	public void setBarcodeList(List<Barcode> barcodeList) {
		this.barcodeList = barcodeList;
	}
	public String getIsBarcodeAvailable() {
		return isBarcodeAvailable;
	}
	public void setIsBarcodeAvailable(String isBarcodeAvailable) {
		this.isBarcodeAvailable = isBarcodeAvailable;
	}
	public int getAction() {
		return action;
	}
	public void setAction(int action) {
		this.action = action;
	}
	public String getShopCode() {
		return shopCode;
	}
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getOfferId() {
		return offerId;
	}
	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}
	public int getProdId() {
		return prodId;
	}
	public void setProdId(int prodId) {
		this.prodId = prodId;
	}
	public int getProdCatId() {
		return prodCatId;
	}
	public void setProdCatId(int prodCatId) {
		this.prodCatId = prodCatId;
	}
	
	public int getProdSubCatId() {
		return prodSubCatId;
	}
	public void setProdSubCatId(int subcatid) {
		this.prodSubCatId = subcatid;
	}
	
	
	public String getProdName() {
		return prodName;
	}
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	public String getProdBarCode() {
		return prodBarCode;
	}
	public void setProdBarCode(String prodBarCode) {
		this.prodBarCode = prodBarCode;
	}
	public String getProdDesc() {
		return prodDesc;
	}
	public void setProdDesc(String prodDesc) {
		this.prodDesc = prodDesc;
	}
	public int getProdReorderLevel() {
		return prodReorderLevel;
	}
	public void setProdReorderLevel(int prodReorderLevel) {
		this.prodReorderLevel = prodReorderLevel;
	}
	public int getProdQoh() {
		return prodQoh;
	}
	public void setProdQoh(int prodQoh) {
		this.prodQoh = prodQoh;
	}
	public String getProdHsnCode() {
		return prodHsnCode;
	}
	public void setProdHsnCode(String prodHsnCode) {
		this.prodHsnCode = prodHsnCode;
	}
	public String getProdMfgDate() {
		return prodMfgDate;
	}
	public void setProdMfgDate(String prodMfgDate) {
		this.prodMfgDate = prodMfgDate;
	}
	public String getProdExpiryDate() {
		return prodExpiryDate;
	}
	public void setProdExpiryDate(String prodExpiryDate) {
		this.prodExpiryDate = prodExpiryDate;
	}
	public String getProdMfgBy() {
		return prodMfgBy;
	}
	public void setProdMfgBy(String prodMfgBy) {
		this.prodMfgBy = prodMfgBy;
	}
	public String getProdImage1() {
		return prodImage1;
	}
	public void setProdImage1(String prodImage1) {
		this.prodImage1 = prodImage1;
	}
	public String getProdImage2() {
		return prodImage2;
	}
	public void setProdImage2(String prodImage2) {
		this.prodImage2 = prodImage2;
	}
	public String getProdImage3() {
		return prodImage3;
	}
	public void setProdImage3(String prodImage3) {
		this.prodImage3 = prodImage3;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public float getProdCgst() {
		return prodCgst;
	}
	public void setProdCgst(float prodCgst) {
		this.prodCgst = prodCgst;
	}
	public float getProdIgst() {
		return prodIgst;
	}
	public void setProdIgst(float prodIgst) {
		this.prodIgst = prodIgst;
	}
	public float getProdSgst() {
		return prodSgst;
	}
	public void setProdSgst(float prodSgst) {
		this.prodSgst = prodSgst;
	}

	public float getProdSp() {
		return prodSp;
	}
	public void setProdSp(float prodSp) {
		this.prodSp = prodSp;
	}
	public float getProdWarranty() {
		return prodWarranty;
	}
	public void setProdWarranty(float prodWarranty) {
		this.prodWarranty = prodWarranty;
	}
	public float getProdMrp() {
		return prodMrp;
	}
	public void setProdMrp(float prodMrp) {
		this.prodMrp = prodMrp;
	}
	public String getRetRetailerId() {
		return retRetailerId;
	}
	public void setRetRetailerId(String retRetailerId) {
		this.retRetailerId = retRetailerId;
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
	
	

}
