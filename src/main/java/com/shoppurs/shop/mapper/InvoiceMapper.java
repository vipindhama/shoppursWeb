package com.shoppurs.shop.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.shop.model.Invoice;

public class InvoiceMapper implements RowMapper<Invoice>{

	@Override
	public Invoice mapRow(ResultSet rs, int arg1) throws SQLException {
		Invoice invoice = new Invoice();
		invoice.setInvId(rs.getInt("INVM_ID"));
		invoice.setInvNo(rs.getString("INVM_NO"));
        invoice.setInvTransId(rs.getString("INVM_TRANS_ID"));
        invoice.setInvDate(rs.getString("INVM_DATE"));
        invoice.setInvShopId(rs.getInt("INVM_SHOP_ID"));
        invoice.setInvShopCode(rs.getString("INVM_SHOP_CODE"));
        invoice.setInvShopName(rs.getString("INVM_SHOP_NAME"));
        invoice.setInvShopAddress(rs.getString("INVM_SHOP_ADDRESS"));
        invoice.setInvShopEmail(rs.getString("INVM_SHOP_EMAIL"));
        invoice.setInvShopMobile(rs.getString("INVM_SHOP_MOBILE"));
        invoice.setInvShopGSTIn(rs.getString("INVM_SHOP_GST"));
        invoice.setInvCustId(rs.getInt("INVM_CUST_ID"));
        //invoice.setCustCode(rs.getString("INVM_CUST_CODE"));
        invoice.setInvCustName(rs.getString("INVM_CUST_NAME"));
        invoice.setInvCustMobile(rs.getString("INVM_CUST_MOBILE"));
        invoice.setInvTotCGST(rs.getFloat("INVM_TOT_CGST"));
        invoice.setInvTotSGST(rs.getFloat("INVM_TOT_SGST"));
        invoice.setInvTotIGST(rs.getFloat("INVM_TOT_IGST"));
        invoice.setInvTotDisAmount(rs.getFloat("INVM_TOT_DISCOUNT_AMOUNT"));
        invoice.setInvTotTaxAmount(rs.getFloat("INVM_TOT_TAX_AMOUNT"));
        invoice.setInvTotAmount(rs.getFloat("INVM_TOT_AMOUNT"));
        invoice.setInvTotNetPayable(rs.getFloat("INVM_TOT_NET_PAYABLE"));
        invoice.setInvStatus(rs.getString("INVM_STATUS"));
        invoice.setInvCoupenId(rs.getString("INVM_COUPEN_ID"));
        invoice.setInvPaymentMode(rs.getString("INVM_PAYMENT_MODE"));
        invoice.setUserName(rs.getString("CREATED_BY"));
        //invoice.setDbName(myorder.getDbName());
       // invoice.setDbUserName(myorder.getDbUserName());
       // invoice.setDbPassword(myorder.getDbPassword());
        
		return invoice;
	}

}
