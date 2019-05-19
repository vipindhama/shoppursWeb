package com.shoppurs.shop.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.shop.model.SimpleItem;

public class SimpleItemMapper implements RowMapper<SimpleItem>{

	@Override
	public SimpleItem mapRow(ResultSet rs, int arg1) throws SQLException {
		SimpleItem item = new SimpleItem();
		item.setId(rs.getInt("id"));
		item.setName(rs.getString("name"));
		return item;
	}

}
