package com.shoppurs.shop.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoppurs.customers.model.MyCart;
import com.shoppurs.shop.dao.ProductDao;
import com.shoppurs.shop.dao.ShoppursDao;
import com.shoppurs.shop.model.MyProduct;
import com.shoppurs.shop.model.MyResponse;
import com.shoppurs.shop.model.ProductBarcode;
import com.shoppurs.shop.model.ProductRating;
import com.shoppurs.shop.model.ProductSaleObject;
import com.shoppurs.shop.model.UserID;
import com.shoppurs.shop.model.requestModel.CategoryDetailReq;
import com.shoppurs.shop.model.requestModel.ProductRatingReq;

@RestController("/api/products")
public class ProductApiController {
	
	private static final Logger log = LoggerFactory.getLogger(ShoppursApiController.class);
	
	@Autowired
   	private ShoppursDao shoppursDao;
	
	@Autowired
   	private ProductDao productDao;
	
	
//Add product to master and retailer
    
    @RequestMapping(value = "/api/products/addProduct", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public MyResponse addProductWithRetailer(@RequestBody  List<MyProduct> item) {
    	
    	String[] statusMessage = productDao.addRetailerProduct(item).split("-");
    	
    	log.info("Details "+statusMessage[0] + " "+statusMessage[1]+" "+statusMessage[2]+" "+statusMessage.length);
    	
    	MyProduct myProduct = item.get(0);
    	
    	if(statusMessage[0].equals("0")) {
    		return generateResponse(true,"Product added successfully.",
    				productDao.getProductDetailsById(Integer.parseInt(statusMessage[2]),myProduct.getDbName(),
    						myProduct.getDbUserName(),myProduct.getDbPassword()));
    	}else {
    		return generateResponse(false,statusMessage[1],null);
    	}
    	
    }
    
    
//Update product of retailer
    
    @RequestMapping(value = "/api/products/updateProduct", method = RequestMethod.POST)
    public MyResponse updateProductWithRetailer(@RequestBody  MyProduct item) {
    	
    	String[] statusMessage = productDao.updateRetailerProduct(item).split("-");
    	
    	log.info("Details "+statusMessage[0] + " "+statusMessage[1]+" "+statusMessage[2]+" "+statusMessage.length);
    
    	
    	if(statusMessage[0].equals("0")) {
    		return generateResponse(true,"Product updated successfully.",
    				productDao.getProductDetailsById(item.getProdId(),item.getDbName(),
    						item.getDbUserName(),item.getDbPassword()));
    	}else {
    		return generateResponse(false,statusMessage[1],null);
    	}
    	
    }    
    
  //Add barcode
  		@RequestMapping("/api/products/add_product_barcode")
  		public MyResponse addBarCode(@RequestBody ProductBarcode item) {
  			String status = productDao.addBarCode(item);
  			if(status.equals("success")) {
  				return generateResponse(true,"Product Barcode added successfully.",null);
  				
  			}else if(status.equals("error")) {
  				return generateResponse(true,"There is some problem in adding product barcode.",null);				
  			}
  			else {
  				return generateResponse(false,status,null);			
  				
  			}
  		}	
  		
  	//API to get all products barcode
  	    
  	    @RequestMapping("/api/products/products_barcode_list")
  	    public MyResponse getProductsBarcodes(@RequestBody UserID item) {
  	    	
  	    	List<ProductBarcode> itemList = productDao.getBarCodes(item);
  	    	return generateResponse(true,"Product Barcode List fetched successfuly",itemList);
  	    	
  	    }  		
    
//API to get all products
    
    @RequestMapping("/api/products/productslist")
    public MyResponse getProductss(@RequestBody UserID item) {
    	
    	List<MyProduct> itemList = productDao.getProductList(item);
    	return generateResponse(true,"Product List fetched successfuly",itemList);
    	
    }
    
   
    
//API to get all products
    
    @RequestMapping("/api/products/ret_productslist")
    public MyResponse getRetProductss(@RequestBody UserID item) {
    	
    	List<MyProduct> itemList = productDao.getRetailerProductList(item);
    	return generateResponse(true,"Product List fetched successfuly",itemList);
    	
    }    
    
  //API to get product sale data
    @RequestMapping("/api/products/product_sale_data")
    public MyResponse getProductSaleData(@RequestBody UserID userId) {
    	
    	List<ProductSaleObject> itemList = productDao.getProductSaleData(userId);
    	return generateResponse(true,"Product Sale Data fetched successfuly",itemList);
    	
    }
    
//API to get all products with sub cat
    
    @RequestMapping("/api/products/subcat/productslist")
    public MyResponse getProductsBySubCat(@RequestBody CategoryDetailReq item) {
    	List<MyProduct> itemList = productDao.getProductList(item);
    	return generateResponse(true,"Product List fetched successfuly",itemList);
    	
    }
    
//API to get products ratings
    
    @RequestMapping("/api/products/product_ratings")
    public MyResponse getProductRatings(@RequestBody UserID item) {
    	ProductRating productRating = productDao.getProductRatings(item);
    	return generateResponse(true,"Product ratings fetched successfuly",productRating);
    	
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
