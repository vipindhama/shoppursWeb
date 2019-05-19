package com.shoppurs.customers.model;

import java.util.List;

public class Category {

	private int catId;
	private String catName,catImage,delStatus,createdBy,updatedBy,createdDate,updatedDate;
	
	private List<SubCategory> subCatList;
	
	
	
	public List<SubCategory> getSubCatList() {
		return subCatList;
	}
	public void setSubCatList(List<SubCategory> subCatList) {
		this.subCatList = subCatList;
	}
	public String getCatImage() {
		return catImage;
	}
	public void setCatImage(String catImage) {
		this.catImage = catImage;
	}
	public int getCatId() {
		return catId;
	}
	public void setCatId(int catId) {
		this.catId = catId;
	}
	public String getCatName() {
		return catName;
	}
	public void setCatName(String catName) {
		this.catName = catName;
	}
	public String getDelStatus() {
		return delStatus;
	}
	public void setDelStatus(String delStatus) {
		this.delStatus = delStatus;
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
	
	
}
