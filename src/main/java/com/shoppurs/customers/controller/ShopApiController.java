package com.shoppurs.customers.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoppurs.customers.dao.ShopManagerDao;
import com.shoppurs.customers.model.MyResponse;
import com.shoppurs.customers.model.UserID;
import com.shoppurs.shop.model.Favourite;
import com.shoppurs.shop.model.Ratings;

@RestController("/api/customers/shop")
public class ShopApiController {
	
	private static final Logger log = LoggerFactory.getLogger(CustomerApiController.class);
	
	@Autowired
	private ShopManagerDao shopManagerDao;
	
	@RequestMapping("/api/customers/shop/favourite_shops")
	private MyResponse getFavouriteShops(@RequestBody UserID item) {
		List<Map<Integer,String>> favShopList = shopManagerDao.getFavouriteShops(item);
		return generateResponse(true,"Favourite shops fetched successfully.",favShopList);	
	}
	
	//get ratings
	@RequestMapping("/api/customers/shop/get_ratings")
	public MyResponse getRatings(@RequestBody Ratings item) {
		float ratings = shopManagerDao.getShopRatings(item);
		return generateResponse(true,"Ratings fetched successfully.",ratings);	
	}					
	
	@RequestMapping("/api/customers/shop/favourite")
	private MyResponse setFavourite(@RequestBody Favourite item) {
		String status = shopManagerDao.setFavourite(item);
			if(status.equals("success")) {
				return generateResponse(true,"Favourite status is changed successfully.",null);	
			}
			else if(status.equals("error")){
				return generateResponse(false,"Error in changing favourite status...",0);			
			}
			else if(status.equals("0")){
				return generateResponse(false,"This shop is already favourite.",0);			
			}
			else if(status.equals("1")){
				return generateResponse(false,"This shop is already not favourite.",0);			
			}else {
				return generateResponse(false,"Error in changing favourite status...",0);
			}
	}
	
	//setFavourite
		@RequestMapping("/api/customers/shop/ratings")
		public MyResponse setRatings(@RequestBody Ratings item) {
			String status = shopManagerDao.setRatings(item);
			if(status.equals("success")) {
				return generateResponse(true,"Ratings inserted successfully.",null);
				
			}else if(status.equals("error")){
				return generateResponse(false,"Error in inserting ratings...",0);			
			}
			else {
				return generateResponse(false,status,1);			
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
