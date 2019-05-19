package com.shoppurs.customers.controller;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoppurs.customers.dao.ShoppursCustomerDao;
import com.shoppurs.customers.model.Category;
import com.shoppurs.customers.model.Country;
import com.shoppurs.customers.model.Customer;
import com.shoppurs.customers.model.Greeting;
import com.shoppurs.customers.model.MyProduct;
import com.shoppurs.customers.model.MyResponse;
import com.shoppurs.customers.model.ShopCode;
import com.shoppurs.customers.model.MyUser;
import com.shoppurs.customers.model.RetailerCategory;
import com.shoppurs.customers.model.SimpleItem;
import com.shoppurs.customers.model.SubCategory;
import com.shoppurs.customers.model.UserID;
import com.shoppurs.customers.model.UserLogin;

@RestController("/api/customers")
public class CustomerApiController {
    
    private static final Logger log = LoggerFactory.getLogger(CustomerApiController.class);
    
    //@Autowired
	//private MyResponse myResponse;
    
    @Autowired
   	private ShoppursCustomerDao shoppursDao;
    
    
 //API to new customer registration
    
    @RequestMapping("/api/customers/registerCustomer")
    public MyResponse registerCustomer(@RequestBody  MyUser myUser) {
    	
    	String status = shoppursDao.manageRegistration(myUser);
    	
    	if(status.contains("1")) {
    		MyUser user = shoppursDao.getCustomerDetails(myUser.getMobile());
    		return generateResponse(true,"Customer registered successfully.",user);
    	}else {
    		return generateResponse(false,status,null);
    	}
    	
    }
    
    
    // Api to customer login
	  @RequestMapping("/api/customers/loginCustomer") 
	  public MyResponse loginCustomer(@RequestBody UserLogin user) {
		/*
		 * UserLogin user = new UserLogin(); user.setEmail(email);
		 * user.setPassword(password);
		 */
		  
		  MyUser customer = shoppursDao.loginCustomer(user); 
		  if(customer==null)
			  return generateResponse(false,"Authentication Failed",null);
		  else
			  return generateResponse(true,"User logged in successfuly",customer);
	  }
	  
	  //Api to admin login
	  // Api to customer login
	  @RequestMapping("/api/user/adminLogin") 
	  public MyResponse loginUser(@RequestBody UserLogin user) {
		/*
		 * UserLogin user = new UserLogin(); user.setEmail(email);
		 * user.setPassword(password);
		 */
		  
		  MyUser customer = shoppursDao.loginCustomer(user); 
		  if(customer==null)
			  return generateResponse(false,"Authentication Failed",null);
		  else
			  return generateResponse(true,"User logged in successfuly",customer);
	  }
    
	  
	//GET Block
	    
	    
	  //API to get all categories
	    
	    @RequestMapping("/api/customers/categories")
	    public MyResponse getCategories() {
	    	
	    	List<Category> itemList = shoppursDao.getCategories();
	    	return generateResponse(true,"Categories fetched successfuly",itemList);
	    	
	    }
	    
	//API to get all sub categories
	    
	    @RequestMapping("/api/customers/subcategories")
	    public MyResponse getSubCategories(@RequestParam int catId) {
	    	
	    	List<SubCategory> itemList = shoppursDao.getSubCategoryList(catId);
	    	return generateResponse(true,"Sub Categories fetched successfuly",itemList);	
	    } 
	    
	    
	  //API to get all products
	    
	    @RequestMapping("/api/customers/productslist")
	    public MyResponse getProductss() {
	    	
	    	List<MyProduct> itemList = shoppursDao.getProductList();
	    	return generateResponse(true,"Product List fetched successfuly",itemList);	
	    } 
	    
	  //API to get all products with sub cat
	    
	    @RequestMapping("/api/customers/subcat/productslist")
	    public MyResponse getProductsBySubCat(@RequestParam String subCats) {
	    	List<MyProduct> itemList = shoppursDao.getProductList(subCats);
	    	return generateResponse(true,"Product List fetched successfuly",itemList);
	    	
	    }     
	    
	//API to get all data
	    
	    @RequestMapping("/api/customers/cat_subcat")
	    public MyResponse syncData() {
	    	
	    	HashMap<String,Object> hashMap = shoppursDao.get_cat_subcat();
	    	return generateResponse(true,"Data fetched successfuly",hashMap);
	    	
	    }     
	    
	    
	// API to get shops by subcatid
	    
	    @RequestMapping("/api/customers/shoplist")
	    public MyResponse getshopsList(@RequestBody  ShopCode myshop) {
	    	
	    	HashMap<String,Object> hashMap = shoppursDao.shoplist(myshop.getSubcatid());
	    	return generateResponse(true,"Data fetched successfuly",hashMap);
	    	
	    }
	    
	    
// API to get products from shop db
	    
	    @RequestMapping("/api/customers/producfromshop")
	    public MyResponse getproductList(@RequestBody  UserID shopDetail) {
	    	
	    	HashMap<String,Object> hashMap = shoppursDao.get_producfromshop(shopDetail);
	    	return generateResponse(true,"Data fetched successfuly",hashMap);
	    	
	    }
	    
	//API to get userDetails
	    
	    @RequestMapping("/api/customrs/userProfile")
	    public MyResponse getUserProfile(@RequestParam String email) {
	    	
	    	MyUser item = shoppursDao.getUserDetails(email);
	    	return generateResponse(true,"Profile fetched successfuly",item);
	    	
	    }  
	    
	  //generate qr code
		@RequestMapping("/api/customrs/profile/generate_qrcode")
		public MyResponse generateQrCode(@RequestBody UserID item) {
			String status = shoppursDao.generateQrCode(item);
			if(status.equals("success")) {
				return generateResponse(true,"QrCode generated successfully.",null);
				
			}else if(status.equals("error")) {
				return generateResponse(true,"There is some problem in generating QrCode.",null);				
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
