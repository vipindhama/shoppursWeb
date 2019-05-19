package com.shoppurs.customers.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.customers.model.MyOrder;

public class OrderMapper implements RowMapper<MyOrder>{

	@Override
	public MyOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
		MyOrder myOrder = new MyOrder();
		myOrder.setOrderId(rs.getInt(1));
		myOrder.setOrderNumber(rs.getString(2));
		myOrder.setOrderDate(rs.getString(3));
		myOrder.setOrderDeliveryNote(rs.getString(4));
		myOrder.setOrderDeliveryMode(rs.getString(5));
		myOrder.setPaymentMode(rs.getString(6));
		myOrder.setOrderImage(rs.getString(7));
		/*myOrder.setShopCode(rs.getString(8));
		myOrder.setShopName(rs.getString(9));
		myOrder.setShopAddress(rs.getString(10));
		myOrder.setShopMobile(rs.getString(11));*/	
		myOrder.setCustCode(rs.getString(8));
		myOrder.setCustName(rs.getString(9));
		myOrder.setMobileNo(rs.getString(10));
		myOrder.setDeliveryAddress(rs.getString(11));
		myOrder.setPinCode(rs.getString(12));	
		myOrder.setTotalQuantity(rs.getInt(13));
		myOrder.setToalAmount(rs.getFloat(14));	
		myOrder.setOrdCouponId(rs.getString(15));
		myOrder.setOrderStatus(rs.getString("ORD_STATUS"));
		//myOrder.setOrderReason(rs.getString("ORD_REASON"));
		try {
			myOrder.setOderPaymentStatus(rs.getString("ORD_PAYMENT_STATUS"));
		}catch(Exception e) {
			
		}
		
		return myOrder;
	}

}
