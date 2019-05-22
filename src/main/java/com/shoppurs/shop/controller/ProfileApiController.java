package com.shoppurs.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoppurs.shop.dao.ProfileDao;
import com.shoppurs.shop.model.MyResponse;
import com.shoppurs.shop.model.ProductBarcode;
import com.shoppurs.shop.model.UserID;
import com.shoppurs.shop.model.requestModel.AddressReq;
import com.shoppurs.shop.model.requestModel.BasicProfileReq;
import com.shoppurs.shop.model.requestModel.DeliveryStatusReq;

@RestController("/api/shop/profile")
public class ProfileApiController {
	
	@Autowired
	private ProfileDao profileDao;
	
	//update basic profile
		@RequestMapping("/api/shop/profile/update_basic_details")
		public MyResponse updateBasicDetails(@RequestBody BasicProfileReq item) {
			String status = profileDao.updateBasicProfile(item);
			if(status.equals("success")) {
				return generateResponse(true,"Profile updated successfully.",null);
				
			}else if(status.equals("error")) {
				return generateResponse(true,"There is some problem in updating profile.",null);				
			}
			else {
				return generateResponse(false,status,null);			
				
			}
		}
		
		//update basic profile
		@RequestMapping("/api/shop/profile/update_address")
		public MyResponse updateAddress(@RequestBody AddressReq item) {
			String status = profileDao.updateAddress(item);
			if(status.equals("success")) {
				return generateResponse(true,"Address updated successfully.",null);
				
			}else if(status.equals("error")) {
				return generateResponse(true,"There is some problem in updating address.",null);				
			}
			else {
				return generateResponse(false,status,null);			
				
			}
		}
		
		//update delivery details
				@RequestMapping("/api/shop/profile/update_delivery_status")
				public MyResponse updateDeliveryStatus(@RequestBody DeliveryStatusReq item) {
					String status = profileDao.updateDeliveryStatus(item);
					if(status.equals("success")) {
						return generateResponse(true,"Delivery status updated successfully.",item);
						
					}else if(status.equals("error")) {
						return generateResponse(true,"There is some problem in updating delivery status.",null);				
					}
					else {
						return generateResponse(false,status,null);			
						
					}
				}		

				
				//generate qr code
				@RequestMapping("/api/shop/profile/generate_qrcode")
				public MyResponse generateQrCode(@RequestBody UserID item) {
					String status = profileDao.generateQrCode(item);
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
