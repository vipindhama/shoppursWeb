package com.shoppurs.shop.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.shop.model.DbUser;

public class DbUserMapper implements RowMapper<DbUser>{

	@Override
	public DbUser mapRow(ResultSet rs, int arg1) throws SQLException {
		DbUser myUser = new DbUser();
		myUser.setDbName(rs.getString(1));
		myUser.setDbUserName(rs.getString(2));
		myUser.setDbPassword(rs.getString(3));
		return myUser;
	}

}
