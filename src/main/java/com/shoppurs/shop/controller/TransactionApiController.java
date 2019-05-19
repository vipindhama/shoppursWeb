package com.shoppurs.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoppurs.shop.dao.TransactionDao;
import com.shoppurs.shop.model.Invoice;
import com.shoppurs.shop.model.MyPayment;
import com.shoppurs.shop.model.MyResponse;
import com.shoppurs.shop.model.SetOrderStatusObject;
import com.shoppurs.shop.model.UserID;

@RestController("/api/trans")
public class TransactionApiController {

	@Autowired
	private TransactionDao transactionDao;
	
	@RequestMapping("/api/trans/add_trans_data")
	public MyResponse insertPaymentData(@RequestBody MyPayment item) {
		String status =  transactionDao.insertPaymentData(item);
		
		if(status.equals("success")) {
			return generateResponse(true,"Data added successfully.",null);
		}
		else {
			return generateResponse(false,"There is some problem in adding data.",null);				
		}
	} 
	
	
	@RequestMapping("/api/trans/generate_invoice")
	public MyResponse generateInvoice(@RequestBody Invoice item) {
		String status =  transactionDao.generateInvoice(item);
		
		if(status.equals("success")) {
			return generateResponse(true,"Invoice generated successfully.",null);
		}
		else {
			return generateResponse(false,"There is some problem in adding data.",null);				
		}
	} 
	
	
	@RequestMapping("/api/trans/get_invoice")
	public MyResponse getinvoice(@RequestBody UserID item) {
		Invoice invoice =  transactionDao.getinvoice(item);
		
		if(invoice != null) {
			return generateResponse(true,"Invoice fetched successfully.",invoice);
		}
		else {
			return generateResponse(false,"There is some problem in fetching invoice.",null);				
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
