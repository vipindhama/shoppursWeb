package com.shoppurs.shop.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.shop.model.Coupon;
import com.shoppurs.shop.model.ProductComboOffer;

public class CouponMapper implements RowMapper<Coupon>{

	@Override
	public Coupon mapRow(ResultSet rs, int rowNum) throws SQLException {
		Coupon item = new Coupon();
		item.setId(rs.getInt("COP_ID"));
		item.setPercentage(rs.getFloat("COP_PER"));
		item.setAmount(rs.getFloat("COP_AMOUNT"));
		item.setName(rs.getString("COP_NAME"));
		item.setStatus(rs.getString("COP_STATUS"));
		item.setStartDate(rs.getString("COP_START_DATE"));
		item.setEndDate(rs.getString("COP_END_DATE"));
		return item;
	}

}
