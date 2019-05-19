package com.shoppurs.customers.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.customers.model.Country;
import com.shoppurs.customers.model.MyUser;

public class UserMapper implements RowMapper<MyUser>{

	@Override
	public MyUser mapRow(ResultSet rs, int arg1) throws SQLException {
		MyUser user = new MyUser();
		user.setUserid(rs.getString("USER_ID"));
		user.setUsername(rs.getString("USERNAME"));
		user.setUser_email(rs.getString("EMAIL"));
		user.setMobile(rs.getString("MOBILE"));
		user.setDbname(rs.getString("DB_NAME"));
		user.setDbusername(rs.getString("DB_USERNAME"));
		user.setDbpassword(rs.getString("DB_PASSWORD"));
		
		/**/
		return user;
	}

}
