package com.shoppurs.shop.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoppurs.customers.model.MyResponse;
import com.shoppurs.shop.dao.OfferDao;
import com.shoppurs.shop.model.Category;
import com.shoppurs.shop.model.Coupon;
import com.shoppurs.shop.model.MyOffer;
import com.shoppurs.shop.model.OfferMaster;
import com.shoppurs.shop.model.ProductComboOffer;
import com.shoppurs.shop.model.ProductDiscountOffer;
import com.shoppurs.shop.model.UserID;

@RestController("/api/offers")
public class OfferApiController {
	


@Autowired
private OfferDao offerDao;

//Get Shoppurs Master Offers
@RequestMapping("/api/offers/get_offer_list")
public MyResponse getAllOffers(@RequestBody UserID item) {
	Map<String,Object> response = offerDao.getAllOffers(item);
	return generateResponse(true,"Offers fetched succesfully.",response);	
}

//Get Shoppurs Master Offers
@RequestMapping("/api/offers/get_master_offers")
public MyResponse getMasterOffers(@RequestBody UserID item) {
	List<OfferMaster> response = offerDao.getMasterOffers(item);
	return generateResponse(true,"Offers fetched succesfully.",response);	
}

//Get product discount Offers
@RequestMapping("/api/offers/get_product_discount_offer")
public MyResponse getProductDiscountOffer(@RequestBody UserID item) {
	List<ProductDiscountOffer> response = offerDao.getProductDiscountOffer(item);
	return generateResponse(true,"Offers fetched succesfully.",response);	
}

//Get product combo Offers
@RequestMapping("/api/offers/get_product_combo_offer")
public MyResponse getProductComboOffer(@RequestBody UserID item) {
	List<ProductComboOffer> response = offerDao.getProductComboOffer(item);
	return generateResponse(true,"Offers fetched succesfully.",response);	
}

//Get product price Offers
@RequestMapping("/api/offers/get_product_price_offer")
public MyResponse getProductPriceOffer(@RequestBody UserID item) {
	List<ProductComboOffer> response = offerDao.getProductPriceOffer(item);
	return generateResponse(true,"Offers fetched succesfully.",response);	
}


//Get coupon Offers
@RequestMapping("/api/offers/get_coupon_offer")
public MyResponse getCouponOffers(@RequestBody UserID item) {
	List<Coupon> response = offerDao.getCouponOffers(item);
	return generateResponse(true,"Offers fetched succesfully.",response);	
}

//create product combo offers
@RequestMapping("/api/offers/create_product_combo_offer")
public MyResponse createProductComboOffer(@RequestBody ProductComboOffer item) {
	ProductComboOffer response = offerDao.createProductComboOffer(item);
	if(response != null){
		return generateResponse(true,"Offer created succesfully.",response);	
	}else {
		return generateResponse(true,"There is some problem occurred in creating offer.",null);	
	}
	
}

//create product combo offers
@RequestMapping("/api/offers/create_product_price_offer")
public MyResponse createProductPriceOffer(@RequestBody ProductComboOffer item) {
	ProductComboOffer response = offerDao.createProductPriceOffer(item);
	if(response != null){
		return generateResponse(true,"Offer created succesfully.",response);	
	}else {
		return generateResponse(true,"There is some problem occurred in creating offer.",null);	
	}
	
}


//create product discount offers
@RequestMapping("/api/offers/create_product_discount_offer")
public MyResponse createProductDiscountOffer(@RequestBody ProductDiscountOffer item) {
	ProductDiscountOffer response = offerDao.createProductDiscountOffer(item);
	if(response != null){
		return generateResponse(true,"Offer created succesfully.",response);	
	}else {
		return generateResponse(true,"There is some problem occurred in creating offer.",null);	
	}
	
}


//create coupon offers
@RequestMapping("/api/offers/create_coupon_offer")
public MyResponse createCouponOffer(@RequestBody Coupon item) {
	Coupon response = offerDao.createCouponOffer(item);
	if(response != null){
		return generateResponse(true,"Coupon offer created succesfully.",response);	
	}else {
		return generateResponse(true,"There is some problem occurred in creating offer.",null);	
	}
	
}


private MyResponse generateResponse(boolean status,String message,Object data) {
	MyResponse myResponse = new MyResponse();
	myResponse.setStatus(status);
	myResponse.setMessage(message);
	myResponse.setResult(data);
	return myResponse;
}

}
