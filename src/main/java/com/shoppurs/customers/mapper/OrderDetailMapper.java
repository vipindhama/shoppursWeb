package com.shoppurs.customers.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.customers.model.OrderDetail;


public class OrderDetailMapper implements RowMapper<OrderDetail>{

	@Override
	public OrderDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setOrderId(rs.getInt(2));
		orderDetail.setQty(rs.getInt(4));
		orderDetail.setOfferId(rs.getString(5));
		orderDetail.setShopCode(rs.getString(6));
		orderDetail.setShopName(rs.getString(7));
		orderDetail.setShopAddress(rs.getString(8));
		orderDetail.setShopMobile(rs.getString(9));
		orderDetail.setOrderDeliveryMode(rs.getString(10));
		orderDetail.setOrderStatus(rs.getString(11));
		orderDetail.setStatus(rs.getString(11));
		orderDetail.setOderPaymentStatus(rs.getString(12));
		orderDetail.setOrderDeliverBy(rs.getString(13));
		orderDetail.setOrderReason(rs.getString(14));
		orderDetail.setProdName(rs.getString(15));
		orderDetail.setProdBarCode(rs.getString(16));
		orderDetail.setProdDesc(rs.getString(17));
		orderDetail.setProdMrp(rs.getFloat(18));
		orderDetail.setProdSp(rs.getFloat(19));
		orderDetail.setProdCgst(rs.getFloat(20));
		orderDetail.setProdIgst(rs.getFloat(21));
		orderDetail.setProdSgst(rs.getFloat(22));
		orderDetail.setProdImage1(rs.getString(23));
		orderDetail.setProdImage2(rs.getString(24));
		orderDetail.setProdImage3(rs.getString(25));
		return orderDetail;
	}

}
