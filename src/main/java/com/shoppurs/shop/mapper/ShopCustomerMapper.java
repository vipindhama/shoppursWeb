package com.shoppurs.shop.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.shop.model.Customer;

public class ShopCustomerMapper implements RowMapper<Customer> {

	@Override
	public Customer mapRow(ResultSet rs, int arg1) throws SQLException {
		Customer customer = new Customer();
		customer.setId(rs.getInt(1));
		customer.setCode(rs.getString(2));
		customer.setName(rs.getString(3));
		customer.setMobileNo(rs.getString(4));
		customer.setEmail(rs.getString(5));
		//customer.setAddress(rs.getString("RET_ADDRESS"));
		customer.setPhoto(rs.getString(7));
		customer.setIsFav(rs.getString(8));
		customer.setRatings(rs.getFloat(9));
		return customer;
	}

}
