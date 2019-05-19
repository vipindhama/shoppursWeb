package com.shoppurs.customers.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.customers.model.MyCart;
import com.shoppurs.customers.model.MyProduct;

public class CartMapper implements RowMapper<MyCart>{

	@Override
	public MyCart mapRow(ResultSet rs, int arg1) throws SQLException {
		MyCart myCart = new MyCart();
        MyProduct item = new MyProduct();
		
		item.setProdId(rs.getInt("PROD_ID"));
		item.setProdSubCatId(rs.getInt("PROD_SUB_CAT_ID"));
		item.setProdName(rs.getString("PROD_NAME"));
		item.setProdCode(rs.getString("PROD_CODE"));
		item.setProdBarCode(rs.getString("PROD_BARCODE"));
		item.setProdDesc(rs.getString("PROD_DESC"));
		item.setProdReorderLevel(rs.getInt("PROD_REORDER_LEVEL"));
		item.setProdQoh(rs.getInt("PROD_QOH"));
		item.setProdHsnCode(rs.getString("PROD_HSN_CODE"));
		item.setProdCgst(rs.getFloat("PROD_CGST"));
		item.setProdIgst(rs.getFloat("PROD_IGST"));
		item.setProdSgst(rs.getFloat("PROD_SGST"));
		item.setProdWarranty(rs.getFloat("PROD_WARRANTY"));
		item.setProdMfgDate(rs.getString("PROD_MFG_DATE"));
		item.setProdExpiryDate(rs.getString("PROD_EXPIRY_DATE"));
		item.setProdMfgBy(rs.getString("PROD_MFG_BY"));
		item.setProdImage1(rs.getString("PROD_IMAGE_1"));
		item.setProdImage2(rs.getString("PROD_IMAGE_2"));
		item.setProdImage3(rs.getString("PROD_IMAGE_3"));
		item.setProdMrp(rs.getFloat("PROD_MRP"));
		item.setProdSp(rs.getFloat("PROD_SP"));
		item.setCreatedBy(rs.getString("CREATED_BY"));
		item.setUpdatedBy(rs.getString("UPDATED_BY"));
		item.setCreatedDate(rs.getString("CREATED_DATE"));
		item.setUpdatedDate(rs.getString("UPDATED_DATE"));
		myCart.setShopCode(rs.getString(1));
		myCart.setProdQty(rs.getInt(2));
		myCart.setMyProduct(item);
		return myCart;
	}

}
