package com.shoppurs.customers.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.customers.model.Customer;

public class CustomerMapper implements RowMapper<Customer> {

	@Override
	public Customer mapRow(ResultSet rs, int arg1) throws SQLException {
		Customer customer = new Customer();
		customer.setId(rs.getString("RET_ID"));
		customer.setCode(rs.getString("RET_CODE"));
		customer.setName(rs.getString("RET_NAME"));
		customer.setMobileNo(rs.getString("RET_MOBILE_NO"));
		customer.setEmail(rs.getString("RET_EMAIL_ID"));
		customer.setAddress(rs.getString("RET_ADDRESS"));
		customer.setPin(rs.getString("RET_PIN_CODE"));
		customer.setPhoto(rs.getString("RET_PHOTO"));
		customer.setCountry(rs.getString("RET_COUNTRY"));
		customer.setState(rs.getString("RET_STATE"));
		customer.setCity(rs.getString("RET_CITY"));
		customer.setPanNo(rs.getString("RET_PAN_NO"));
		customer.setAadharNo(rs.getString("RET_AADHAR_NO"));
		customer.setGstNo(rs.getString("RET_GST_NO"));
		//customer.setCreatedBy(rs.getString(""));
		//customer.setUpdatedBy(rs.getString(""));
		customer.setCreatedDate(rs.getString("CREATED_DATE"));
		customer.setUpdatedDate(rs.getString("UPDATED_DATE"));
		return customer;
	}

}
