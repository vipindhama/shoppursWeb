package com.shoppurs.shop.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoppurs.customers.model.MyResponse;
import com.shoppurs.shop.dao.OfferDao;
import com.shoppurs.shop.model.Category;
import com.shoppurs.shop.model.MyOffer;

@RestController("/api/offers")
public class OfferApiController {
	


@Autowired
private OfferDao offerDao;

//Manage Shoppurs Offers
@RequestMapping("/api/offers/manageShoppursOffer")
public MyResponse manageShoppursOffer(@RequestBody MyOffer myoffer) {
	List<MyOffer> response;
	List<Category> catList;
	String status;
	if(myoffer.getAction()==3 || myoffer.getAction()==2) { //action 3 =get all offer, action 2 = get offer product id wise
		response = (List<MyOffer>) offerDao.manageShoppursOffer(myoffer);
		return generateResponse(true,"Offer Fetched successfully.",response);
	}else if(myoffer.getAction()==4) {
		catList = (List<Category>) offerDao.manageShoppursOffer(myoffer);
		return generateResponse(true,"Category Fetched successfully.",catList);
	}else {	// action 1 = create offer for shoppurs
		status =  (String) offerDao.manageShoppursOffer(myoffer);
		if(status.equals("success")) {
		return generateResponse(true,"Offer created successfully.",null);
		}else {
		return generateResponse(false,status,null);
		}
	}
}


//Manage Shop Offers
@RequestMapping("/api/offers/manageShopOffer")
public MyResponse manageShopsOffer(@RequestBody MyOffer myoffer) {
	List<MyOffer> response;
	String status;
	if(myoffer.getAction()==3 || myoffer.getAction() ==2) { //action 3 =get all offer, action 2 = get offer product id wise
		response = (List<MyOffer>) offerDao.manageShopOffer(myoffer);
		return generateResponse(true,"Offer Fetched successfully.",response);
	}else {	// action 1 = create offer for shop 
		status =  (String) offerDao.manageShopOffer(myoffer);
		if(status.equals("success")) {
		return generateResponse(true,"Offer created successfully.",null);
		}else {
		return generateResponse(false,status,null);
		}
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
