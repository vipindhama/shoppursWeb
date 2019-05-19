package com.shoppurs.customers.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import com.shoppurs.connection.DaoConnection;
import com.shoppurs.customers.mapper.MyShopMapper;
import com.shoppurs.customers.mapper.OrderDetailMapper;
import com.shoppurs.customers.mapper.OrderMapper;
import com.shoppurs.customers.model.MyOrder;
import com.shoppurs.customers.model.MyProduct;
import com.shoppurs.customers.model.MyShop;
import com.shoppurs.customers.model.OrderDetail;
import com.shoppurs.customers.model.UserID;
import com.shoppurs.shop.controller.ShoppursApiController;
import com.shoppurs.shop.mapper.RetCustomerMapper;
import com.shoppurs.shop.mapper.CustomerMapper;
import com.shoppurs.shop.mapper.MyUserMapper;
import com.shoppurs.customers.mapper.ProductMapper;
import com.shoppurs.shop.model.Category;
import com.shoppurs.shop.model.Customer;
import com.shoppurs.shop.model.Invoice;
import com.shoppurs.shop.model.InvoiceDetail;
import com.shoppurs.shop.model.MyDataSource;
import com.shoppurs.shop.model.MyOffer;
import com.shoppurs.shop.model.MyUser;

public class OrderManagerDao {
	private static final Logger log = LoggerFactory.getLogger(ShoppursApiController.class);
	
	@Autowired
    private DaoConnection daoConnection;
	
	@Autowired
    private MyDataSource myDataSource;

	
	public String generateOrder(List<MyOrder> myOrderList) {
		String status = "failure";
		int orderId = 0;
		String shopCode = "";
		JdbcTemplate dynamicShopJdbc = null;
		String sql="";
		List<MyProduct> myProductList = null;
		JdbcTemplate shopJdbcTemplate = null,transactionJdbcTemplate = null;
		
		try {
			
			for(MyOrder myorder : myOrderList) {
				
				if(shopJdbcTemplate == null) {
					shopJdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.SHOP_DB_NAME,myorder.getDbUserName(),
							myorder.getDbPassword());
					transactionJdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.TRANS_DB_NAME,myorder.getDbUserName(),
							myorder.getDbPassword());
				}
				
				shopCode = myorder.getShopCode();
		        sql = "SELECT * FROM retailer_info WHERE `ret_code` = ?";
			    List<MyShop> shopListDetails =shopJdbcTemplate.query(sql, new MyShopMapper(),shopCode);
			    MyShop myShop = shopListDetails.get(0);
				dynamicShopJdbc = daoConnection.getDynamicDataSource(myShop.getDbname(),myShop.getDbuser(),myShop.getDbpassword());
				
				
				if(orderId == 0) {
					sql="INSERT INTO `CUST_ORDER`" + 
							"(`ORD_NO`,`ORD_DATE`,`ORD_DELIVERY_NOTE`,`ORD_DELIVERY_MODE`," + 
							"`ORD_PAYMENT_MODE`,`ORD_IMAGE`,"
							+ "`ORD_CUST_CODE`,`ORD_CUST_NAME`," + 
							"`ORD_CUST_MOBILE`,`ORD_DELIVERY_ADDRESS`," + 
							"`ORD_PINCODE`,`ORD_TOTAL_QTY`,`ORD_TOTAL_AMT`,`ORD_COUPON_CODE`,`CREATED_BY`," + 
							"`UPDATED_BY`,`CREATED_DATE`,`UPDATED_DATE`,`ORD_STATUS`) " + 
							"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),now(),?)";
					
					transactionJdbcTemplate.update(sql,"",myorder.getOrderDate(),myorder.getOrderDeliveryNote(),
							myorder.getOrderDeliveryMode(),
							myorder.getPaymentMode(),myorder.getOrderImage(),myorder.getCustCode(),
							myorder.getCustName(),myorder.getMobileNo(),myorder.getDeliveryAddress(),myorder.getPinCode(),
							myorder.getTotalQuantity(),myorder.getToalAmount(),
							myorder.getOrdCouponId(),myorder.getCreatedBy(),
							myorder.getUpdateBy(),myorder.getOrderStatus());
					
					sql="SELECT MAX(ORD_ID)  FROM shoppurs_trans.CUST_ORDER";
					orderId = transactionJdbcTemplate.queryForObject(sql, Integer.class);
					myorder.setOrderNumber(myorder.getCustCode()+"-"+orderId);
					
					sql="UPDATE shoppurs_trans.CUST_ORDER SET ORD_NO = ? WHERE ORD_ID = ?";
					transactionJdbcTemplate.update(sql,myorder.getOrderNumber(),orderId);
				}
				
				myProductList = myorder.getMyProductList();
				int qty = 0;
				String barCode = "";
				for(MyProduct myProduct : myProductList) {
					qty = myProduct.getQty();
					barCode = myProduct.getProdBarCode();
					
					if(barCode == null || barCode.equals("null") || barCode.equals("")) {
						sql="select * from RET_PRODUCT WHERE PROD_ID = ?";
						myProduct = dynamicShopJdbc.query(sql,new ProductMapper(),myProduct.getProdId()).get(0);
					}else {
						sql="select * from RET_PRODUCT as rp, product_barcodes as pb WHERE rp.PROD_ID = pb.PROD_ID AND "
								+ "pb.PROD_BARCODE = ? GROUP BY rp.PROD_ID";
						myProduct = dynamicShopJdbc.query(sql,new ProductMapper(),barCode).get(0);
					}
					
					
					
					sql="INSERT INTO `cust_order_details`(`ORD_ORD_ID`,`ORD_QTY`,`ORD_OFFER_ID`,`ORD_SHOP_CODE`,`ORD_SHOP_NAME`,"
							+ "`ORD_SHOP_ADDRESS`,`ORD_SHOP_MOBILE`,`ORD_DELIVERY_MODE`,`ORD_STATUS`,`ORD_PAYMENT_STATUS`,"
							+ "`PROD_NAME`,`PROD_BARCODE`," + 
							"`PROD_DESC`,`PROD_MRP`,`PROD_SP`,`PROD_CGST`,`PROD_IGST`,`PROD_SGST`,`PROD_IMAGE_1`," + 
							"`PROD_IMAGE_2`,`PROD_IMAGE_3`,`PROD_CODE`,`PROD_ID`,`IS_BARCODE_AVAILABLE`) "
							+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					
					transactionJdbcTemplate.update(sql,orderId,qty,myProduct.getOfferId(),shopCode,myShop.getRetshopname(),
							myShop.getRetaddress(),myShop.getRetmobile(),myorder.getOrderDeliveryMode(),myorder.getOrderStatus(),
							myorder.getOderPaymentStatus(),myProduct.getProdName(),barCode,
							myProduct.getProdDesc(),myProduct.getProdMrp(),myProduct.getProdSp(),myProduct.getProdCgst(),
							myProduct.getProdIgst(),myProduct.getProdSgst(),myProduct.getProdImage1(),myProduct.getProdImage2(),
							myProduct.getProdImage3(),myProduct.getProdCode(),myProduct.getProdId(),myProduct.getIsBarcodeAvailable());
				}
					
			}
			
