package com.shoppurs.shop.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.shop.model.MyUser;

public class MyUserMapper implements RowMapper<MyUser>{

	@Override
	public MyUser mapRow(ResultSet rs, int arg1) throws SQLException {
		MyUser myUser = new MyUser();
		myUser.setId(rs.getString("RET_ID"));
		myUser.setUsername(rs.getString("RET_NAME"));
		myUser.setShopName(rs.getString("RET_SHOP_NAME"));
		myUser.setShopCode(rs.getString("RET_CODE"));
		myUser.setUserEmail(rs.getString("RET_EMAIL_ID"));
		myUser.setMobile(rs.getString("RET_MOBILE_NO"));
		myUser.setLanguage(rs.getString("RET_LANGUAGE"));
		//myUser.setMpassword(rs.getString("RET_CODE"));
		myUser.setCity(rs.getString("RET_CITY"));
		myUser.setProvince(rs.getString("RET_STATE"));
		myUser.setCountry(rs.getString("RET_COUNTRY"));
		myUser.setZip(rs.getString("RET_PIN_CODE"));
		myUser.setAddress(rs.getString("RET_ADDRESS"));
		myUser.setImeiNo(rs.getString("RET_CODE"));
		myUser.setPhoto(rs.getString("RET_PHOTO"));
		//myUser.setIdProof(rs.getString("RET_CODE"));
		myUser.setPanNo(rs.getString("RET_PAN_NO"));
		myUser.setAadharNo(rs.getString("RET_AADHAR_NO"));
		myUser.setGstNo(rs.getString("RET_GST_NO"));
		myUser.setUserLat(rs.getString("RET_LAT"));
		myUser.setUserLong(rs.getString("RET_LONG"));
		myUser.setUserType("Seller");
		myUser.setDbName(rs.getString("DB_NAME"));
		myUser.setDbUserName(rs.getString("DB_USER_NAME"));
		myUser.setDbPassword(rs.getString("DB_PASSWORD"));
		myUser.setCreatedDate(rs.getString("CREATED_DATE"));
		myUser.setCreatedBy(rs.getString("CREATED_BY"));
		myUser.setUpdatedBy(rs.getString("UPDATED_BY"));
		myUser.setUpdatedDate(rs.getString("UPDATED_DATE"));
		myUser.setIsDeliveryAvailable(rs.getString("IS_DELIVERY_AVAILABLE"));
		myUser.setMinDeliveryAmount(rs.getInt("MIN_DELIVERY_AMOUNT"));
		myUser.setBankName(rs.getString("RET_BANK_NAME"));
		myUser.setAccountNo(rs.getString("RET_BANK_ACCOUNT"));
		myUser.setIfscCode(rs.getString("RET_BANK_IFSC_CODE"));
		myUser.setBranchAddress(rs.getString("RET_BANK_BRANCH_ADDRESS"));
		myUser.setPanImage(rs.getString("RET_PAN_IMAGE"));
		myUser.setAadharImage(rs.getString("RET_AADHAR_IMAGE"));
		myUser.setChequeImage(rs.getString("RET_CHEQUE_IMAGE"));
		//myUser.setFcmToken(rs.getString("FCM_TOKEN"));
		myUser.setToken(rs.getString("TOKEN"));
		return myUser;
	}

}
