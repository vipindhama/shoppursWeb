package com.shoppurs.customers.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoppurs.customers.dao.SearchDao;
import com.shoppurs.customers.model.Category;
import com.shoppurs.customers.model.MyProduct;
import com.shoppurs.customers.model.MyResponse;
import com.shoppurs.customers.model.MyShop;
import com.shoppurs.shop.model.Customer;
import com.shoppurs.shop.model.requestModel.SearchCustomerReq;
import com.shoppurs.shop.model.requestModel.SearchProductReq;
import com.shoppurs.shop.model.requestModel.SearchShopReq;

@RestController("/api/search")
public class SearchApiController {

	@Autowired
	private SearchDao searchDao;
	
	//API to seacrh shops
    
    @RequestMapping("/api/search/shops")
    public MyResponse searchShops(@RequestBody SearchShopReq item) {
    	
    	List<MyShop> itemList = searchDao.searchShops(item);
    	return generateResponse(true,"Shops fetched successfuly",itemList);
    	
    }
    
//API to search products
    
    @RequestMapping("/api/search/shop_products")
    public MyResponse searchProductsByShop(@RequestBody SearchProductReq item) {
    	
    	List<MyProduct> itemList = searchDao.searchProductsByShop(item);
    	return generateResponse(true,"Products fetched successfuly",itemList);
    	
    } 
    
//API to search products
    
    @RequestMapping("/api/search/shop_customers")
    public MyResponse searchCustomerByShop(@RequestBody SearchCustomerReq item) {
    	
    	List<Customer> itemList = searchDao.searchCustomer(item);
    	return generateResponse(true,"Customers fetched successfuly",itemList);
    	
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
