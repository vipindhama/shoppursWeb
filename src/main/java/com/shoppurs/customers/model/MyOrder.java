package com.shoppurs.customers.model;

import java.util.List;

public class MyOrder {
	
	private String transactionId,orderNumber, orderDate, orderDeliveryNote, orderDeliveryMode, paymentMode,
	        orderDeliverBy,orderPaymentStatus,orderReason,ordCouponId,shopCode,shopName,shopMobile,shopAddress,
	        custCode,custName,custUserCreateStatus,
	        deliveryAddress, deliveryCountry,deliveryState,deliveryCity,pinCode, 
	        createdBy, updateBy, createdDate, 
			updatedDate, orderStatus,prodIds,orderImage,mobileNo,dbName,dbPassword,dbUserName;
	
	private int action, orderId, totalQuantity;
	private float toalAmount;
	
	private List<MyProduct> myProductList;
	
	
	
	
	public String getDeliveryCountry() {
		return deliveryCountry;
	}
	public void setDeliveryCountry(String deliveryCountry) {
		this.deliveryCountry = deliveryCountry;
	}
	public String getDeliveryState() {
		return deliveryState;
	}
	public void setDeliveryState(String deliveryState) {
		this.deliveryState = deliveryState;
	}
	public String getDeliveryCity() {
		return deliveryCity;
	}
	public void setDeliveryCity(String deliveryCity) {
		this.deliveryCity = deliveryCity;
	}
	public String getCustUserCreateStatus() {
		return custUserCreateStatus;
	}
	public void setCustUserCreateStatus(String custUserCreateStatus) {
		this.custUserCreateStatus = custUserCreateStatus;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getOrderPaymentStatus() {
		return orderPaymentStatus;
	}
	public void setOrderPaymentStatus(String orderPaymentStatus) {
		this.orderPaymentStatus = orderPaymentStatus;
	}
	public String getOrdCouponId() {
		return ordCouponId;
	}
	public void setOrdCouponId(String ordCouponId) {
		this.ordCouponId = ordCouponId;
	}
	public String getOrderDeliverBy() {
		return orderDeliverBy;
	}
	public void setOrderDeliverBy(String orderDeliverBy) {
		this.orderDeliverBy = orderDeliverBy;
	}
	public String getOderPaymentStatus() {
		return orderPaymentStatus;
	}
	public void setOderPaymentStatus(String orderPaymentStatus) {
		this.orderPaymentStatus = orderPaymentStatus;
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
	public List<MyProduct> getMyProductList() {
		return myProductList;
	}
	public void setMyProductList(List<MyProduct> myProductList) {
		this.myProductList = myProductList;
	}
	public int getAction() {
		return action;
	}
	public void setAction(int action) {
		this.action = action;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getOrderDeliveryNote() {
		return orderDeliveryNote;
	}
	public void setOrderDeliveryNote(String orderDeliveryNote) {
		this.orderDeliveryNote = orderDeliveryNote;
	}
	public String getOrderDeliveryMode() {
		return orderDeliveryMode;
	}
	public void setOrderDeliveryMode(String orderDeliveryMode) {
		this.orderDeliveryMode = orderDeliveryMode;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getDeliveryAddress() {
		return deliveryAddress;
	}
	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}
	public String getPinCode() {
		return pinCode;
	}
	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
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
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	
	public int getTotalQuantity() {
		return totalQuantity;
	}
	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	public float getToalAmount() {
		return toalAmount;
	}
	public void setToalAmount(float toalAmount) {
		this.toalAmount = toalAmount;
	}
	public String getProdIds() {
		return prodIds;
	}
	public void setProdIds(String prodIds) {
		this.prodIds = prodIds;
	}
	public String getOrderImage() {
		return orderImage;
	}
	public void setOrderImage(String orderImage) {
		this.orderImage = orderImage;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
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
	public String getCustCode() {
		return custCode;
	}
	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}
	

	
}
