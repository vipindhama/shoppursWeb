package com.shoppurs.shop.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.shop.model.CustomerSale;

public class CustomerSaleMapper implements RowMapper<CustomerSale>{

	@Override
	public CustomerSale mapRow(ResultSet rs, int arg1) throws SQLException {
		CustomerSale item = new CustomerSale();
		//item.setId(rs.getString(1));
		item.setOrderDate(rs.getString(2));
		item.setAmount(rs.getFloat(3));
		return item;
	}

}
