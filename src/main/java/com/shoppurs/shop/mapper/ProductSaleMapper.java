package com.shoppurs.shop.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.shop.model.ProductSaleObject;

public class ProductSaleMapper implements RowMapper<ProductSaleObject>{

	@Override
	public ProductSaleObject mapRow(ResultSet rs, int arg1) throws SQLException {
		ProductSaleObject item = new ProductSaleObject();
		item.setOrderDate(rs.getString(1));
		item.setQty(rs.getInt(2));
		item.setAmount(rs.getFloat(3));
		return item;
	}

}
