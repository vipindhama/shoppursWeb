package com.shoppurs.customers.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoppurs.customers.dao.CartManagerDao;
import com.shoppurs.customers.model.MyCart;
import com.shoppurs.customers.model.MyOrder;
import com.shoppurs.customers.model.MyResponse;
import com.shoppurs.customers.model.UserID;

@RestController("/api/cart")
public class CartApiCotroller {

	@Autowired
	private CartManagerDao cartDao;
	
	//Add to cart
		@RequestMapping("/api/cart/add")
		public MyResponse addToCart(@RequestBody MyCart myCart) {
			String status = cartDao.addToCart(myCart);
			if(status.equals("success")) {
				return generateResponse(true,"Product added to cart successfully.",null);
				}
			else {
				return generateResponse(false,status,null);				
			}
		}
		
		//Add to cart
		@RequestMapping("/api/cart/update")
		public MyResponse updateCart(@RequestBody MyCart myCart) {
			String status = cartDao.updateCart(myCart);
			if(status.equals("success")) {
				return generateResponse(true,"updated successfully.",null);
				}
			else {
				return generateResponse(false,status,null);			
				
			}
		}	
		
		//get cart data
		@RequestMapping("/api/cart/getCartData")
		public MyResponse getCartData(@RequestBody UserID userId) {
			List<MyCart> itemList = cartDao.getCartData(userId);
			if(itemList != null) {
				return generateResponse(true,"Cart details fecthed successfully.",itemList);
				}
			else {
				return generateResponse(false,"Error in fetching cart details...",null);			
			}
		}
		
		//remove product from cart
		@RequestMapping("/api/cart/remove_product")
		public MyResponse removeProductFromCart(@RequestBody MyCart item) {
			String status = cartDao.removeProductFromCart(item);
			if(status.equals("success")) {
				return generateResponse(true,"Product removed successfully.",null);
				}
			else {
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
