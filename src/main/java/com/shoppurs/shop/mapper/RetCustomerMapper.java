package com.shoppurs.shop.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.shop.model.Customer;

public class RetCustomerMapper implements RowMapper<Customer> {

	@Override
	public Customer mapRow(ResultSet rs, int arg1) throws SQLException {
		Customer customer = new Customer();
		customer.setId(rs.getInt("CUST_ID"));
		customer.setCode(rs.getString("CUST_CODE"));
		customer.setName(rs.getString("CUST_NAME"));
		customer.setMobileNo(rs.getString("CUST_MOBILENO"));
		customer.setEmail(rs.getString("CUST_EMAILID"));
		customer.setPhoto(rs.getString("CUST_PHOTO"));
		try
		{
			customer.setIsFav(rs.getString("IS_FAVOURITE"));
			customer.setRatings(rs.getFloat("RATINGS"));
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		customer.setAddress(rs.getString("CUST_ADDRESS"));
		customer.setPin(rs.getString("CUST_ZIP"));
		customer.setState(rs.getString("CUST_PROVINCE"));
		customer.setCity(rs.getString("CUST_CITY"));
		customer.setCreatedDate(rs.getString("CREATED_DATE"));
		customer.setCreatedBy(rs.getString("CREATED_BY"));
		customer.setUpdatedDate(rs.getString("UPDATED_DATE"));
		customer.setUpdatedBy(rs.getString("UPDATED_BY"));
		customer.setUserType(rs.getString("USER_TYPE"));
		customer.setIsActive(rs.getInt("ISACTIVE"));
		customer.setIp(rs.getString("SERVER_IP"));
		customer.setDbName(rs.getString("DB_NAME"));
		customer.setDbUserName(rs.getString("DB_USERNAME"));
		customer.setDbPassword(rs.getString("DB_PASSWORD"));
		customer.setUserCreateStatus(rs.getString("USER_CREATE_STATUS"));
		
		return customer;
	}

}
