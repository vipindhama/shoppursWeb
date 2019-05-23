package com.shoppurs.shop.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.shop.model.Customer;

public class ShopCustomerMapper implements RowMapper<Customer> {

	@Override
	public Customer mapRow(ResultSet rs, int arg1) throws SQLException {
		Customer customer = new Customer();
		customer.setId(rs.getInt("CUST_ID"));
		customer.setCode(rs.getString("CUST_CODE"));
		customer.setName(rs.getString("CUST_NAME"));
		customer.setMobileNo(rs.getString("CUST_MOBILENO"));
		customer.setEmail(rs.getString("CUST_EMAILID"));
		customer.setAddress(rs.getString("CUST_ADDRESS"));
		customer.setPin(rs.getString("CUST_ZIP"));
		customer.setState(rs.getString("CUST_PROVINCE"));
		customer.setCity(rs.getString("CUST_CITY"));
		customer.setPhoto(rs.getString("CUST_PHOTO"));
		customer.setIsFav(rs.getString("IS_FAVOURITE"));
		customer.setUserCreateStatus(rs.getString("USER_CREATE_STATUS"));
		customer.setRatings(rs.getFloat("RATINGS"));
		return customer;
	}

}
