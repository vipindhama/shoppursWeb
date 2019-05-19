package com.shoppurs.shop.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.shop.model.ProductBarcode;


public class ProductBarCodeMapper implements RowMapper<ProductBarcode>{

	@Override
	public ProductBarcode mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProductBarcode item = new ProductBarcode();
		item.setProdId(rs.getInt("PROD_ID"));
		item.setProdBarCode(rs.getString("PROD_BARCODE"));
		return item;
	}

}
