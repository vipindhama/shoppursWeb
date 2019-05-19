package com.shoppurs.customers.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoppurs.customers.dao.ProductReviewManagerDao;
import com.shoppurs.customers.model.GetReviewObject;
import com.shoppurs.customers.model.MyCart;
import com.shoppurs.customers.model.MyResponse;
import com.shoppurs.customers.model.ProductReview;
import com.shoppurs.customers.model.UserID;

@RestController("/api/product_review")
public class ProductReviewApiController {
	
	@Autowired
	private ProductReviewManagerDao productReviewManagerDao;
	
	//Add to cart
	@RequestMapping("/api/product_review/add_review")
	public MyResponse addProductReview(@RequestBody ProductReview item) {
		String status = productReviewManagerDao.addProductReview(item);
		if(status.equals("success")) {
			return generateResponse(true,"Product review added successfully.",null);
			}
		else {
			return generateResponse(false,status,null);		
		}
	}
	
	//get cart data
	@RequestMapping("/api/product_review/getReview")
	public MyResponse getCartData(@RequestBody GetReviewObject item) {
		List<ProductReview> itemList = productReviewManagerDao.getProductReview(item);
		if(itemList != null) {
			return generateResponse(true,"Reviews fecthed successfully.",itemList);
			}
		else {
			return generateResponse(false,"Error in fetching reviews...",null);			
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
