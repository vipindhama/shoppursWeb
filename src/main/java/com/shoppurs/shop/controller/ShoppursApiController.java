package com.shoppurs.shop.controller;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoppurs.shop.dao.ShoppursDao;
import com.shoppurs.shop.model.Category;
import com.shoppurs.shop.model.Country;
import com.shoppurs.shop.model.Customer;
import com.shoppurs.shop.model.Greeting;
import com.shoppurs.shop.model.MyBank;
import com.shoppurs.shop.model.MyProduct;
import com.shoppurs.shop.model.MyResponse;
import com.shoppurs.shop.model.MyUser;
import com.shoppurs.shop.model.RetailerCategory;
import com.shoppurs.shop.model.SimpleItem;
import com.shoppurs.shop.model.SubCategory;
import com.shoppurs.shop.model.UserID;
import com.shoppurs.shop.model.UserLogin;

@RestController("/api")
public class ShoppursApiController {
    
    private static final Logger log = LoggerFactory.getLogger(ShoppursApiController.class);
    
    //@Autowired
	//private MyResponse myResponse;
    
    @Autowired
   	private ShoppursDao shoppursDao;
    
   
    //Creation block
      

    
//API to get all data
    
    @RequestMapping("/api/syncdata")
    public MyResponse syncData(@RequestBody  UserID item) {
    	
    	HashMap<String,Object> hashMap = shoppursDao.syncData(item);
    	return generateResponse(true,"Data fetched successfuly",hashMap);
    	
    }     
    

    
    
  //API to get all countries
    
    @RequestMapping("/api/countries")
    public MyResponse getCountries() {
    	
    	List<Country> countryList = shoppursDao.getCountryList();
    	return generateResponse(true,"Countries fetched successfuly",countryList);
    	
    }
    
  //API to get all states
    
    @RequestMapping("/api/states")
    public MyResponse getStates(@RequestParam int countryId) {
    	
    	List<SimpleItem> itemList = shoppursDao.getStateList(countryId);
    	return generateResponse(true,"States fetched successfuly",itemList);
    	
    }
    
  //API to get all cities
    
    @RequestMapping("/api/cities")
    public MyResponse getCities(@RequestParam int stateId) {
    	
    	List<SimpleItem> itemList = shoppursDao.getCityList(stateId);
    	return generateResponse(true,"Cities fetched successfuly",itemList);
    	
    }
    
//test API to run script
    
    @RequestMapping("/api/script")
    public MyResponse testScript() {
    	
    	shoppursDao.testRunScript();
    	return generateResponse(true,"Cities fetched successfuly",null);
    	
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