			status = "success";
			
		}catch(Exception e) {
			e.printStackTrace();
			status = "error";
		}
		
		
		return status+"-"+orderId;
	}
	
	public String placeOrder(List<MyOrder> myOrderList) {
		String status = null;
		//String shopDbName = null;
		String shopCode = "";
		JdbcTemplate dynamicShopJdbc = null,transactionJdbcTemplate =null,shopJdbcTemplate = null;
		String sql="";
		int orderId = 0,ordTotalQty = 0;
		float orderTotalPrice = 0f;
		String ordNum = "";
		List<MyProduct> myProductList = null;
		JdbcTemplate dynamicCustJdbc = null;
		int increament = 0;
		
		Invoice invoice = null;
		InvoiceDetail invoiceDetail = null;
		List<Invoice> invoiceList = new ArrayList();
		List<InvoiceDetail> invoiceDetailList = null;
		
		MyOrder item = myOrderList.get(0);
		
		if(shopJdbcTemplate == null) {
			shopJdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.SHOP_DB_NAME,item.getDbUserName(),
					item.getDbPassword());
			transactionJdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.TRANS_DB_NAME,item.getDbUserName(),
					item.getDbPassword());
		}
		
		sql="select count(*) from INVOICE_MASTER";
		int invMaxNO = transactionJdbcTemplate.queryForObject(sql,Integer.class);
		if(invMaxNO > 0) {
			sql="select max(INVM_ID) from INVOICE_MASTER";
			invMaxNO = transactionJdbcTemplate.queryForObject(sql,Integer.class) + 1;
		}else {
			invMaxNO = invMaxNO + 1;
		}
		
		try {
			
		for(MyOrder myorder : myOrderList) {
			
			if(orderId == 0)
			orderId = myorder.getOrderId();
			
			orderTotalPrice = orderTotalPrice + myorder.getToalAmount();
			ordTotalQty = ordTotalQty + myorder.getTotalQuantity();
			
			ordNum = myorder.getCustCode()+"-"+orderId;
			if(dynamicCustJdbc == null)
	        dynamicCustJdbc = daoConnection.getDynamicDataSource(myorder.getDbName(),myorder.getDbUserName(),myorder.getDbPassword());
			
	        shopCode = myorder.getShopCode();
	        sql = "SELECT * FROM retailer_info WHERE `ret_code` = ?";
		    List<MyShop> shopListDetails =shopJdbcTemplate.query(sql, new MyShopMapper(),shopCode);
		    MyShop myShop = shopListDetails.get(0);
			dynamicShopJdbc = daoConnection.getDynamicDataSource(myShop.getDbname(),myShop.getDbuser(),myShop.getDbpassword());
			
			
				sql="INSERT INTO `CUST_ORDER`" + 
						"(`ORD_ID`,`ORD_NO`,`ORD_DATE`,`ORD_DELIVERY_NOTE`,`ORD_DELIVERY_MODE`," + 
						"`ORD_PAYMENT_MODE`,`ORD_IMAGE`,"
						+ "`ORD_CUST_CODE`,`ORD_CUST_NAME`," + 
						"`ORD_CUST_MOBILE`,`ORD_DELIVERY_ADDRESS`," + 
						"`ORD_PINCODE`,`ORD_TOTAL_QTY`,`ORD_TOTAL_AMT`,`ORD_COUPON_CODE`,`CREATED_BY`," + 
						"`UPDATED_BY`,`CREATED_DATE`,`UPDATED_DATE`,`ORD_STATUS`) " + 
						"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),now(),?)";
				
				dynamicShopJdbc.update(sql,orderId,ordNum,myorder.getOrderDate(),myorder.getOrderDeliveryNote(),
						myorder.getOrderDeliveryMode(),
						myorder.getPaymentMode(),myorder.getOrderImage(),myorder.getCustCode(),
						myorder.getCustName(),myorder.getMobileNo(),myorder.getDeliveryAddress(),myorder.getPinCode(),
						myorder.getTotalQuantity(),myorder.getToalAmount(),
						myorder.getOrdCouponId(),myorder.getCreatedBy(),
						myorder.getUpdateBy(),myorder.getOrderStatus());
				
				if(increament == 0) {
					
				sql="INSERT INTO `CUST_ORDER`" + 
						"(`ORD_ID`,`ORD_NO`,`ORD_DATE`,`ORD_DELIVERY_NOTE`,`ORD_DELIVERY_MODE`," + 
						"`ORD_PAYMENT_MODE`,`ORD_IMAGE`,"
						+ "`ORD_CUST_CODE`,`ORD_CUST_NAME`," + 
						"`ORD_CUST_MOBILE`,`ORD_DELIVERY_ADDRESS`," + 
						"`ORD_PINCODE`,`ORD_TOTAL_QTY`,`ORD_TOTAL_AMT`,`ORD_COUPON_CODE`,`CREATED_BY`," + 
						"`UPDATED_BY`,`CREATED_DATE`,`UPDATED_DATE`,`ORD_STATUS`) " + 
						"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),now(),?)";
				
				dynamicCustJdbc.update(sql,orderId,ordNum,myorder.getOrderDate(),myorder.getOrderDeliveryNote(),
						myorder.getOrderDeliveryMode(),
						myorder.getPaymentMode(),myorder.getOrderImage(),myorder.getCustCode(),
						myorder.getCustName(),myorder.getMobileNo(),myorder.getDeliveryAddress(),myorder.getPinCode(),
						myorder.getTotalQuantity(),myorder.getToalAmount(),
						myorder.getOrdCouponId(),myorder.getCreatedBy(),
						myorder.getUpdateBy(),myorder.getOrderStatus());
			}
			
			increament++;
			
			myProductList = myorder.getMyProductList();
			int qty = 0;
			String barCode = "";
			float totIgst = 0f,totSgst = 0f,totCgst = 0f,totalDisAmount = 0f,totalPrice = 0f;
			invoiceDetailList = new ArrayList();
			for(MyProduct myProduct : myProductList) {
				qty = myProduct.getQty();
				barCode = myProduct.getProdBarCode();
				
				if(barCode == null || barCode.equals("null") || barCode.equals("")) {
					sql="select * from RET_PRODUCT WHERE PROD_ID = ?";
					myProduct = dynamicShopJdbc.query(sql,new ProductMapper(),myProduct.getProdId()).get(0);
				}else {
					sql="select * from RET_PRODUCT as rp, product_barcodes as pb WHERE rp.PROD_ID = pb.PROD_ID AND "
							+ "pb.PROD_BARCODE = ? GROUP BY rp.PROD_ID";
					myProduct = dynamicShopJdbc.query(sql,new ProductMapper(),barCode).get(0);
				}
				
				int ordId = 0;
				
				if(barCode == null || barCode.equals("null") || barCode.equals("")) {
					sql="SELECT ORD_ID FROM cust_order_details where PROD_CODE = ? AND ORD_SHOP_CODE = ? AND ORD_ORD_ID = ?";
					ordId = transactionJdbcTemplate.queryForObject(sql, Integer.class,myProduct.getProdCode(),shopCode,orderId);
				}else {
					sql="SELECT ORD_ID FROM cust_order_details where PROD_BARCODE = ? AND ORD_SHOP_CODE = ? AND ORD_ORD_ID = ?";
				    ordId = transactionJdbcTemplate.queryForObject(sql, Integer.class,barCode,shopCode,orderId);
				}
				
				
				
				
				sql="UPDATE cust_order_details SET ORD_PAYMENT_STATUS = ? WHERE ORD_ORD_ID = ? AND ORD_SHOP_CODE = ?";
				transactionJdbcTemplate.update(sql,myorder.getOderPaymentStatus(),orderId,shopCode);
				
				sql="INSERT INTO `cust_order_details`(`ORD_ID`,`ORD_ORD_ID`,`ORD_TRANS_ID`,`ORD_QTY`,`ORD_OFFER_ID`,`ORD_SHOP_CODE`,`ORD_SHOP_NAME`,"
						+ "`ORD_SHOP_ADDRESS`,`ORD_SHOP_MOBILE`,`ORD_DELIVERY_MODE`,`ORD_STATUS`,`ORD_PAYMENT_STATUS`,"
						+ "`PROD_NAME`,`PROD_BARCODE`," + 
						"`PROD_DESC`,`PROD_MRP`,`PROD_SP`,`PROD_CGST`,`PROD_IGST`,`PROD_SGST`,`PROD_IMAGE_1`," + 
						"`PROD_IMAGE_2`,`PROD_IMAGE_3`,`PROD_CODE`,`PROD_ID`,`IS_BARCODE_AVAILABLE`) "
						+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				
				dynamicShopJdbc.update(sql,ordId,orderId,myorder.getTransactionId(),qty,myProduct.getOfferId(),shopCode,myShop.getRetshopname(),
						myShop.getRetaddress(),myShop.getRetmobile(),myorder.getOrderDeliveryMode(),myorder.getOrderStatus(),
						myorder.getOderPaymentStatus(),myProduct.getProdName(),barCode,
						myProduct.getProdDesc(),myProduct.getProdMrp(),myProduct.getProdSp(),myProduct.getProdCgst(),
						myProduct.getProdIgst(),myProduct.getProdSgst(),myProduct.getProdImage1(),myProduct.getProdImage2(),
						myProduct.getProdImage3(),myProduct.getProdCode(),myProduct.getProdId(),myProduct.getIsBarcodeAvailable());
				
		
				dynamicCustJdbc.update(sql,ordId,orderId,myorder.getTransactionId(),qty,myProduct.getOfferId(),shopCode,myShop.getRetshopname(),
						myShop.getRetaddress(),myShop.getRetmobile(),myorder.getOrderDeliveryMode(),myorder.getOrderStatus(),
						myorder.getOderPaymentStatus(),myProduct.getProdName(),barCode,
						myProduct.getProdDesc(),myProduct.getProdMrp(),myProduct.getProdSp(),myProduct.getProdCgst(),
						myProduct.getProdIgst(),myProduct.getProdSgst(),myProduct.getProdImage1(),myProduct.getProdImage2(),
						myProduct.getProdImage3(),myProduct.getProdCode(),myProduct.getProdId(),myProduct.getIsBarcodeAvailable());
				
				sql="select PROD_QOH from RET_PRODUCT WHERE PROD_ID = ?";
				int counter = dynamicShopJdbc.queryForObject(sql,Integer.class,myProduct.getProdId());
				counter = counter - qty;
				sql="UPDATE RET_PRODUCT SET PROD_QOH = ? WHERE PROD_ID = ?";
				dynamicShopJdbc.update(sql,counter,myProduct.getProdId());
				
				if(barCode == null || barCode.equals("null") || barCode.equals("")) {
					//sql="UPDATE product_barcodes SET SOLD_STATUS = ? WHERE PROD_PROD_ID = ?";
					//dynamicShopJdbc.update(sql,"Y",myProduct.getProdId());
				}else {
					sql="UPDATE product_barcodes SET SOLD_STATUS = ? WHERE PROD_BARCODE = ?";
					dynamicShopJdbc.update(sql,"Y",myProduct.getProdBarCode());
				}
				
				
				totIgst = totIgst + (myProduct.getProdMrp() * qty * myProduct.getProdIgst()/100);
				totSgst = totSgst + (myProduct.getProdMrp() * qty * myProduct.getProdSgst()/100);
				totCgst = totCgst + (myProduct.getProdMrp() * qty * myProduct.getProdCgst()/100);
				totalDisAmount = totalDisAmount + (myProduct.getProdMrp() - myProduct.getProdSp());
				totalPrice = totalPrice + (myProduct.getProdSp() * qty);
				
				invoiceDetail = new InvoiceDetail();
                invoiceDetail.setInvDId(0);
                invoiceDetail.setInvDProdId(myProduct.getProdId());
                invoiceDetail.setInvDProdName(myProduct.getProdName());
                invoiceDetail.setInvDQty(qty);
                invoiceDetail.setInvDHsnCode(myProduct.getProdHsnCode());
                invoiceDetail.setInvDSp(myProduct.getProdSp());
                invoiceDetail.setInvDMrp(myProduct.getProdMrp());
                invoiceDetail.setInvDDisAmount(invoiceDetail.getInvDMrp() - invoiceDetail.getInvDSp());
                if(invoiceDetail.getInvDDisAmount() > 0){
                    invoiceDetail.setInvDDisPercentage(invoiceDetail.getInvDDisAmount() * 100 /invoiceDetail.getInvDMrp());
                }else{
                    invoiceDetail.setInvDDisPercentage(0f);
                }
                invoiceDetail.setInvDSGST(myProduct.getProdSgst());
                invoiceDetail.setInvDCGST(myProduct.getProdCgst());
                invoiceDetail.setInvDIGST(myProduct.getProdIgst());
                invoiceDetail.setInvDTotAmount(myProduct.getProdMrp() * qty);
                invoiceDetail.setInvdOfferId(myProduct.getOfferId());
                invoiceDetailList.add(invoiceDetail);
                
				
			}
			
			sql = "UPDATE cust_order_details SET ORD_TRANS_ID = ? WHERE ORD_ORD_ID = ?";
			transactionJdbcTemplate.update(sql,myorder.getTransactionId(),orderId);
			
			sql="SELECT count(*) FROM CUSTOMER_INFO where CUST_CODE = ?";
			int count = dynamicShopJdbc.queryForObject(sql, Integer.class,myorder.getCustCode());
			
			sql="SELECT * FROM CUSTOMER_INFO where CUST_CODE = ?";
			Customer customer = dynamicCustJdbc.query(sql,new CustomerMapper(),myorder.getCustCode()).get(0);
			
			if(count ==0) {
					
				sql="insert into customer_info (`CUST_ID`,`CUST_CODE`,`CUST_NAME`,`CUST_MOBILENO`,"
						+ "`CUST_EMAILID`,`PASSWORD`,`CUST_PHOTO`, `CUST_ADDRESS`,`CUST_ZIP`,`CUST_PROVINCE`,`CUST_CITY`,"
						+ "`CREATED_DATE`,`CREATED_BY`,`UPDATED_DATE`,`UPDATED_BY`,`USER_TYPE`,`ISACTIVE`,`SERVER_IP`,"
						+ "`DB_NAME`,`DB_USERNAME`,`DB_PASSWORD`)" + 
						" values (?,?,?,?,?,?,?,?,?,?,?,now(),?,now(),?,?,?,?,?,?,?)";
				
				dynamicShopJdbc.update(sql,customer.getId(),customer.getCode(),customer.getName(),customer.getMobileNo(),
						customer.getEmail(),
						"",customer.getPhoto(),customer.getAddress(),customer.getPin(),customer.getState(),customer.getCity(),
						customer.getCreatedBy(),customer.getUpdatedBy(),customer.getUserType(),customer.getIsActive(),
						customer.getIp(),customer.getDbName(),
						customer.getDbUserName(),customer.getDbPassword());
				
			}
			
			invoice = new Invoice();
			invoice.setInvId(0);
			
			
            invoice.setInvNo("INV"+invMaxNO);
            invoice.setInvTransId(myorder.getTransactionId());
            invoice.setInvDate("");
            invoice.setInvShopId(myShop.getRetId());
            invoice.setInvShopCode(myShop.getRetcode());
            invoice.setInvShopName(myShop.getRetshopname());
            invoice.setInvShopAddress(myShop.getRetaddress());
            invoice.setInvShopEmail(myShop.getRetemail());
            invoice.setInvShopMobile(myShop.getRetmobile());
            invoice.setInvShopGSTIn(myShop.getRetGstIn());
            invoice.setInvCustId(customer.getId());
            invoice.setCustCode(customer.getCode());
            invoice.setInvCustName(myorder.getCustName());
            invoice.setInvCustMobile(customer.getMobileNo());
            invoice.setInvTotCGST(totCgst);
            invoice.setInvTotSGST(totSgst);
            invoice.setInvTotIGST(totIgst);
            invoice.setInvTotDisAmount(totalDisAmount);
            invoice.setInvTotTaxAmount(invoice.getInvTotCGST() + invoice.getInvTotSGST());
            invoice.setInvTotAmount(totalPrice + invoice.getInvTotTaxAmount());
            invoice.setInvTotNetPayable(invoice.getInvTotAmount() - invoice.getInvTotDisAmount());
            invoice.setInvStatus("Generated");
            invoice.setInvCoupenId(myorder.getOrdCouponId());
            invoice.setInvPaymentMode(myorder.getPaymentMode());
            invoice.setUserName(myorder.getCreatedBy());
            invoice.setDbName(myorder.getDbName());
            invoice.setDbUserName(myorder.getDbUserName());
            invoice.setDbPassword(myorder.getDbPassword());
            invoice.setInvoiceDetailList(invoiceDetailList);
            invoiceList.add(invoice);
            
            invMaxNO = invMaxNO + 1;
				
		}
		
		sql = "UPDATE CUST_ORDER SET ORD_TOTAL_QTY = ?,ORD_TOTAL_AMT = ? WHERE ORD_ID = ?";
		dynamicCustJdbc.update(sql,ordTotalQty,orderTotalPrice,orderId);
		
		sql="DELETE FROM CUST_CART";
		dynamicCustJdbc.update(sql);
			
		status = generateInvoice(invoiceList);			
			
		}catch(Exception e) {
			status = "There is some problem in placing order...";
			e.printStackTrace();
		}
		
		return status;
	}
	
	
	public String generateInvoice(List<Invoice> itemList) {
		String status = "failure";
		JdbcTemplate dynamicShopJdbc = null,shopJdbcTemplate = null,transactionJdbcTemplate = null;
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
				"(?,?,?,now(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),now());";
		
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
		
		Invoice invItem = itemList.get(0);
		
		if(shopJdbcTemplate == null) {
			shopJdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.SHOP_DB_NAME,invItem.getDbUserName(),
					invItem.getDbPassword());
			transactionJdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.TRANS_DB_NAME,invItem.getDbUserName(),
					invItem.getDbPassword());
		}
		
		for(Invoice item : itemList) {
			//dynamicShopJdbc = daoConnection.getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
			String sql = "SELECT * FROM retailer_info WHERE `ret_code` = ?";
		    List<MyShop> shopListDetails =shopJdbcTemplate.query(sql, new MyShopMapper(),item.getInvShopCode());
		    MyShop myShop = shopListDetails.get(0);
			dynamicShopJdbc = daoConnection.getDynamicDataSource(myShop.getDbname(),item.getDbUserName(),item.getDbPassword());
			try {
				
				transactionJdbcTemplate.update(invSql,0,item.getInvNo(),item.getInvTransId(),item.getInvShopId(),
						item.getInvShopCode(),item.getInvShopName(),item.getInvShopAddress(),item.getInvShopEmail(),item.getInvShopMobile(),
						item.getInvShopGSTIn(),item.getInvCustId(),item.getInvCustName(),item.getInvCustMobile(),item.getInvTotCGST(),
						item.getInvTotSGST(),item.getInvTotIGST(),item.getInvTotDisAmount(),item.getInvTotTaxAmount(),item.getInvTotAmount(),
						item.getInvTotNetPayable(),item.getInvStatus(),item.getInvCoupenId(),item.getInvPaymentMode(),item.getUserName(),
						item.getUserName());
				
				sql="SELECT INVM_ID  FROM invoice_master where INVM_NO = ?";
				int invTransId = transactionJdbcTemplate.queryForObject(sql, Integer.class,item.getInvNo());
				
				dynamicShopJdbc.update(invSql,0,item.getInvNo(),item.getInvTransId(),item.getInvShopId(),
						item.getInvShopCode(),item.getInvShopName(),item.getInvShopAddress(),item.getInvShopEmail(),item.getInvShopMobile(),
						item.getInvShopGSTIn(),item.getInvCustId(),item.getInvCustName(),item.getInvCustMobile(),item.getInvTotCGST(),
						item.getInvTotSGST(),item.getInvTotIGST(),item.getInvTotDisAmount(),item.getInvTotTaxAmount(),item.getInvTotAmount(),
						item.getInvTotNetPayable(),item.getInvStatus(),item.getInvCoupenId(),item.getInvPaymentMode(),item.getUserName(),
						item.getUserName());
				
				sql="SELECT INVM_ID  FROM invoice_master where INVM_NO = ?";
				int invshopId = dynamicShopJdbc.queryForObject(sql, Integer.class,item.getInvNo());

				dynamicCustJdbc = daoConnection.getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
				dynamicCustJdbc.update(invSql,0,item.getInvNo(),item.getInvTransId(),item.getInvShopId(),
						item.getInvShopCode(),item.getInvShopName(),item.getInvShopAddress(),item.getInvShopEmail(),item.getInvShopMobile(),
						item.getInvShopGSTIn(),item.getInvCustId(),item.getInvCustName(),item.getInvCustMobile(),item.getInvTotCGST(),
						item.getInvTotSGST(),item.getInvTotIGST(),item.getInvTotDisAmount(),item.getInvTotTaxAmount(),item.getInvTotAmount(),
						item.getInvTotNetPayable(),item.getInvStatus(),item.getInvCoupenId(),item.getInvPaymentMode(),item.getUserName(),
						item.getUserName());
				
				sql="SELECT INVM_ID  FROM invoice_master where INVM_NO = ?";
				int invcustId = dynamicCustJdbc.queryForObject(sql, Integer.class,item.getInvNo());
	            
	            List<InvoiceDetail> itemDetailList = item.getInvoiceDetailList();
	            
	            for(InvoiceDetail itemDetail : itemDetailList) {
	            	transactionJdbcTemplate.update(invDetailSql,0,invTransId,itemDetail.getInvDProdId(),
	            			itemDetail.getInvDProdName(),itemDetail.getInvDQty(),itemDetail.getInvDHsnCode(),itemDetail.getInvDMrp(),
	            			itemDetail.getInvDSp(),itemDetail.getInvDDisPercentage(),itemDetail.getInvDDisAmount(),
	            			itemDetail.getInvDCGST(),itemDetail.getInvDSGST(),itemDetail.getInvDIGST(),itemDetail.getInvDTotAmount(),
	            			itemDetail.getInvdOfferId(),item.getUserName(),item.getUserName());
	            	
	            	dynamicShopJdbc.update(invDetailSql,0,invshopId,itemDetail.getInvDProdId(),
	            			itemDetail.getInvDProdName(),itemDetail.getInvDQty(),itemDetail.getInvDHsnCode(),itemDetail.getInvDMrp(),
	            			itemDetail.getInvDSp(),itemDetail.getInvDDisPercentage(),itemDetail.getInvDDisAmount(),
	            			itemDetail.getInvDCGST(),itemDetail.getInvDSGST(),itemDetail.getInvDIGST(),itemDetail.getInvDTotAmount(),
	            			itemDetail.getInvdOfferId(),item.getUserName(),item.getUserName());
	            	
	            	dynamicCustJdbc.update(invDetailSql,0,invcustId,itemDetail.getInvDProdId(),
                			itemDetail.getInvDProdName(),itemDetail.getInvDQty(),itemDetail.getInvDHsnCode(),itemDetail.getInvDMrp(),
                			itemDetail.getInvDSp(),itemDetail.getInvDDisPercentage(),itemDetail.getInvDDisAmount(),
                			itemDetail.getInvDCGST(),itemDetail.getInvDSGST(),itemDetail.getInvDIGST(),itemDetail.getInvDTotAmount(),
                			itemDetail.getInvdOfferId(),item.getUserName(),item.getUserName());
	            }
	            
	            status = "success";
				
			}catch(Exception e) {
				status = "error";
				e.printStackTrace();
			}
		}
		
		return status;
	}
	
	public List<MyOrder> getCustomerOrder(UserID item) {
		List<MyOrder> itemList = null;
		JdbcTemplate dynamicJdbc = daoConnection.getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		String sql="SELECT * FROM  CUST_ORDER";
		try {
			itemList = dynamicJdbc.query(sql,new OrderMapper());
			//sql="SELECT * FROM  CUST_ORDER_DETAILS WHERE ORD_ORD_ID = ?";
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return itemList;
	}
	
	public List<OrderDetail> getCustomerOrderDetail(UserID item) {
		List<OrderDetail> itemList = null;
		JdbcTemplate dynamicJdbc = daoConnection.getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		String sql="SELECT * FROM  CUST_ORDER_DETAILS WHERE ORD_ORD_ID = ?";
		try {
			itemList = dynamicJdbc.query(sql,new OrderDetailMapper(),item.getId());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return itemList;
	}
	
	//Manage order action 1 = create order, action 3 = get all order, action 2 = get order product id wise 
	/*public Object manageShoppursOrder(MyOrder myorder) {
		List<MyOrder> myorderList = new ArrayList<MyOrder>();
		MyOrder myOrder = null;
		int status = 1;
	    String message = "";
	  
	    String query = "{ call manage_orders(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }";
	    Connection conn = null;
		try {
			conn = transactionJdbcTemplate.getDataSource().getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(conn !=null) {
			ResultSet rs;
			
			    try {
			    	
			    	 // string orderDeliveryNote, orderDeliveryMode, paymentMode,
					//	custName,deliveryAddress, pinCode, orderStatus, 
					// int action, shopId, custId, totalQuantity
					// float toalAmount
			    	
			        CallableStatement stmt = conn.prepareCall(query);
			        stmt.setInt(1, myorder.getOrderId());
			        stmt.setString(2, myorder.getOrderNumber());
			        stmt.setString(3,myorder.getProdIds());
			        stmt.setString(4, myorder.getOrderDate());
			        stmt.setString(5, myorder.getOrderDeliveryNote());
			    	stmt.setString(6, myorder.getOrderDeliveryMode());
			    	stmt.setString(7, myorder.getPaymentMode());
			    	stmt.setInt(8, myorder.getShopId());
			    	stmt.setInt(9, myorder.getCustId());
			    	stmt.setString(10, myorder.getCustName());
			    	stmt.setString(11, myorder.getDeliveryAddress());
			    	stmt.setString(12, myorder.getPinCode());
			    	stmt.setInt(13, myorder.getTotalQuantity());
			    	stmt.setFloat(14, myorder.getToalAmount());
			    	stmt.setString(15, myorder.getOrderStatus());
			    	stmt.setString(16, myorder.getCreatedBy());
			    	stmt.setString(17, myorder.getUpdateBy());
			    	stmt.setInt(18, myorder.getAction());
			    	stmt.setString(19, myorder.getMobileNo());
			    	stmt.setString(20, myorder.getOrderImage());
			    	
			    
			    	//log.info("productId "+myoffer.getProductId());
			    	stmt.registerOutParameter(21, java.sql.Types.INTEGER);
			    	stmt.registerOutParameter(22, java.sql.Types.VARCHAR);
			        stmt.execute();		     
			        status = stmt.getInt(21);
			    	message = stmt.getString(22);
			    	if(myorder.getAction()==2 || myorder.getAction()==3 | myorder.getAction()==4) {
			    		rs = stmt.getResultSet();
			    		if(rs!=null) {
					    	while(rs.next()) {
					    		myOrder = new MyOrder();
					    		myOrder.setOrderId(rs.getInt(1));
					    		myOrder.setOrderNumber(rs.getString(2));
					    		myOrder.setOrderDate(rs.getString(3));
					    		myOrder.setOrderDeliveryNote(rs.getString(4));
					    		myOrder.setOrderDeliveryMode(rs.getString(5));
					    		myOrder.setPaymentMode(rs.getString(6));
					    		myOrder.setOrderImage(rs.getString(7));
					    		myOrder.setShopId(rs.getInt(8));
					    		myOrder.setCustId(rs.getInt(9));
					    		myOrder.setCustName(rs.getString(10));
					    		myOrder.setMobileNo(rs.getString(11));
					    		myOrder.setDeliveryAddress(rs.getString(12));
					    		myOrder.setPinCode(rs.getString(13));	
					    		myOrder.setTotalQuantity(rs.getInt(14));
					    		myOrder.setToalAmount(rs.getFloat(15));					    		
					    		myOrder.setOrderStatus(rs.getString(16));					    	
					    		myorderList.add(myOrder);
					    	}
			    		}
			    		
			    		return myorderList;
			    	}
			    	log.info("status "+status+" message "+message);
			    	
			    }catch (SQLException ex) {
			        System.out.println(ex.getMessage());
			}
		}
	    
		return status+"|"+message;
	}*/
	
}
