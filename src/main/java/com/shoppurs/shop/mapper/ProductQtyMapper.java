package com.shoppurs.shop.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.shop.model.ProductQty;


public class ProductQtyMapper implements RowMapper<ProductQty>{

	@Override
	public ProductQty mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProductQty item = new ProductQty();
		item.setProdId(rs.getInt("PROD_ID"));
		item.setIsBarCodeAvailable(rs.getString("IS_BARCODE_AVAILABLE"));
		item.setProdCode(rs.getString("PROD_CODE"));
		item.setProdBarCode(rs.getString("PROD_BARCODE"));
		item.setQty(rs.getInt("ORD_QTY"));
		return item;
	}

}
