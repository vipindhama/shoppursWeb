package com.shoppurs.shop.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoppurs.customers.model.MyOrder;
import com.shoppurs.shop.dao.OrderDao;
import com.shoppurs.shop.model.MyProduct;
import com.shoppurs.shop.model.MyResponse;
import com.shoppurs.shop.model.SetOrderStatusObject;
import com.shoppurs.shop.model.UserID;


@RestController("/api/shop/order")
public class OrderApiController {
	
	@Autowired
	private OrderDao orderDao;
	
	
	@RequestMapping("/api/shop/order/generate_order")
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
	
	@RequestMapping("/api/shop/order/place_order")
	public MyResponse placeShoppursOrder(@RequestBody List<MyOrder> myOrderList) {
		String status =  orderDao.placeOrder(myOrderList);
		if(status.equals("success")) {
			return generateResponse(true,"Order created successfully.",null);
		}
		else {
			return generateResponse(false,status,null);				
		}
	}

	
//API to get all products with sub cat
    
    @RequestMapping("/api/shop/order/productslist")
    public MyResponse getProductsByOrder(@RequestBody UserID userId) {
    	List<MyProduct> itemList = orderDao.getProductList(userId);
    	return generateResponse(true,"Product List fetched successfuly",itemList);
    	
    }  
    
    @RequestMapping("/api/shop/order/get_shop_cust_order")
	public MyResponse getShopCustomerOrder(@RequestBody UserID item) {
		List<MyOrder> itemList =  orderDao.getShopCustomerOrder(item);
		
		if(itemList != null) {
			return generateResponse(true,"Orders Fetched successfully.",itemList);
		}
		else {
			return generateResponse(false,"There is some problem in fetching orders.",null);				
		}
	}
    
    @RequestMapping("/api/shop/order/get_shop_order")
   	public MyResponse getShopOrder(@RequestBody UserID item) {
   		List<MyOrder> itemList =  orderDao.getShopOrder(item);
   		
   		if(itemList != null) {
   			return generateResponse(true,"Orders Fetched successfully.",itemList);
   		}
   		else {
   			return generateResponse(false,"There is some problem in fetching orders.",null);				
   		}
   	}
    
    @RequestMapping("/api/shop/order/get_pending_order")
   	public MyResponse getPendingCustomerOrder(@RequestBody UserID item) {
   		List<MyOrder> itemList =  orderDao.getPendingShopOrder(item);
   		
   		if(itemList != null) {
   			return generateResponse(true,"Orders Fetched successfully.",itemList);
   		}
   		else {
   			return generateResponse(false,"There is some problem in fetching orders.",null);				
   		}
   	}
    
    @RequestMapping("/api/shop/order/accept_order")
	public MyResponse acceptCustomerOrder(@RequestBody SetOrderStatusObject item) {
		String status =  orderDao.acceptOrder(item);
		
		if(status.equals("success")) {
			return generateResponse(true,"Order accepted successfully.",null);
		}
		else {
			return generateResponse(false,"There is some problem in accepting order.",null);				
		}
	} 
    
    @RequestMapping("/api/shop/order/cancel_order")
	public MyResponse cancelCustomerOrder(@RequestBody SetOrderStatusObject item) {
		String status =  orderDao.declineOrder(item);
		
		if(status.equals("success")) {
			return generateResponse(true,"Order cancelled successfully.",null);
		}
		else {
			return generateResponse(false,"There is some problem in cancelling order.",null);				
		}
	} 
    
    @RequestMapping("/api/shop/order/order_delivered")
	public MyResponse orderDelivered(@RequestBody SetOrderStatusObject item) {
		String status =  orderDao.orderDelivered(item);
		
		if(status.equals("success")) {
			return generateResponse(true,"Order delivered successfully.",null);
		}
		else {
			return generateResponse(false,"There is some problem in delivering order.",null);				
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
