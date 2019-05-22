package com.shoppurs.shop.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoppurs.connection.DaoConnection;
import com.shoppurs.shop.dao.CategoryDao;
import com.shoppurs.shop.dao.ShoppursDao;
import com.shoppurs.shop.model.Category;
import com.shoppurs.shop.model.MyResponse;
import com.shoppurs.shop.model.RetailerCategory;
import com.shoppurs.shop.model.SubCategory;
import com.shoppurs.shop.model.UserID;
import com.shoppurs.shop.model.requestModel.CategoryDetailReq;
import com.shoppurs.shop.model.requestModel.DelCategory;

@RestController("/api/categories")
public class CategoryApiController {
	
private static final Logger log = LoggerFactory.getLogger(ShoppursApiController.class);
    
   // @Autowired
   //	private MyResponse myResponse;
    
    @Autowired
   	private CategoryDao categoryDao;
    
//API to create new category
    
    @RequestMapping("/api/categories/createCategory")
    public MyResponse createCategory(@RequestBody  Category category) {
    	
    	String status = categoryDao.createCategory(category);
    	
    	if(status.equals("success")) {
    		CategoryDetailReq item = new CategoryDetailReq();
	    	item.setCatName(category.getCatName());
	    	item.setDbName(DaoConnection.SHOPPURS_DB_NAME);
	    	item.setDbPassword(category.getDbPassword());
	    	item.setDbUserName(category.getDbUserName());
    		return generateResponse(true,"Category created successfully.",categoryDao.getCategoryDetails(item));
    	}else {
    		return generateResponse(false,status,null);
    	}
    	
    }
    
//API to create new sub category
    
    @RequestMapping("/api/categories/createSubCategory")
    public MyResponse createSubCategory(@RequestBody  SubCategory subCategory) {
    	
    	String status = categoryDao.createSubCategory(subCategory);
    	
    	if(status.equals("success")) {
    		CategoryDetailReq item = new CategoryDetailReq();
	    	item.setCatName(subCategory.getSubCatName());
	    	item.setDbName(DaoConnection.SHOPPURS_DB_NAME);
	    	item.setDbPassword(subCategory.getDbPassword());
	    	item.setDbUserName(subCategory.getDbUserName());
    		return generateResponse(true,"Sub Category created successfully.",categoryDao.getSubCategoryDetails(item));
    	}else {
    		return generateResponse(false,status,null);
    	}
    	
    }   
    
//Add category to retailer
    
    @RequestMapping(value = "/api/categories/addCategoryRetailer", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public MyResponse addCategoryWithRetailer(@RequestBody  List<RetailerCategory> item) {
    	
    	String status = categoryDao.createRetailerCategory(item);
    	
    	if(status.equals("success")) {
    		return generateResponse(true,"Category added successfully.",null);
    	}else {
    		return generateResponse(false,status,null);
    	}
    	
    }   
    
//Add sub category to retailer
    
    @RequestMapping(value = "/api/categories/addSubCategoryRetailer", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public MyResponse addSubCategoryWithRetailer(@RequestBody  List<RetailerCategory> item) {
    	
    	String status = categoryDao.createRetailerSubCategory(item);
    	
    	if(status.equals("success")) {
    		return generateResponse(true,"Sub Category added successfully.",null);
    	}else {
    		return generateResponse(false,status,null);
    	}
    	
    }    
    
    
  
  //GET Block
    
    
  //API to get all categories
    
    @RequestMapping("/api/categories/categories")
    public MyResponse getCategories(@RequestBody UserID userId) {
    	
    	List<Category> itemList = categoryDao.getMasterCategories(userId);
    	return generateResponse(true,"Categories fetched successfuly",itemList);
    	
    }
    
//API to get all categories for retailer
    
    @RequestMapping("/api/categories/retailer_categories")
    public MyResponse getRetailerCategories(@RequestBody  UserID item) {
    	
    	List<Category> itemList = categoryDao.getCategories(item);
    	return generateResponse(true,"Categories fetched successfuly",itemList);
    	
    }   
    
//API to get all sub categories
    
    @RequestMapping("/api/categories/subcategories")
    public MyResponse getSubCategories(@RequestBody CategoryDetailReq item) {
    	
    	List<SubCategory> itemList = categoryDao.getSubCategoryList(item);
    	return generateResponse(true,"Sub Categories fetched successfuly",itemList);
    	
    }
    
//del category from retailer
    
    @RequestMapping(value = "/api/categories/delete", method = RequestMethod.POST)
    public MyResponse deleteCategory(@RequestBody  DelCategory item) {
    	
    	String status = categoryDao.deleteCategory(item);
    	
    	if(status.equals("success")) {
    		return generateResponse(true,"Category deleted successfully.",null);
    	}else {
    		return generateResponse(false,status,null);
    	}
    	
    }  
    
//del sub category from retailer
    
    @RequestMapping(value = "/api/categories/delete", method = RequestMethod.POST)
    public MyResponse deleteSubCategory(@RequestBody  DelCategory item) {
    	
    	String status = categoryDao.deleteSubCategory(item);
    	
    	if(status.equals("success")) {
    		return generateResponse(true,"Category deleted successfully.",null);
    	}else {
    		return generateResponse(false,status,null);
    	}
    	
    }      
    
//This method generates response body
    
    private MyResponse generateResponse(boolean status,String message,Object data) {
    	MyResponse myResponse = new MyResponse();
    	myResponse.setStatus(status);
    	myResponse.setMessage(message);
    	myResponse.setResult(data);
    	return myResponse;
    }

}
