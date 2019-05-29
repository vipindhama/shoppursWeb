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
import com.shoppurs.shop.model.OfferMaster;
import com.shoppurs.shop.model.UserID;

@RestController("/api/offers")
public class OfferApiController {
	


@Autowired
private OfferDao offerDao;

//Get Shoppurs Master Offers
@RequestMapping("/api/offers/get_master_offers")
public MyResponse getMasterOffers(@RequestBody UserID item) {
	List<OfferMaster> response = offerDao.getMasterOffers(item);
	return generateResponse(true,"Offers fetched succesfully.",response);	
}


private MyResponse generateResponse(boolean status,String message,Object data) {
	MyResponse myResponse = new MyResponse();
	myResponse.setStatus(status);
	myResponse.setMessage(message);
	myResponse.setResult(data);
	return myResponse;
}

}
