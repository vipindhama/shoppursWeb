package com.shoppurs.shop.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.shop.model.Customer;

public class CustomerMapper implements RowMapper<Customer> {

	@Override
	public Customer mapRow(ResultSet rs, int arg1) throws SQLException {
		Customer customer = new Customer();
		customer.setId(rs.getInt(1));
		customer.setCode(rs.getString(2));
		customer.setName(rs.getString(3));
		customer.setMobileNo(rs.getString(4));
		customer.setEmail(rs.getString(5));
		customer.setPhoto(rs.getString(7));
		customer.setAddress(rs.getString(8));
		customer.setPin(rs.getString(9));
		customer.setState(rs.getString(10));
		customer.setCity(rs.getString(11));
		customer.setCreatedDate(rs.getString(12));
		customer.setCreatedBy(rs.getString(13));
		customer.setUpdatedDate(rs.getString(14));
		customer.setUpdatedBy(rs.getString(15));
		customer.setUserType(rs.getString(16));
		customer.setIsActive(rs.getInt(17));
		customer.setIp(rs.getString(18));
		customer.setDbName(rs.getString(19));
		customer.setDbUserName(rs.getString(20));
		customer.setDbPassword(rs.getString(21));
		
		try {
			customer.setUserCreateStatus(rs.getString("USER_CREATE_STATUS"));
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return customer;
	}

}
