package com.shoppurs.shop.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.shop.model.InvoiceDetail;

public class InvoiceDetailMapper implements RowMapper<InvoiceDetail>{

	@Override
	public InvoiceDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
		InvoiceDetail invoiceDetail = new InvoiceDetail();
        invoiceDetail.setInvDId(rs.getInt("INVD_ID"));
        invoiceDetail.setInvDProdId(rs.getInt("INVD_PROD_ID"));
        invoiceDetail.setInvDProdName(rs.getString("INVD_PROD_NAME"));
        invoiceDetail.setInvDQty(rs.getInt("INVD_QTY"));
        invoiceDetail.setInvDHsnCode(rs.getString("INVD_HSN_CODE"));
        invoiceDetail.setInvDSp(rs.getFloat("INVD_SP"));
        invoiceDetail.setInvDMrp(rs.getFloat("INVD_MRP"));
        invoiceDetail.setInvDDisAmount(rs.getFloat("INVD_DISCOUNT_AMOUNT"));
        invoiceDetail.setInvDDisPercentage(rs.getFloat("INVD_DISCOUNT_PERCENTAGE"));
        invoiceDetail.setInvDSGST(rs.getFloat("INVD_SGST"));
        invoiceDetail.setInvDCGST(rs.getFloat("INVD_CGST"));
        invoiceDetail.setInvDIGST(rs.getFloat("INVD_IGST"));
        invoiceDetail.setInvDTotAmount(rs.getFloat("INVD_TAMOUNT"));
        invoiceDetail.setInvdOfferId(rs.getString("INVD_OFFER_ID"));
        
		return invoiceDetail;
	}

}
