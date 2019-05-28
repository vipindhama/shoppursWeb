package com.shoppurs.shop.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import com.shoppurs.connection.DaoConnection;
import com.shoppurs.shop.mapper.CustomerMapper;
import com.shoppurs.shop.mapper.InvoiceDetailMapper;
import com.shoppurs.shop.mapper.InvoiceMapper;
import com.shoppurs.shop.model.Customer;
import com.shoppurs.shop.model.Invoice;
import com.shoppurs.shop.model.InvoiceDetail;
import com.shoppurs.shop.model.MyPayment;
import com.shoppurs.shop.model.UserID;

public class TransactionDao {
	
	@Autowired
    private DaoConnection daoConnection;
	
	
	
	public String insertPaymentData(MyPayment item) {
		
		String status = "failure";
		JdbcTemplate dynamicShopJdbc = daoConnection.getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		JdbcTemplate transactionJdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.TRANS_DB_NAME,
				item.getDbUserName(),item.getDbPassword());
		
		String sql = "INSERT INTO `cust_payment`" + 
				"(`PAYMENT_ID`," + 
				"`PAYMENT_TRANSACTION_ID`," + 
				"`PAYMENT_TRANSACTION_TYPE`," + 
				"`PAYMENT_MERCHANT_ID`," + 
				"`PAYMENT_CARD_BRAND`," + 
				"`PAYMENT_CARD_LEVEL`," + 
				"`PAYMENT_CARD_NUMBER`," + 
				"`PAYMENT_CARD_TYPE`," + 
				"`PAYMENT_PAN_LENGTH`," + 
				"`PAYMENT_PAYMENT_ID`," + 
				"`PAYMENT_PIN_VERIFIED_FLAG`," + 
				"`PAYMENT_RESPONSE_CODE`," + 
				"`PAYMENT_RESPONSE_MESSAGE`," + 
				"`PAYMENT_CURRENCY_CODE`," + 
				"`PAYMENT_SECURE_HASH`," + 
				"`PAYMENT_AID`," + 
				"`PAYMENT_AID_NAME`," + 
				"`PAYMENT_RRN`," + 
				"`PAYMENT_TSI`," + 
				"`PAYMENT_TVR`," + 
				"`PAYMENT_MERCHANT_REF_INVOICE_NO`," + 
				"`PAYMENT_APPROVED`," + 
				"`PAYMENT_CARD_HOLDER_NAME`," + 
				"`PAYMENT_AMOUNT`," + 
				"`PAYMENT_STATUS`," + 
				"`PAYMENT_AUTH_CODE`," + 
				"`PAYMENT_BANK_MERCHANT_ID`," + 
				"`PAYMENT_BANK_TERMINAL_ID`," + 
				"`PAYMENT_BATCH_NO`," + 
				"`PAYMENT_PAYMENT_METHOD`," + 
				"`PAYMENT_PAYMENT_BRAND`," + 
				"`PAYMENT_PAYMENT_MODE`," + 
				"`PAYMENT_PAYMENT_DATE`," + 
				"`PAYMENT_PAYMENT_INVOICE_NO`," + 
				"`PAYMENT_MERCHANT_NAME`," + 
				"`PAYMENT_MERCHANT_ADDRESS`," + 
				"`PAYMENT_PAYMENT_TC`," + 
				"`CREATED_BY`," + 
				"`UPDATED_BY`," + 
				"`CREATED_DATE`," + 
				"`UPDATED_DATE`)" + 
				"VALUES " + 
				"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),now());";
		
		
		String sqlPayStatus = "UPDATE CUST_ORDER_DETAILS SET ORD_PAYMENT_STATUS = ? WHERE ORD_ORD_ID = "
				+ "(SELECT ORD_ID FROM CUST_ORDER WHERE ORD_NO = ?)";
		
