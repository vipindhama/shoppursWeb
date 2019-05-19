package com.shoppurs.customers.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.customers.model.Country;

public class CountryMapper implements RowMapper<Country>{

	@Override
	public Country mapRow(ResultSet rs, int arg1) throws SQLException {
		Country country = new Country();
		country.setId(rs.getInt("id"));
		country.setName(rs.getString("name"));
		country.setPhoneCode(rs.getString("phonecode"));
		country.setSortName(rs.getString("sortname"));
		return country;
	}

}
