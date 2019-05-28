package com.shoppurs.shop.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoppurs.customers.model.MyOrder;
import com.shoppurs.shop.dao.ShoppursDeviceOrderDao;
import com.shoppurs.shop.model.MyResponse;

@RestController("/api/device/order")
public class ShoppursDeviceOrderApiController {
	
	@Autowired
	private ShoppursDeviceOrderDao shoppursDeviceOrderDao;
	
	@RequestMapping("/api/device/order/generate_order")
	public MyResponse generateShoppursOrder(@RequestBody List<MyOrder> myOrderList) {
		String[] dataArray =  shoppursDeviceOrderDao.generatePayDeviceOrder(myOrderList).split("-");
		String status = dataArray[0];
		String orderNumber = dataArray[1];
		Map<String,String> map = new HashMap();
		map.put("orderNumber",orderNumber);
		if(status.equals("success")) {
			return generateResponse(true,"Order generated successfully.",map);
		}
		else {
			return generateResponse(false,status,null);				
		}
	}
	
	@RequestMapping("/api/device/order/place_order")
	public MyResponse placeShoppursOrder(@RequestBody List<MyOrder> myOrderList) {
		String status =  shoppursDeviceOrderDao.placeOrder(myOrderList);
		if(status.equals("success")) {
			return generateResponse(true,"Order created successfully.",null);
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
