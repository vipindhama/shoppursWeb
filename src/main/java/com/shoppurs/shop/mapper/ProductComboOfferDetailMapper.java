package com.shoppurs.shop.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.shop.model.ProductComboDetails;

public class ProductComboOfferDetailMapper implements RowMapper<ProductComboDetails>{

	@Override
	public ProductComboDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProductComboDetails item = new ProductComboDetails();
		item.setId(rs.getInt("PCOD_ID"));
		item.setPcodPcoId(rs.getInt("PCOD_PCO_ID"));
		item.setPcodProdId(rs.getInt("PCOD_PROD_ID"));
		item.setPcodProdQty(rs.getInt("PCOD_PROD_QTY"));
		item.setPcodPrice(rs.getFloat("PCOD_PROD_PRICE"));
		item.setStatus(rs.getString("PCOD_STATUS"));
		return item;
	}

}
