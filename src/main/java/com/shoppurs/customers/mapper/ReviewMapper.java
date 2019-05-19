package com.shoppurs.customers.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.customers.model.ProductReview;


public class ReviewMapper implements RowMapper<ProductReview>{

	@Override
	public ProductReview mapRow(ResultSet rs, int arg1) throws SQLException {
		ProductReview item = new ProductReview();
		item.setReviewId(rs.getInt(1));
		item.setShopId(rs.getInt(2));
		item.setProdId(rs.getInt(3));
		item.setCustomerId(rs.getInt(4));
		item.setCustomerName(rs.getString(5));
		item.setReviewMessage(rs.getString(6));
		item.setRating(rs.getFloat(7));
		item.setCreatedDate(rs.getString(8));
		item.setUpdatedDate(rs.getString(9));
		return item;
	}

}
