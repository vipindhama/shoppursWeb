package com.shoppurs.shop.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.shoppurs.connection.DaoConnection;
import com.shoppurs.shop.controller.ShoppursApiController;
import com.shoppurs.shop.model.MyDataSource;
import com.shoppurs.shop.model.UserID;
import com.shoppurs.shop.model.requestModel.AddressReq;
import com.shoppurs.shop.model.requestModel.BasicProfileReq;
import com.shoppurs.shop.model.requestModel.DeliveryStatusReq;
import com.shoppurs.utilities.Constants;

public class ProfileDao {
	
	private static final Logger log = LoggerFactory.getLogger(ShoppursApiController.class);
	
	@Autowired
    private DaoConnection daoConnection;
	
	
	public String updateBasicProfile(BasicProfileReq item) {
		String status = "failure";
		byte[] imageByte1 = null;
		String directory="";
		JdbcTemplate dynamicJdbc = daoConnection.getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		JdbcTemplate authJdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.AUTH_DB_NAME,item.getDbUserName(),item.getDbPassword());
		JdbcTemplate shopJdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.SHOP_DB_NAME,item.getDbUserName(),item.getDbPassword());
		try {
			if(!item.getProfileImage().equals("no")) {
				
				File file = new File(Constants.FULL_IMAGE_PATH+"\\shops\\"+item.getShopCode()+"\\");
		        if (!file.exists()) {
		            if (file.mkdirs()) {
		                System.out.println("Directory is created!");
		            } else {
		                System.out.println("Failed to create directory!");
		            }
		        } 
		        
		        directory = Constants.FULL_IMAGE_PATH+"\\shops\\"+item.getShopCode()+"\\photo.jpg";
		        
	    		imageByte1=Base64.decodeBase64(item.getProfileImage());
				new FileOutputStream(directory).write(imageByte1);
				
			}
			
			item.setProfileImage(Constants.IMAGE_URL+"/shops/"+item.getShopCode()+"/photo.jpg");
			
			//item.setProfileImage(Constants.IMAGE_URL+"/shops/"+item.getShopCode()+"/photo.jpg");
			
			String sql="UPDATE USER_INFO SET USERNAME = ?,EMAIL = ?,PHOTO = ? WHERE MOBILE = ?";
			authJdbcTemplate.update(sql,item.getRetName(),item.getEmail(),item.getProfileImage(),item.getMobile());
			
			sql="UPDATE RETAILER_INFO SET RET_NAME = ?,RET_SHOP_NAME = ?,RET_GST_NO = ?,RET_EMAIL_ID = ?,RET_PHOTO = ? WHERE RET_MOBILE_NO = ?";
			shopJdbcTemplate.update(sql,item.getRetName(),item.getShopName(),item.getGstNo(),item.getEmail(),item.getProfileImage(),item.getMobile());
			
			sql="UPDATE RETAILER_INFO SET RET_NAME = ?,RET_SHOP_NAME = ?,RET_GST_NO = ?,RET_EMAIL_ID = ?,RET_PHOTO = ? WHERE RET_MOBILE_NO = ?";
			dynamicJdbc.update(sql,item.getRetName(),item.getShopName(),item.getGstNo(),item.getEmail(),item.getProfileImage(),item.getMobile());
			
			status= "success";
			
		}catch (IOException e) {
			e.printStackTrace();
			status= "error";
		}
		
		
		return status;
	}
	
	public String updateAddress(AddressReq item) {
		String status = "failure";
		JdbcTemplate dynamicJdbc = daoConnection.getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		JdbcTemplate authJdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.AUTH_DB_NAME,item.getDbUserName(),item.getDbPassword());
		JdbcTemplate shopJdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.SHOP_DB_NAME,item.getDbUserName(),item.getDbPassword());
		try {
			String sql="UPDATE USER_INFO SET ADDRESS = ?,PROVINCE = ?,CITY = ?,ZIP = ? WHERE MOBILE = ?";
			authJdbcTemplate.update(sql,item.getAddress(),item.getState(),item.getCity(),item.getPinCode(),item.getMobile());
			
			sql="UPDATE RETAILER_INFO SET RET_ADDRESS = ?,RET_COUNTRY = ?,RET_STATE = ?,RET_CITY = ?,"
					+ "RET_PIN_CODE = ?,RET_LAT = ?,RET_LONG = ? WHERE RET_MOBILE_NO = ?";
			shopJdbcTemplate.update(sql,item.getAddress(),item.getCountry(),item.getState(),item.getCity(),item.getPinCode(),
					item.getLatitude(),item.getLongitude(),item.getMobile());
			
			sql="UPDATE RETAILER_INFO SET RET_ADDRESS = ?,RET_COUNTRY = ?,RET_STATE = ?,RET_CITY = ?,"
					+ "RET_PIN_CODE = ?,RET_LAT = ?,RET_LONG = ? WHERE RET_MOBILE_NO = ?";
			dynamicJdbc.update(sql,item.getAddress(),item.getCountry(),item.getState(),item.getCity(),item.getPinCode(),
					item.getLatitude(),item.getLongitude(),item.getMobile());
			
			status= "success";
			
		}catch (Exception e) {
			e.printStackTrace();
			status= "error";
		}
		return status;
	}
	
	public String updateDeliveryStatus(DeliveryStatusReq item) {
		String status = "failure";
		JdbcTemplate dynamicJdbc = daoConnection.getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		//JdbcTemplate authJdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.AUTH_DB_NAME,item.getDbUserName(),item.getDbPassword());
		JdbcTemplate shopJdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.SHOP_DB_NAME,item.getDbUserName(),item.getDbPassword());
		try {
			//String sql="UPDATE USER_INFO SET ADDRESS = ?,PROVINCE = ?,CITY = ?,ZIP = ? WHERE USER_ID = ?";
			//authJdbcTemplate.update(sql,item.getAddress(),item.getState(),item.getCity(),item.getPinCode(),item.getId());
			
			String sql="UPDATE RETAILER_INFO SET IS_DELIVERY_AVAILABLE = ?,MIN_DELIVERY_AMOUNT = ? WHERE RET_MOBILE_NO = ?";
			shopJdbcTemplate.update(sql,item.getDeliveryStatus(),item.getAmount(),item.getMobile());
			
			sql="UPDATE RETAILER_INFO SET IS_DELIVERY_AVAILABLE = ?,MIN_DELIVERY_AMOUNT = ? WHERE RET_MOBILE_NO = ?";
			dynamicJdbc.update(sql,item.getDeliveryStatus(),item.getAmount(),item.getMobile());
			
			status= "success";
			
		}catch (Exception e) {
			e.printStackTrace();
			status= "error";
		}
		return status;
	}
	
	public String generateQrCode(UserID item) {
		String status = "failure";
		
		try {
    		
    		File file = new File(Constants.FULL_IMAGE_PATH+"\\shops\\"+item.getCode()+"\\");
	        if (!file.exists()) {
	            if (file.mkdirs()) {
	                System.out.println("Directory is created!");
	            } else {
	                System.out.println("Failed to create directory!");
	            }
	        } 
	        
	        String directory = Constants.FULL_IMAGE_PATH+"\\shops\\"+item.getCode()+"\\qrcode.PNG";
	        
			generateQRCodeImage(item.getMobile(),350,350,directory);
			
			status = "success";
			
		} catch (WriterException | IOException e) {
			// TODO Auto-generated catch block
			status = "error";
			e.printStackTrace();
		}
		
		return status;
	}
	
	private void generateQRCodeImage(String text, int width, int height, String filePath)
	        throws WriterException, IOException {
		
	    QRCodeWriter qrCodeWriter = new QRCodeWriter();
	    BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

	    Path path = FileSystems.getDefault().getPath(filePath);
	    MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
	}
	

}
