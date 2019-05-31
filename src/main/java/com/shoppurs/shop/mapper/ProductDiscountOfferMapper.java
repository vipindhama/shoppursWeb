package com.shoppurs.shop.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.shop.model.ProductDiscountOffer;

public class ProductDiscountOfferMapper implements RowMapper<ProductDiscountOffer>{

	@Override
	public ProductDiscountOffer mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProductDiscountOffer item = new ProductDiscountOffer();
		item.setId(rs.getInt("PDO_ID"));
		item.setOfferName(rs.getString("PDO_NAME"));
		item.setProdBuyId(rs.getInt("PDO_PRD_ID_BUY"));
		item.setProdFreeId(rs.getInt("PDO_PRD_ID_FREE"));
		item.setProdBuyQty(rs.getInt("PDO_PRD_BUY_QTY"));
		item.setProdFreeQty(rs.getInt("PDO_PRD_FREE_QTY"));
		item.setStatus(rs.getString("PDO_STATUS"));
		item.setStartDate(rs.getString("PDO_START_DATE"));
		item.setEndDate(rs.getString("PDO_END_DATE"));
		return item;
	}

}
