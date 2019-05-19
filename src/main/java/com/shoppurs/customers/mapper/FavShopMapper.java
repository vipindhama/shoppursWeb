package com.shoppurs.customers.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

public class FavShopMapper implements RowMapper<Map<Integer,String>>{

	@Override
	public Map<Integer,String> mapRow(ResultSet rs, int arg1) throws SQLException {
		Map<Integer,String> map = new HashMap();
		String shopCode = rs.getString(1);
		map.put(arg1,shopCode);
		return map;
	}

}
