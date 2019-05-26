package com.shoppurs.shop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoppurs.customers.model.GetReviewObject;
import com.shoppurs.customers.model.MyOrder;
import com.shoppurs.customers.model.MyResponse;
import com.shoppurs.customers.model.ProductReview;
import com.shoppurs.customers.model.UserID;
import com.shoppurs.shop.dao.ShopCustomerDao;
import com.shoppurs.shop.model.Customer;
import com.shoppurs.shop.model.CustomerSale;
import com.shoppurs.shop.model.Favourite;
import com.shoppurs.shop.model.ProductSaleObject;
import com.shoppurs.shop.model.Ratings;
import com.shoppurs.customers.model.MyUser;

@RestController("/api/shop_customer")
public class CustomerApiController {
	
	@Autowired
	private ShopCustomerDao shopCustomerDao;
	
	//get customer list data
	@RequestMapping("/api/shop_customer/customers")
	public MyResponse getCustomerList(@RequestBody UserID item) {
		List<Customer> itemList = shopCustomerDao.getCustomerList(item);
		if(itemList != null) {
			return generateResponse(true,"Customers fecthed successfully.",itemList);
			}
		else {
			return generateResponse(false,"Error in fetching customers...",null);			
		}
	}
	
	
	//is customer registered
		@RequestMapping("/api/shop_customer/is_registered")
		public MyResponse isCustomerRegistered(@RequestBody UserID item) {
			Customer customer = shopCustomerDao.isCustomerRegistered(item);
			if(customer == null) {
				return generateResponse(false,"Error in checking customer detail...",0);
					
			}else if(customer.getName() != null){
				return generateResponse(true,"Customer is regsitered",customer);		
			}
			else {
				return generateResponse(false,"Customer is not regsitered",1);			
			}
		}

		//is customer registered
		@RequestMapping("/api/shop_customer/register_shop_customer")
		public MyResponse registerCustomer(@RequestBody Customer item) {
			String status = shopCustomerDao.registerCustomer(item);
			if(status.equals("success")) {
				return generateResponse(true,"Customer is registered",null);
				
			}else if(status.equals("error")){
				return generateResponse(false,"Error in registering customer...",0);			
			}
			else {
				return generateResponse(false,status,1);			
			}
		}							
	
	//API to get Customer sale data
    @RequestMapping("/api/shop_customer/customer_sale_data")
    public MyResponse getCustomerSaleData(@RequestBody UserID userId) {
    	
    	List<CustomerSale> itemList = shopCustomerDao.getCustomerSaleData(userId);
    	return generateResponse(true,"Customer Sale Data fetched successfuly",itemList);
    	
    }	
    
    @RequestMapping("/api/shop_customer/customer_pre_orders")
	public MyResponse getCustomerOrder(@RequestBody UserID item) {
		List<MyOrder> itemList =  shopCustomerDao.getPreOrders(item);
		
		if(itemList != null) {
			return generateResponse(true,"Orders Fetched successfully.",itemList);
		}
		else {
			return generateResponse(false,"There is some problem in fetching orders.",null);				
		}
	}
		
  //setFavourite
  		@RequestMapping("/api/shop_customer/favourite")
  		public MyResponse setFavourite(@RequestBody Favourite item) {
  			String status = shopCustomerDao.setFavourite(item);
  			if(status.equals("success")) {
  				return generateResponse(true,"Favourite status is changed successfully.",null);
  				
  			}else if(status.equals("error")){
  				return generateResponse(false,"Error in changing favourite status...",0);			
  			}
  			else {
  				return generateResponse(false,status,1);			
  			}
  		}	
  		
  	//setRatings
  		@RequestMapping("/api/shop_customer/ratings")
  		public MyResponse setRatings(@RequestBody Ratings item) {
  			String status = shopCustomerDao.setRatings(item);
  			if(status.equals("success")) {
  				return generateResponse(true,"Ratings inserted successfully.",null);
  				
  			}else if(status.equals("error")){
  				return generateResponse(false,"Error in insertinf ratings...",0);			
  			}
  			else {
  				return generateResponse(false,status,1);			
  			}
  		}	
  		
  	//getRatings
  		@RequestMapping("/api/shop_customer/get_ratings")
  		public MyResponse getCustomerRatings(@RequestBody UserID item) {
  			float ratings = shopCustomerDao.getCustomerRatings(item);
  			return generateResponse(true,"Ratings fetched successfully.",null);
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
