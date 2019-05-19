package com.shoppurs.customers.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoppurs.customers.dao.ProductManagerDao;
import com.shoppurs.customers.model.MyResponse;
import com.shoppurs.shop.model.MyDataSource;
import com.shoppurs.shop.model.MyProduct;
import com.shoppurs.shop.model.Ratings;
import com.shoppurs.shop.model.requestModel.RetProductReq;


@RestController("/api/customers/product")
public class ProductApiController {
	
	@Autowired
	private ProductManagerDao productManagerDao;
	
	
	//set ratings
			@RequestMapping("/api/customers/product/ratings")
			public MyResponse setRatings(@RequestBody Ratings item) {
				String status = productManagerDao.setRatings(item);
				if(status.equals("success")) {
					return generateResponse(true,"Ratings inserted successfully.",null);
					
				}else if(status.equals("error")){
					return generateResponse(false,"Error in insertinf ratings...",0);			
				}
				else {
					return generateResponse(false,status,1);			
				}
			}
			
			//API to get all products
		    
			@RequestMapping("/api/customers/products/ret_productslist")
		    public MyResponse getRetProductss(@RequestBody RetProductReq item) {
		    	
		    	List<MyProduct> itemList = productManagerDao.getRetailerProductList(item);
		    	return generateResponse(true,"Product List fetched successfuly",itemList);
		    	
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
