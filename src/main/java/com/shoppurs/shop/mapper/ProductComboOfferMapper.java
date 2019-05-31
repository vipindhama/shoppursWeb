package com.shoppurs.shop.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.shop.model.ProductComboOffer;

public class ProductComboOfferMapper implements RowMapper<ProductComboOffer>{

	@Override
	public ProductComboOffer mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProductComboOffer item = new ProductComboOffer();
		item.setId(rs.getInt("PCO_ID"));
		item.setOfferName(rs.getString("PCO_NAME"));
		item.setStatus(rs.getString("PCO_STATUS"));
		item.setStartDate(rs.getString("PCO_START_DATE"));
		item.setEndDate(rs.getString("PCO_END_DATE"));
		return item;
	}

}
