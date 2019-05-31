package com.shoppurs.shop.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.shop.model.ProductComboOffer;

public class ProductPriceOfferMapper implements RowMapper<ProductComboOffer>{

	@Override
	public ProductComboOffer mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProductComboOffer item = new ProductComboOffer();
		item.setId(rs.getInt("PPO_ID"));
		item.setProdId(rs.getInt("PPO_PROD_ID"));
		item.setOfferName(rs.getString("PPO_NAME"));
		item.setStatus(rs.getString("PPO_STATUS"));
		item.setStartDate(rs.getString("PPO_START_DATE"));
		item.setEndDate(rs.getString("PPO_END_DATE"));
		return item;
	}

}