		try {
			
			transactionJdbcTemplate.update(sql,0,item.getTransactionId(),item.getTransactionType(),item.getMerchantId(),
					item.getCardBrand(),item.getCardLevel(),
					item.getCardNumber(),item.getCardType(),item.getPanLength(),item.getPaymentId(),item.isPinVerifiedFlag(),
					item.getResponseCode(),item.getResponseMessage(),item.getCurrencyCode(),item.getSecureHash(),
					item.getAid(),item.getAidname(),item.getRrn(),item.getTsi(),item.getTvr(),
					item.getMerchantRefInvoiceNo(),item.isApproved(),item.getCardHolderName(),item.getAmount(),item.getStatus(),
					item.getAuthCode(),item.getBankMerchantId(),item.getBankTerminalId(),item.getBatchNo(),item.getPaymentMethod(),
					item.getPaymentBrand(),item.getPaymentMode(),item.getDate(),
					item.getInvoiceNo(),item.getMerchantName(),item.getMerchantAddress(),item.getTc(),
					item.getUserName(),item.getUserName());
			
			transactionJdbcTemplate.update(sqlPayStatus,item.getPayStatus(),item.getOrderNumber());
			
			dynamicShopJdbc.update(sql,0,item.getTransactionId(),item.getTransactionType(),item.getMerchantId(),
					item.getCardBrand(),item.getCardLevel(),
					item.getCardNumber(),item.getCardType(),item.getPanLength(),item.getPaymentId(),item.isPinVerifiedFlag(),
					item.getResponseCode(),item.getResponseMessage(),item.getCurrencyCode(),item.getSecureHash(),
					item.getAid(),item.getAidname(),item.getRrn(),item.getTsi(),item.getTvr(),
					item.getMerchantRefInvoiceNo(),item.isApproved(),item.getCardHolderName(),item.getAmount(),item.getStatus(),
					item.getAuthCode(),item.getBankMerchantId(),item.getBankTerminalId(),item.getBatchNo(),item.getPaymentMethod(),
					item.getPaymentBrand(),item.getPaymentMode(),item.getDate(),
					item.getInvoiceNo(),item.getMerchantName(),item.getMerchantAddress(),item.getTc(),
					item.getUserName(),item.getUserName());
			
			dynamicShopJdbc.update(sqlPayStatus,item.getPayStatus(),item.getOrderNumber());
			
			if(item.getUserCreateStatus() != null && !item.getUserCreateStatus().equals("S")) {
				
				String custSql ="SELECT * FROM CUSTOMER_INFO where CUST_CODE = ?";
				JdbcTemplate customerJdbc = daoConnection.getDynamicDataSource(DaoConnection.CUSTOMER_DB_NAME,
						item.getDbUserName(),item.getDbPassword());
				Customer customer = customerJdbc.query(custSql,new CustomerMapper(),item.getCustCode()).get(0);
				JdbcTemplate dynamicCustJdbc = daoConnection.getDynamicDataSource(customer.getDbName(),
						item.getDbUserName(),item.getDbPassword());
				
				dynamicCustJdbc.update(sql,0,item.getTransactionId(),item.getTransactionType(),item.getMerchantId(),
						item.getCardBrand(),item.getCardLevel(),
						item.getCardNumber(),item.getCardType(),item.getPanLength(),item.getPaymentId(),item.isPinVerifiedFlag(),
						item.getResponseCode(),item.getResponseMessage(),item.getCurrencyCode(),item.getSecureHash(),
						item.getAid(),item.getAidname(),item.getRrn(),item.getTsi(),item.getTvr(),
						item.getMerchantRefInvoiceNo(),item.isApproved(),item.getCardHolderName(),item.getAmount(),item.getStatus(),
						item.getAuthCode(),item.getBankMerchantId(),item.getBankTerminalId(),item.getBatchNo(),item.getPaymentMethod(),
						item.getPaymentBrand(),item.getPaymentMode(),item.getDate(),
						item.getInvoiceNo(),item.getMerchantName(),item.getMerchantAddress(),item.getTc(),
						item.getUserName(),item.getUserName());
				
				dynamicCustJdbc.update(sqlPayStatus,item.getPayStatus(),item.getOrderNumber());
				
			}
			
			
			
			status = "success";
			
		}catch(Exception e) {
			status = "error";
			e.printStackTrace();
		}
		
