package com.shoppurs.shop.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.shop.model.ProductComboDetails;

public class ProductPriceOfferDetailMapper implements RowMapper<ProductComboDetails>{

	@Override
	public ProductComboDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProductComboDetails item = new ProductComboDetails();
		item.setId(rs.getInt("PPOD_ID"));
		item.setPcodPcoId(rs.getInt("PPOD_PPO_ID"));
		item.setPcodProdQty(rs.getInt("PPOD_PROD_QTY"));
		item.setPcodPrice(rs.getFloat("PPOD_PROD_PRICE"));
		item.setStatus(rs.getString("PPOD_STATUS"));
		return item;
	}

}
