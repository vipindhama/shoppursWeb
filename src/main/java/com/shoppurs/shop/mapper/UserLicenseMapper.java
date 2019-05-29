package com.shoppurs.shop.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.shop.model.UserLicense;

public class UserLicenseMapper implements RowMapper<UserLicense>{

	@Override
	public UserLicense mapRow(ResultSet rs, int arg1) throws SQLException {
		UserLicense userLicense = new UserLicense();
		userLicense.setId(rs.getInt("UL_ID"));
		userLicense.setShopCode(rs.getString("UL_SHOP_CODE"));
		userLicense.setShopMobile(rs.getString("UL_SHOP_MOBILE"));
		userLicense.setNumOfUsers(rs.getInt("UL_NO_OF_USER"));
		userLicense.setScheme(rs.getString("UL_SCHEME"));
		userLicense.setAmount(rs.getFloat("UL_AMOUNT"));
		userLicense.setLicenseType(rs.getString("UL_LICENSE_TYPE"));
		userLicense.setPurchaseDate(rs.getString("UL_LICENSE_PURCHASE_DATE"));
		userLicense.setRenewdDate(rs.getString("UL_LICENSE_RENEW_DATE"));
		userLicense.setExpiryDate(rs.getString("UL_LICENSE_EXPIRE_DATE"));
		return userLicense;
	}

}
