package com.shoppurs.shop.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.shop.model.OfferMaster;

public class OfferMasterMapper implements RowMapper<OfferMaster>{

	@Override
	public OfferMaster mapRow(ResultSet rs, int rowNum) throws SQLException {
		OfferMaster item = new OfferMaster();
		item.setId(rs.getInt("SOM_ID"));
		item.setName(rs.getString("SOM_NAME"));
		return item;
	}

}
