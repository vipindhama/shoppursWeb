package com.shoppurs.shop.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.shop.model.Barcode;

public class BarCodeMapper implements RowMapper<Barcode>{

	@Override
	public Barcode mapRow(ResultSet rs, int arg1) throws SQLException {
		Barcode item = new Barcode();
		item.setBarcode(rs.getString("PROD_BARCODE"));
		return item;
	}

}
