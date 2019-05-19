package com.shoppurs.shop.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.shop.model.ProductRating;


public class ProductRatingMapper implements RowMapper<ProductRating>{

	@Override
	public ProductRating mapRow(ResultSet rs, int arg1) throws SQLException {
		ProductRating item = new ProductRating();
		if(rs.getString(1) == null || rs.getString(1).equals("null") || rs.getString(1).equals("NULL")) {
			item.setRatings(0f);
		}else {
			item.setRatings(rs.getFloat(1));
		}
		
		item.setRatingHits(rs.getInt(2));
		return item;
	}

}
