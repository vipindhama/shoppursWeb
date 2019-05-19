package com.shoppurs.customers.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoppurs.customers.dao.OrderManagerDao;
import com.shoppurs.customers.model.MyOrder;
import com.shoppurs.customers.model.MyProduct;
import com.shoppurs.customers.model.MyResponse;
import com.shoppurs.customers.model.OrderDetail;
import com.shoppurs.customers.model.UserID;
import com.shoppurs.shop.controller.ShoppursApiController;
import com.shoppurs.shop.dao.OfferDao;
import com.shoppurs.shop.model.Category;
import com.shoppurs.shop.model.MyOffer;

@RestController("/api/order")
public class OrderApiController {
	
	private static final Logger log = LoggerFactory.getLogger(ShoppursApiController.class);
	
	@Autowired
	private OrderManagerDao orderDao;
	
	
	@RequestMapping("/api/order/generate_order")
	public MyResponse generateShoppursOrder(@RequestBody List<MyOrder> myOrderList) {
		String[] dataArray =  orderDao.generateOrder(myOrderList).split("-");
		String status = dataArray[0];
		String orderId = dataArray[1];
		Map<String,String> map = new HashMap();
		map.put("orderId",orderId);
		if(status.equals("success")) {
			return generateResponse(true,"Order generated successfully.",map);
		}
		else {
			return generateResponse(false,status,null);				
		}
	}
	
	@RequestMapping("/api/order/place_order")
	public MyResponse placeShoppursOrder(@RequestBody List<MyOrder> myOrderList) {
		String status =  orderDao.placeOrder(myOrderList);
		if(status.equals("success")) {
			return generateResponse(true,"Order created successfully.",null);
		}
		else {
			return generateResponse(false,status,null);				
		}
	}
	
	@RequestMapping("/api/order/get_order")
	public MyResponse getCustomerOrder(@RequestBody UserID item) {
		List<MyOrder> itemList =  orderDao.getCustomerOrder(item);
		
		if(itemList != null) {
			return generateResponse(true,"Orders Fetched successfully.",itemList);
		}
		else {
			return generateResponse(false,"There is some problem in fetching orders.",null);				
		}
	}

	
	@RequestMapping("/api/order/get_order_details")
	public MyResponse getCustomerOrderDetails(@RequestBody UserID item) {
		List<OrderDetail> itemList =  orderDao.getCustomerOrderDetail(item);
		
		if(itemList != null) {
			return generateResponse(true,"Order details Fetched successfully.",itemList);
		}
		else {
			return generateResponse(false,"There is some problem in fetching orders.",null);				
		}
	}		
	
	
	//Manage Shoppurs Order
	/*@RequestMapping("/api/order/manageShoppursOrder")
	public MyResponse manageShoppursOrder(@RequestBody MyOrder myorder) {
		List<MyOrder> response;
		List<Category> catList;
		String status = "something went wrong, try again!";
		if(myorder.getAction() ==1) {	// action 1 = create order
			status =  (String) orderDao.manageShoppursOrder(myorder);
			if(status.equals("success")) {
			return generateResponse(true,"order created successfully.",null);
			}else {
			return generateResponse(false,status,null);			
			}
		}else if(myorder.getAction()==2 || myorder.getAction()==3 | myorder.getAction()==4) {
			response = (List)orderDao.manageShoppursOrder(myorder);
			return generateResponse(true,"Orders Fetched successfully.",response);
		}
		return generateResponse(false,status,null);	
	}*/
	
	//This method generates response body
    private MyResponse generateResponse(boolean status,String message,Object data) {
    	MyResponse myResponse = new MyResponse();
    	myResponse.setStatus(status);
    	myResponse.setMessage(message);
    	myResponse.setResult(data);
    	return myResponse;
    }

}
