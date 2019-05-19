package com.shoppurs.customers.model;

public class OrderDetail {
	
	private int orderId,qty;
	private String offerId,shopCode,shopName,shopMobile,shopAddress,orderDeliveryMode,orderStatus,
	                oderPaymentStatus,orderDeliverBy,orderReason,prodName,prodBarCode,prodDesc,
	               prodImage1,prodImage2,prodImage3,status;
	
	private float prodCgst,prodIgst,prodSgst,prodMrp,prodSp;
	
	

	public String getOrderDeliveryMode() {
		return orderDeliveryMode;
	}

	public void setOrderDeliveryMode(String orderDeliveryMode) {
		this.orderDeliveryMode = orderDeliveryMode;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOderPaymentStatus() {
		return oderPaymentStatus;
	}

	public void setOderPaymentStatus(String oderPaymentStatus) {
		this.oderPaymentStatus = oderPaymentStatus;
	}

	public String getOrderDeliverBy() {
		return orderDeliverBy;
	}

	public void setOrderDeliverBy(String orderDeliverBy) {
		this.orderDeliverBy = orderDeliverBy;
	}

	public String getOrderReason() {
		return orderReason;
	}

	public void setOrderReason(String orderReason) {
		this.orderReason = orderReason;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopMobile() {
		return shopMobile;
	}

	public void setShopMobile(String shopMobile) {
		this.shopMobile = shopMobile;
	}

	public String getShopAddress() {
		return shopAddress;
	}

	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}

	public String getOfferId() {
		return offerId;
	}

	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
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

	public float getProdMrp() {
		return prodMrp;
	}

	public void setProdMrp(float prodMrp) {
		this.prodMrp = prodMrp;
	}

	public float getProdSp() {
		return prodSp;
	}

	public void setProdSp(float prodSp) {
		this.prodSp = prodSp;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
