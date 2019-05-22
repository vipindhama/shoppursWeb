package com.shoppurs.shop.model;

public class ShopCustomerSaleData {
	
	private int invId;
	
	private String invDate,custName;
	
	private float netPayable;

	public int getInvId() {
		return invId;
	}

	public void setInvId(int invId) {
		this.invId = invId;
	}

	public String getInvDate() {
		return invDate;
	}

	public void setInvDate(String invDate) {
		this.invDate = invDate;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public float getNetPayable() {
		return netPayable;
	}

	public void setNetPayable(float netPayable) {
		this.netPayable = netPayable;
	}
	
	

}
