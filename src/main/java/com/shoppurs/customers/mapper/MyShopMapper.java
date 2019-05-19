package com.shoppurs.customers.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.customers.model.MyShop;
import com.shoppurs.customers.model.ShopCode;

public class MyShopMapper implements RowMapper<MyShop>{

	@Override
	public MyShop mapRow(ResultSet rs, int arg1) throws SQLException {
		MyShop shop = new MyShop();
		shop.setRetId(rs.getInt("RET_ID"));
		shop.setRetcode(rs.getString("RET_CODE"));
		shop.setRetname(rs.getString("RET_NAME"));
		shop.setRetshopname(rs.getString("RET_SHOP_NAME"));
		shop.setRetmobile(rs.getString("RET_MOBILE_NO"));
		shop.setRetGstIn(rs.getString("RET_GST_NO"));
		shop.setRetlanguage(rs.getString("RET_LANGUAGE"));
		shop.setRetaddress(rs.getString("RET_ADDRESS"));
		shop.setRetpincode(rs.getString("RET_PIN_CODE"));
		shop.setRetemail(rs.getString("RET_EMAIL_ID"));
		shop.setRetphoto(rs.getString("RET_PHOTO"));
		shop.setRetpassword(rs.getString("RET_PASSWORD"));
		shop.setRetcountry(rs.getString("RET_COUNTRY"));
		shop.setRetstate(rs.getString("RET_STATE"));
		shop.setRetcity(rs.getString("RET_CITY"));
		shop.setServerip(rs.getString("SERVER_IP"));
		shop.setDbname(rs.getString("DB_NAME"));
		shop.setDbuser(rs.getString("DB_USER_NAME"));
		shop.setDbpassword(rs.getString("DB_PASSWORD"));
		shop.setIsDeliveryAvailable(rs.getString("IS_DELIVERY_AVAILABLE"));
		shop.setMinDeliveryAmount(rs.getInt("MIN_DELIVERY_AMOUNT"));
		return shop;
	}

}
