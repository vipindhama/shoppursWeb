package com.shoppurs.shop.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoppurs.connection.DaoConnection;
import com.shoppurs.shop.dao.RetailerDao;
import com.shoppurs.shop.dao.ShoppursDao;
import com.shoppurs.shop.model.Customer;
import com.shoppurs.shop.model.MyBank;
import com.shoppurs.shop.model.MyResponse;
import com.shoppurs.shop.model.MyUser;
import com.shoppurs.shop.model.UserLogin;
import com.shoppurs.shop.model.requestModel.ShopSaleReq;
import com.shoppurs.shop.model.requestModel.UserDetailsReq;

@RestController("/api/user")
public class RetailerApiController {
	

    @Autowired
   	private RetailerDao retailerDao;
    
//API to create new retailer
    
    @RequestMapping("/api/user/manageRegistration")
    public MyResponse manageRegistration(@RequestBody  MyUser myUser) {
    	
    	String[] statusMessage = retailerDao.manageRegistration(myUser).split("-");
    	
    	if(statusMessage[0].equals("0")) {
    		UserDetailsReq item = new UserDetailsReq();
    		item.setHeader(myUser.getUserEmail());
	    	//item.setDbName(DaoConnection.SHOPPURS_DB_NAME);
	    	item.setDbPassword(statusMessage[2]);
	    	item.setDbUserName(statusMessage[3]);
    		return generateResponse(true,"Retailer created successfully.",retailerDao.getUserDetails(item));
    	}else {
    		return generateResponse(false,statusMessage[1],null);
    	}
    	
    }
    
//API to perform login for retailer
    
    @RequestMapping("/api/user/loginRetailer")
    public MyResponse loginRetailer(@RequestBody  UserLogin userLogin) {
    	
    	Object ob = retailerDao.loginRetailer(userLogin);
    	
    	if(ob instanceof String) {
    		return generateResponse(false,(String)ob,null);
    	}else {
    		return generateResponse(true,"Retailer logged-in successfully.",ob);
    	}
    	
    }
    
    
     //API to create new retailer
    
    @RequestMapping("/api/user/createRetailer")
    public MyResponse createCustomer(@RequestBody  Customer customer) {
    	
    	String status = retailerDao.createCustomer(customer);
    	
    	if(status.equals("success")) {
    		return generateResponse(true,"Retailer created successfully.",customer);
    	}else {
    		return generateResponse(false,status,null);
    	}
    	
    
   }
    
    
//API to update bank details
    
    @RequestMapping("/api/user/updateBankDetails")
    public MyResponse updateBankDetails(@RequestBody  MyBank item) {
    	
    	String status = retailerDao.updateBankDetails(item);
    	
    	if(status.equals("success")) {
    		return generateResponse(true,"Bank details updated successfully.",item);
    	}else {
    		return generateResponse(false,status,null);
    	}
    	
    }  
    
//API to get userDetails
    
    @RequestMapping("/api/user/userProfile")
    public MyResponse getUserProfile(@RequestBody UserDetailsReq item) {
    	
    	MyUser myUser = retailerDao.getUserDetails(item);
    	return generateResponse(true,"Profile fetched successfuly",myUser);
    	
    }  
    
//API to get shop sale data
    
    @RequestMapping("/api/user/shop_sale_data")
    public MyResponse getShopSaleData(@RequestBody ShopSaleReq item) {
    	
    	HashMap<String,Object> response = retailerDao.getShopSaleData(item);
    	return generateResponse(true,"Sale data fetched successfuly",response);
    	
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
