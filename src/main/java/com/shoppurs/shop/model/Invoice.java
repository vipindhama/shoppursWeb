package com.shoppurs.shop.model;

import java.util.List;

public class Invoice {
	
	private int invId,invShopId,invCustId;
	private String userName,custCode,custUserCreateStatus,invNo,invTransId,invDate,invShopCode,invShopName,invShopAddress,invShopEmail,invShopMobile,invShopGSTIn,
	invCustName,invCustMobile,invStatus,invCoupenId,invPaymentMode,dbName,dbPassword,dbUserName;
	
	private String paymentMethod,paymentBrand;
	
	private float invTotCGST,invTotSGST,invTotIGST,invTotDisAmount,invTotTaxAmount,invTotAmount,invTotNetPayable;
	
	private List<InvoiceDetail> invoiceDetailList;
	
	

	public List<InvoiceDetail> getInvoiceDetailList() {
		return invoiceDetailList;
	}

	public void setInvoiceDetailList(List<InvoiceDetail> invoiceDetailList) {
		this.invoiceDetailList = invoiceDetailList;
	}
	
	

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getPaymentBrand() {
		return paymentBrand;
	}

	public void setPaymentBrand(String paymentBrand) {
		this.paymentBrand = paymentBrand;
	}



	public String getCustUserCreateStatus() {
		return custUserCreateStatus;
	}

	public void setCustUserCreateStatus(String custUserCreateStatus) {
		this.custUserCreateStatus = custUserCreateStatus;
	}

	public String getCustCode() {
		return custCode;
	}

	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}

	public int getInvId() {
		return invId;
	}

	public void setInvId(int invId) {
		this.invId = invId;
	}

	public int getInvShopId() {
		return invShopId;
	}

	public void setInvShopId(int invShopId) {
		this.invShopId = invShopId;
	}

	public int getInvCustId() {
		return invCustId;
	}

	public void setInvCustId(int invCustId) {
		this.invCustId = invCustId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getInvNo() {
		return invNo;
	}

	public void setInvNo(String invNo) {
		this.invNo = invNo;
	}

	public String getInvTransId() {
		return invTransId;
	}

	public void setInvTransId(String invTransId) {
		this.invTransId = invTransId;
	}

	public String getInvDate() {
		return invDate;
	}

	public void setInvDate(String invDate) {
		this.invDate = invDate;
	}

	public String getInvShopCode() {
		return invShopCode;
	}

	public void setInvShopCode(String invShopCode) {
		this.invShopCode = invShopCode;
	}

	public String getInvShopName() {
		return invShopName;
	}

	public void setInvShopName(String invShopName) {
		this.invShopName = invShopName;
	}

	public String getInvShopAddress() {
		return invShopAddress;
	}

	public void setInvShopAddress(String invShopAddress) {
		this.invShopAddress = invShopAddress;
	}

	public String getInvShopEmail() {
		return invShopEmail;
	}

	public void setInvShopEmail(String invShopEmail) {
		this.invShopEmail = invShopEmail;
	}

	public String getInvShopMobile() {
		return invShopMobile;
	}

	public void setInvShopMobile(String invShopMobile) {
		this.invShopMobile = invShopMobile;
	}

	public String getInvShopGSTIn() {
		return invShopGSTIn;
	}

	public void setInvShopGSTIn(String invShopGSTIn) {
		this.invShopGSTIn = invShopGSTIn;
	}

	public String getInvCustName() {
		return invCustName;
	}

	public void setInvCustName(String invCustName) {
		this.invCustName = invCustName;
	}

	public String getInvCustMobile() {
		return invCustMobile;
	}

	public void setInvCustMobile(String invCustMobile) {
		this.invCustMobile = invCustMobile;
	}

	public String getInvStatus() {
		return invStatus;
	}

	public void setInvStatus(String invStatus) {
		this.invStatus = invStatus;
	}

	public String getInvCoupenId() {
		return invCoupenId;
	}

	public void setInvCoupenId(String invCoupenId) {
		this.invCoupenId = invCoupenId;
	}

	public String getInvPaymentMode() {
		return invPaymentMode;
	}

	public void setInvPaymentMode(String invPaymentMode) {
		this.invPaymentMode = invPaymentMode;
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

	public float getInvTotCGST() {
		return invTotCGST;
	}

	public void setInvTotCGST(float invTotCGST) {
		this.invTotCGST = invTotCGST;
	}

	public float getInvTotSGST() {
		return invTotSGST;
	}

	public void setInvTotSGST(float invTotSGST) {
		this.invTotSGST = invTotSGST;
	}

	public float getInvTotIGST() {
		return invTotIGST;
	}

	public void setInvTotIGST(float invTotIGST) {
		this.invTotIGST = invTotIGST;
	}

	public float getInvTotDisAmount() {
		return invTotDisAmount;
	}

	public void setInvTotDisAmount(float invTotDisAmount) {
		this.invTotDisAmount = invTotDisAmount;
	}

	public float getInvTotTaxAmount() {
		return invTotTaxAmount;
	}

	public void setInvTotTaxAmount(float invTotTaxAmount) {
		this.invTotTaxAmount = invTotTaxAmount;
	}

	public float getInvTotAmount() {
		return invTotAmount;
	}

	public void setInvTotAmount(float invTotAmount) {
		this.invTotAmount = invTotAmount;
	}

	public float getInvTotNetPayable() {
		return invTotNetPayable;
	}

	public void setInvTotNetPayable(float invTotNetPayable) {
		this.invTotNetPayable = invTotNetPayable;
	}
	
	

}
