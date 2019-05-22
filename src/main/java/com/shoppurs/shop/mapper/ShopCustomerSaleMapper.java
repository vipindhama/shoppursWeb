package com.shoppurs.shop.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.shop.model.ShopCustomerSaleData;

public class ShopCustomerSaleMapper implements RowMapper<ShopCustomerSaleData>{

	@Override
	public ShopCustomerSaleData mapRow(ResultSet rs, int arg1) throws SQLException {
		ShopCustomerSaleData item = new ShopCustomerSaleData();
		item.setInvId(rs.getInt("INVM_ID"));
		item.setInvDate(rs.getString("INVM_DATE"));
		item.setCustName(rs.getString("INVM_CUST_NAME"));
		item.setNetPayable(rs.getFloat("INVM_TOT_NET_PAYABLE"));
		return item;
	}

}
