package com.shoppurs.customers.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.customers.model.ShopCode;

public class ShopCodeMapper implements RowMapper<ShopCode>{

	@Override
	public ShopCode mapRow(ResultSet rs, int arg1) throws SQLException {
		ShopCode shop = new ShopCode();
		shop.setShopcode(rs.getString("shop_code"));
		shop.setSubcatid(rs.getInt("subcat_id"));
	
		return shop;
	}

}