		return status;
	}
	
	public String generateInvoice(Invoice item) {
		String status = "failure";
		JdbcTemplate transactionJdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.TRANS_DB_NAME,
				item.getDbUserName(),item.getDbPassword());
		JdbcTemplate dynamicShopJdbc = daoConnection.getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		JdbcTemplate dynamicCustJdbc = null;
		
		String invSql = "INSERT INTO `invoice_master`" + 
				"(`INVM_ID`," + 
				"`INVM_NO`," + 
				"`INVM_TRANS_ID`," + 
				"`INVM_DATE`," + 
				"`INVM_SHOP_ID`," + 
				"`INVM_SHOP_CODE`," + 
				"`INVM_SHOP_NAME`," + 
				"`INVM_SHOP_ADDRESS`," + 
				"`INVM_SHOP_EMAIL`," + 
				"`INVM_SHOP_MOBILE`," + 
				"`INVM_SHOP_GST`," + 
				"`INVM_CUST_ID`," + 
				"`INVM_CUST_NAME`," + 
				"`INVM_CUST_MOBILE`," + 
				"`INVM_TOT_CGST`," + 
				"`INVM_TOT_SGST`," + 
				"`INVM_TOT_IGST`," + 
				"`INVM_TOT_DISCOUNT_AMOUNT`," + 
				"`INVM_TOT_TAX_AMOUNT`," + 
				"`INVM_TOT_AMOUNT`," + 
				"`INVM_TOT_NET_PAYABLE`," + 
				"`INVM_STATUS`," + 
				"`INVM_COUPEN_ID`," + 
				"`INVM_PAYMENT_MODE`," + 
				"`CREATED_BY`," + 
				"`UPDATED_BY`," + 
				"`CREATED_DATE`," + 
				"`UPDATED_DATE`)" + 
				"VALUES " + 
				"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),now());";
		
		String invDetailSql = "INSERT INTO `invoice_detail`" + 
				"(`INVD_ID`," + 
				"`INVD_INVM_ID`," + 
				"`INVD_PROD_ID`," + 
				"`INVD_PROD_NAME`," + 
				"`INVD_QTY`," + 
				"`INVD_HSN_CODE`," + 
				"`INVD_MRP`," + 
				"`INVD_SP`," + 
				"`INVD_DISCOUNT_PERCENTAGE`," + 
				"`INVD_DISCOUNT_AMOUNT`," + 
				"`INVD_CGST`," + 
				"`INVD_SGST`," + 
				"`INVD_IGST`," + 
				"`INVD_TAMOUNT`," + 
				"`INVD_OFFER_ID`," + 
				"`CREATED_BY`," + 
				"`UPDATED_BY`," + 
				"`CREATED_DATE`," + 
				"`UPDATED_DATE`)" + 
				"VALUES " + 
				"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),now());";
		
		
		try {
			
			transactionJdbcTemplate.update(invSql,0,item.getInvNo(),item.getInvTransId(),item.getInvDate(),item.getInvShopId(),
					item.getInvShopCode(),item.getInvShopName(),item.getInvShopAddress(),item.getInvShopEmail(),item.getInvShopMobile(),
					item.getInvShopGSTIn(),item.getInvCustId(),item.getInvCustName(),item.getInvCustMobile(),item.getInvTotCGST(),
					item.getInvTotSGST(),item.getInvTotIGST(),item.getInvTotDisAmount(),item.getInvTotTaxAmount(),item.getInvTotAmount(),
					item.getInvTotNetPayable(),item.getInvStatus(),item.getInvCoupenId(),item.getInvPaymentMode(),item.getUserName(),
					item.getUserName());
			
			dynamicShopJdbc.update(invSql,0,item.getInvNo(),item.getInvTransId(),item.getInvDate(),item.getInvShopId(),
					item.getInvShopCode(),item.getInvShopName(),item.getInvShopAddress(),item.getInvShopEmail(),item.getInvShopMobile(),
					item.getInvShopGSTIn(),item.getInvCustId(),item.getInvCustName(),item.getInvCustMobile(),item.getInvTotCGST(),
					item.getInvTotSGST(),item.getInvTotIGST(),item.getInvTotDisAmount(),item.getInvTotTaxAmount(),item.getInvTotAmount(),
					item.getInvTotNetPayable(),item.getInvStatus(),item.getInvCoupenId(),item.getInvPaymentMode(),item.getUserName(),
					item.getUserName());
			
            if(item.getCustCode() != null && !item.getCustCode().equals("")) {
				
				String sql="SELECT * FROM CUSTOMER_INFO where CUST_CODE = ?";
				JdbcTemplate customerJdbc = daoConnection.getDynamicDataSource(DaoConnection.CUSTOMER_DB_NAME,
						item.getDbUserName(),item.getDbPassword());
				Customer customer = customerJdbc.query(sql,new CustomerMapper(),item.getCustCode()).get(0);
				dynamicCustJdbc = daoConnection.getDynamicDataSource(customer.getDbName(),
						item.getDbUserName(),item.getDbPassword());
				
				dynamicCustJdbc.update(invSql,0,item.getInvNo(),item.getInvTransId(),item.getInvDate(),item.getInvShopId(),
						item.getInvShopCode(),item.getInvShopName(),item.getInvShopAddress(),item.getInvShopEmail(),item.getInvShopMobile(),
						item.getInvShopGSTIn(),item.getInvCustId(),item.getInvCustName(),item.getInvCustMobile(),item.getInvTotCGST(),
						item.getInvTotSGST(),item.getInvTotIGST(),item.getInvTotDisAmount(),item.getInvTotTaxAmount(),item.getInvTotAmount(),
						item.getInvTotNetPayable(),item.getInvStatus(),item.getInvCoupenId(),item.getInvPaymentMode(),item.getUserName(),
						item.getUserName());
				
			}
            
            
            List<InvoiceDetail> itemDetailList = item.getInvoiceDetailList();
            
            for(InvoiceDetail itemDetail : itemDetailList) {
            	transactionJdbcTemplate.update(invDetailSql,0,itemDetail.getInvDInvId(),itemDetail.getInvDProdId(),
            			itemDetail.getInvDProdName(),itemDetail.getInvDQty(),itemDetail.getInvDHsnCode(),itemDetail.getInvDMrp(),
            			itemDetail.getInvDSp(),itemDetail.getInvDDisPercentage(),itemDetail.getInvDDisAmount(),
            			itemDetail.getInvDCGST(),itemDetail.getInvDSGST(),itemDetail.getInvDIGST(),itemDetail.getInvDTotAmount(),
            			itemDetail.getInvdOfferId(),item.getUserName(),item.getUserName());
            	
            	dynamicShopJdbc.update(invDetailSql,0,itemDetail.getInvDInvId(),itemDetail.getInvDProdId(),
            			itemDetail.getInvDProdName(),itemDetail.getInvDQty(),itemDetail.getInvDHsnCode(),itemDetail.getInvDMrp(),
            			itemDetail.getInvDSp(),itemDetail.getInvDDisPercentage(),itemDetail.getInvDDisAmount(),
            			itemDetail.getInvDCGST(),itemDetail.getInvDSGST(),itemDetail.getInvDIGST(),itemDetail.getInvDTotAmount(),
            			itemDetail.getInvdOfferId(),item.getUserName(),item.getUserName());
            	
            	if(dynamicCustJdbc != null) {
            		
            		dynamicCustJdbc.update(invDetailSql,0,itemDetail.getInvDInvId(),itemDetail.getInvDProdId(),
                			itemDetail.getInvDProdName(),itemDetail.getInvDQty(),itemDetail.getInvDHsnCode(),itemDetail.getInvDMrp(),
                			itemDetail.getInvDSp(),itemDetail.getInvDDisPercentage(),itemDetail.getInvDDisAmount(),
                			itemDetail.getInvDCGST(),itemDetail.getInvDSGST(),itemDetail.getInvDIGST(),itemDetail.getInvDTotAmount(),
                			itemDetail.getInvdOfferId(),item.getUserName(),item.getUserName());
            	}
            }
            
            status = "success";
			
		}catch(Exception e) {
			status = "error";
			e.printStackTrace();
		}
		
		return status;
	}
	
	public Invoice getinvoice(UserID item) {
		Invoice invoice = null;
		List<InvoiceDetail> itemList= null;
		
		JdbcTemplate dynamicShopJdbc = daoConnection.getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		
		try {
			
			String sql = "SELECT im.*,cp.* FROM INVOICE_MASTER as im,CUST_ORDER as co,CUST_ORDER_DETAILS as cod,cust_payment as cp "
					+ "WHERE im.INVM_TRANS_ID = cod.ORD_TRANS_ID AND im.INVM_TRANS_ID = cp.PAYMENT_TRANSACTION_ID AND"
					+ " cod.ORD_ORD_ID = co.ORD_ID AND co.ORD_NO = ? GROUP BY cod.ORD_ORD_ID";
			
			invoice = dynamicShopJdbc.query(sql, new InvoiceMapper(),item.getNumber()).get(0);
			
			sql = "SELECT * FROM INVOICE_DETAIL WHERE INVD_INVM_ID = ?";
			itemList = dynamicShopJdbc.query(sql, new InvoiceDetailMapper(),invoice.getInvId());
			
			invoice.setInvoiceDetailList(itemList);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return invoice;
	}
	

}
