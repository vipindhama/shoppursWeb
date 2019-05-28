package com.shoppurs.shop.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.shoppurs.connection.DaoConnection;
import com.shoppurs.customers.mapper.MyShopMapper;
import com.shoppurs.customers.model.MyOrder;
import com.shoppurs.customers.model.MyShop;
import com.shoppurs.shop.controller.ShoppursApiController;
import com.shoppurs.shop.mapper.CustomerMapper;
import com.shoppurs.shop.model.Barcode;
import com.shoppurs.shop.model.Customer;
import com.shoppurs.shop.model.Invoice;
import com.shoppurs.shop.model.InvoiceDetail;

public class ShoppursDeviceOrderDao {
	
private static final Logger log = LoggerFactory.getLogger(ShoppursApiController.class);
	
	@Autowired
    private DaoConnection daoConnection;
	
	public String generatePayDeviceOrder(List<MyOrder> myOrderList) {
		String status = "failure";
		int orderId = 0;
		String shopCode = "",orderNo = "";
		JdbcTemplate dynamicShopJdbc = null,shopJdbcTemplate = null,transactionJdbcTemplate=null;
		String sql="";
		List<com.shoppurs.customers.model.MyProduct> myProductList = null;
		
		try {
			
			for(MyOrder myorder : myOrderList) {
				
				if(shopJdbcTemplate == null) {
					shopJdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.SHOP_DB_NAME,myorder.getDbUserName(),
							myorder.getDbPassword());
					transactionJdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.TRANS_DB_NAME,myorder.getDbUserName(),
							myorder.getDbPassword());
				}
				
				shopCode = DaoConnection.SHOPPURS_SHOP_CODE;
		        sql = "SELECT * FROM retailer_info WHERE `ret_code` = ?";
			    List<MyShop> shopListDetails =shopJdbcTemplate.query(sql, new MyShopMapper(),shopCode);
			    MyShop myShop = shopListDetails.get(0);
				dynamicShopJdbc = daoConnection.getDynamicDataSource(DaoConnection.SHOPPURS_SHOP_DB_NAME,myorder.getDbUserName(),myorder.getDbPassword());
				
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
					
					sql="SELECT MAX(ORD_ID) FROM CUST_ORDER";
					orderId = transactionJdbcTemplate.queryForObject(sql, Integer.class);
					orderNo = myorder.getCustCode()+"-"+orderId;
					myorder.setOrderId(orderId);
					myorder.setOrderNumber(orderNo);
					
					sql="UPDATE CUST_ORDER SET ORD_NO = ? WHERE ORD_ID = ?";
					transactionJdbcTemplate.update(sql,myorder.getOrderNumber(),orderId);
					
					if(myorder.getPaymentMode().equals("Cash")) {
						sql="select count(*) from INVOICE_MASTER";
						int id = transactionJdbcTemplate.queryForObject(sql,Integer.class);
						if(id > 0) {
							sql="select max(INVM_ID) from INVOICE_MASTER";
							id = transactionJdbcTemplate.queryForObject(sql,Integer.class) + 1;
						}else {
							id = id + 1;
						}
						myorder.setTransactionId("trans"+id);
						
					}
				}
				
				myProductList = myorder.getMyProductList();
				int qty = 0;
				String barCode = "";
				for(com.shoppurs.customers.model.MyProduct myProduct : myProductList) {
					qty = myProduct.getQty();
					barCode = myProduct.getProdBarCode();
					
					if(barCode == null || barCode.equals("null") || barCode.equals("")) {
						sql="select * from RET_PRODUCT WHERE PROD_ID = ?";
						myProduct = dynamicShopJdbc.query(sql,new com.shoppurs.customers.mapper.ProductMapper(),myProduct.getProdId()).get(0);
					}else {
						sql="select * from RET_PRODUCT as rp, product_barcodes as pb WHERE rp.PROD_ID = pb.PROD_PROD_ID AND "
								+ "pb.PROD_BARCODE = ? GROUP BY rp.PROD_ID";
						myProduct = dynamicShopJdbc.query(sql,new com.shoppurs.customers.mapper.ProductMapper(),barCode).get(0);
					}
					
					
					sql="INSERT INTO `cust_order_details`(`ORD_ORD_ID`,`ORD_TRANS_ID`,`ORD_QTY`,`ORD_OFFER_ID`,`ORD_SHOP_CODE`,`ORD_SHOP_NAME`,"
							+ "`ORD_SHOP_ADDRESS`,`ORD_SHOP_MOBILE`,`ORD_DELIVERY_MODE`,`ORD_STATUS`,`ORD_PAYMENT_STATUS`,"
							+ "`PROD_NAME`,`PROD_BARCODE`," + 
							"`PROD_DESC`,`PROD_MRP`,`PROD_SP`,`PROD_CGST`,`PROD_IGST`,`PROD_SGST`,`PROD_IMAGE_1`," + 
							"`PROD_IMAGE_2`,`PROD_IMAGE_3`,`PROD_CODE`,`PROD_ID`,`IS_BARCODE_AVAILABLE`) "
							+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					
					transactionJdbcTemplate.update(sql,orderId,myorder.getTransactionId(),qty,myProduct.getOfferId(),shopCode,myShop.getRetshopname(),
							myShop.getRetaddress(),myShop.getRetmobile(),myorder.getOrderDeliveryMode(),myorder.getOrderStatus(),
							myorder.getOderPaymentStatus(),myProduct.getProdName(),barCode,
							myProduct.getProdDesc(),myProduct.getProdMrp(),myProduct.getProdSp(),myProduct.getProdCgst(),
							myProduct.getProdIgst(),myProduct.getProdSgst(),myProduct.getProdImage1(),myProduct.getProdImage2(),
							myProduct.getProdImage3(),myProduct.getProdCode(),myProduct.getProdId(),myProduct.getIsBarcodeAvailable());
				}
					
			}
			
			
			MyOrder myorder = myOrderList.get(0);
			if(myorder.getPaymentMode().equals("Cash")) {
				status = placeOrder(myOrderList);
			}else {
				status = "success";
			}
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
			status = "error";
		}
		
		
		return status+"-"+orderNo;
	}
	
	public String placeOrder(List<MyOrder> myOrderList) {
		String status = null;
		//String shopDbName = null;
		String custCode = "";
		String shopCode = "";
		JdbcTemplate dynamicShoppursShopJdbc = null,dynamicShopJdbc = null,transactionJdbcTemplate =null,shopJdbcTemplate = null;
		String sql="";
		int orderId = 0;
		String ordNum = "";
		List<com.shoppurs.customers.model.MyProduct> myProductList = null;
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
			dynamicShoppursShopJdbc = daoConnection.getDynamicDataSource(DaoConnection.SHOPPURS_SHOP_DB_NAME,item.getDbUserName(),
					item.getDbPassword());
		}
		
		try {
			
		for(MyOrder myorder : myOrderList) {
			
			 shopCode = DaoConnection.SHOPPURS_SHOP_CODE;
			
			
			if(ordNum.equals(""))
			ordNum = myorder.getOrderNumber();
			
			
			if(dynamicShopJdbc == null)
				dynamicShopJdbc = daoConnection.getDynamicDataSource(myorder.getDbName(),myorder.getDbUserName(),myorder.getDbPassword());
			
			sql = "SELECT * FROM retailer_info WHERE `ret_code` = ?";
		    List<MyShop> shopListDetails =dynamicShopJdbc.query(sql, new MyShopMapper(),myorder.getShopCode());
		    MyShop myShop = shopListDetails.get(0);
		   
			sql = "SELECT * FROM retailer_info WHERE `ret_code` = ?";
			MyShop myShoppursShop = dynamicShoppursShopJdbc.query(sql, new MyShopMapper(),DaoConnection.SHOPPURS_SHOP_CODE).get(0);
	        
			
			
				sql="INSERT INTO `CUST_ORDER`" + 
						"(`ORD_NO`,`ORD_DATE`,`ORD_DELIVERY_NOTE`,`ORD_DELIVERY_MODE`," + 
						"`ORD_PAYMENT_MODE`,`ORD_IMAGE`,"
						+ "`ORD_CUST_CODE`,`ORD_CUST_NAME`," + 
						"`ORD_CUST_MOBILE`,`ORD_DELIVERY_ADDRESS`," + 
						"`ORD_PINCODE`,`ORD_TOTAL_QTY`,`ORD_TOTAL_AMT`,`ORD_COUPON_CODE`,`CREATED_BY`," + 
						"`UPDATED_BY`,`CREATED_DATE`,`UPDATED_DATE`,`ORD_STATUS`) " + 
						"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),now(),?)";
				
				dynamicShopJdbc.update(sql,ordNum,myorder.getOrderDate(),myorder.getOrderDeliveryNote(),
						myorder.getOrderDeliveryMode(),
						myorder.getPaymentMode(),myorder.getOrderImage(),myorder.getCustCode(),
						myorder.getCustName(),myorder.getMobileNo(),myorder.getDeliveryAddress(),myorder.getPinCode(),
						myorder.getTotalQuantity(),myorder.getToalAmount(),
						myorder.getOrdCouponId(),myorder.getCreatedBy(),
						myorder.getUpdateBy(),myorder.getOrderStatus());
				
				dynamicShoppursShopJdbc.update(sql,ordNum,myorder.getOrderDate(),myorder.getOrderDeliveryNote(),
						myorder.getOrderDeliveryMode(),
						myorder.getPaymentMode(),myorder.getOrderImage(),myorder.getCustCode(),
						myorder.getCustName(),myorder.getMobileNo(),myorder.getDeliveryAddress(),myorder.getPinCode(),
						myorder.getTotalQuantity(),myorder.getToalAmount(),
						myorder.getOrdCouponId(),myorder.getCreatedBy(),
						myorder.getUpdateBy(),myorder.getOrderStatus());
				
				sql="SELECT MAX(ORD_ID) FROM CUST_ORDER";
				int shopOrderId = dynamicShopJdbc.queryForObject(sql, Integer.class);
				int ShoppursOrderId = dynamicShoppursShopJdbc.queryForObject(sql, Integer.class);
				sql="SELECT ORD_ID FROM CUST_ORDER WHERE ORD_NO = ?";
				orderId = transactionJdbcTemplate.queryForObject(sql, Integer.class,ordNum);
				
				
			
			myProductList = myorder.getMyProductList();
			int qty = 0;
			String barCode = "";
			float totIgst = 0f,totSgst = 0f,totCgst = 0f,totalDisAmount = 0f,totalPrice = 0f;
			invoiceDetailList = new ArrayList();
			List<Barcode> barcodeList = null;
			for(com.shoppurs.customers.model.MyProduct myProduct : myProductList) {
				qty = myProduct.getQty();
				barCode = myProduct.getProdBarCode();
				barcodeList = myProduct.getBarcodeList();
				
				if(barCode == null || barCode.equals("null") || barCode.equals("")) {
					sql="select * from RET_PRODUCT WHERE PROD_ID = ?";
					myProduct = dynamicShoppursShopJdbc.query(sql,new com.shoppurs.customers.mapper.ProductMapper(),myProduct.getProdId()).get(0);
				}else {
					sql="select * from RET_PRODUCT as rp, product_barcodes as pb WHERE rp.PROD_ID = pb.PROD_PROD_ID AND "
							+ "pb.PROD_BARCODE = ? GROUP BY rp.PROD_ID";
					myProduct = dynamicShoppursShopJdbc.query(sql,new com.shoppurs.customers.mapper.ProductMapper(),barCode).get(0);
				}
				
			/*	int ordId = 0;
				if(barCode == null || barCode.equals("null") || barCode.equals("")) {
					sql="SELECT ORD_ID FROM cust_order_details where PROD_ID = ? AND ORD_SHOP_CODE = ? AND ORD_ORD_ID = ?";
					ordId = transactionJdbcTemplate.queryForObject(sql, Integer.class,myProduct.getProdId(),shopCode,orderId);
				}else {
					sql="SELECT ORD_ID FROM cust_order_details where PROD_BARCODE = ? AND ORD_SHOP_CODE = ? AND ORD_ORD_ID = ?";
				    ordId = transactionJdbcTemplate.queryForObject(sql, Integer.class,barCode,shopCode,orderId);
				}*/
				
				sql="UPDATE cust_order_details SET ORD_PAYMENT_STATUS = ? WHERE ORD_ORD_ID = ? AND ORD_SHOP_CODE = ?";
				transactionJdbcTemplate.update(sql,myorder.getOderPaymentStatus(),orderId,shopCode);
				
			
				
				sql="INSERT INTO `cust_order_details`(`ORD_ORD_ID`,`ORD_TRANS_ID`,`ORD_QTY`,`ORD_OFFER_ID`,`ORD_SHOP_CODE`,`ORD_SHOP_NAME`,"
						+ "`ORD_SHOP_ADDRESS`,`ORD_SHOP_MOBILE`,`ORD_DELIVERY_MODE`,`ORD_STATUS`,`ORD_PAYMENT_STATUS`,"
						+ "`PROD_NAME`,`PROD_BARCODE`," + 
						"`PROD_DESC`,`PROD_MRP`,`PROD_SP`,`PROD_CGST`,`PROD_IGST`,`PROD_SGST`,`PROD_IMAGE_1`," + 
						"`PROD_IMAGE_2`,`PROD_IMAGE_3`,`PROD_CODE`,`PROD_ID`,`IS_BARCODE_AVAILABLE`) "
						+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				
				dynamicShopJdbc.update(sql,shopOrderId,myorder.getTransactionId(),qty,myProduct.getOfferId(),shopCode,myShoppursShop.getRetshopname(),
						myShoppursShop.getRetaddress(),myShoppursShop.getRetmobile(),myorder.getOrderDeliveryMode(),myorder.getOrderStatus(),
						myorder.getOderPaymentStatus(),myProduct.getProdName(),barCode,
						myProduct.getProdDesc(),myProduct.getProdMrp(),myProduct.getProdSp(),myProduct.getProdCgst(),
						myProduct.getProdIgst(),myProduct.getProdSgst(),myProduct.getProdImage1(),myProduct.getProdImage2(),
						myProduct.getProdImage3(),myProduct.getProdCode(),myProduct.getProdId(),myProduct.getIsBarcodeAvailable());
				
			
				dynamicShoppursShopJdbc.update(sql,ShoppursOrderId,myorder.getTransactionId(),qty,myProduct.getOfferId(),shopCode,myShoppursShop.getRetshopname(),
						myShoppursShop.getRetaddress(),myShoppursShop.getRetmobile(),myorder.getOrderDeliveryMode(),myorder.getOrderStatus(),
						myorder.getOderPaymentStatus(),myProduct.getProdName(),barCode,
						myProduct.getProdDesc(),myProduct.getProdMrp(),myProduct.getProdSp(),myProduct.getProdCgst(),
						myProduct.getProdIgst(),myProduct.getProdSgst(),myProduct.getProdImage1(),myProduct.getProdImage2(),
						myProduct.getProdImage3(),myProduct.getProdCode(),myProduct.getProdId(),myProduct.getIsBarcodeAvailable());
				
				
				sql="select PROD_QOH from RET_PRODUCT WHERE PROD_ID = ?";
				int counter = dynamicShoppursShopJdbc.queryForObject(sql,Integer.class,myProduct.getProdId());
				counter = counter - qty;
				sql="UPDATE RET_PRODUCT SET PROD_QOH = ? WHERE PROD_ID = ?";
				dynamicShoppursShopJdbc.update(sql,counter,myProduct.getProdId());
				
				log.info("barcodeList "+barcodeList);
				
				if(barCode == null || barCode.equals("null") || barCode.equals("")) {
					
				}else {
					for(Barcode barcode : barcodeList) {
						sql="UPDATE product_barcodes SET SOLD_STATUS = ? WHERE PROD_BARCODE = ?";
						dynamicShoppursShopJdbc.update(sql,"Y",barcode.getBarcode());
					}
					
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
			
			invoice = new Invoice();
			invoice.setInvId(0);
			sql="select count(*) from INVOICE_MASTER";
			int id = transactionJdbcTemplate.queryForObject(sql,Integer.class);
			if(id > 0) {
				sql="select max(INVM_ID) from INVOICE_MASTER";
				id = transactionJdbcTemplate.queryForObject(sql,Integer.class) + 1;
			}else {
				id = id + 1;
			}
			
            invoice.setInvNo("INV"+id);
            invoice.setInvTransId(myorder.getTransactionId());
            invoice.setInvDate("");
            invoice.setInvShopId(myShoppursShop.getRetId());
            invoice.setInvShopCode(myShoppursShop.getRetcode());
            invoice.setInvShopName(myShoppursShop.getRetshopname());
            invoice.setInvShopAddress(myShoppursShop.getRetaddress());
            invoice.setInvShopEmail(myShoppursShop.getRetemail());
            invoice.setInvShopMobile(myShoppursShop.getRetmobile());
            invoice.setInvShopGSTIn(myShoppursShop.getRetGstIn());
            invoice.setInvCustId(myShop.getRetId());
            invoice.setCustCode(myShop.getRetcode());
            invoice.setInvCustName(myShop.getRetshopname());
            invoice.setInvCustMobile(myShop.getRetmobile());
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
			
		}
		
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
		JdbcTemplate dynamicShoppursShopJdbc = null;
		
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
			dynamicShoppursShopJdbc = daoConnection.getDynamicDataSource(DaoConnection.SHOPPURS_SHOP_DB_NAME,invItem.getDbUserName(),
					invItem.getDbPassword());
		}
		
		for(Invoice item : itemList) {
			dynamicShopJdbc = daoConnection.getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
			try {
				
				transactionJdbcTemplate.update(invSql,0,item.getInvNo(),item.getInvTransId(),item.getInvShopId(),
						item.getInvShopCode(),item.getInvShopName(),item.getInvShopAddress(),item.getInvShopEmail(),item.getInvShopMobile(),
						item.getInvShopGSTIn(),item.getInvCustId(),item.getInvCustName(),item.getInvCustMobile(),item.getInvTotCGST(),
						item.getInvTotSGST(),item.getInvTotIGST(),item.getInvTotDisAmount(),item.getInvTotTaxAmount(),item.getInvTotAmount(),
						item.getInvTotNetPayable(),item.getInvStatus(),item.getInvCoupenId(),item.getInvPaymentMode(),item.getUserName(),
						item.getUserName());
				
				String sql="SELECT INVM_ID  FROM invoice_master where INVM_NO = ?";
				int invTransId = transactionJdbcTemplate.queryForObject(sql, Integer.class,item.getInvNo());
				
				dynamicShopJdbc.update(invSql,0,item.getInvNo(),item.getInvTransId(),item.getInvShopId(),
						item.getInvShopCode(),item.getInvShopName(),item.getInvShopAddress(),item.getInvShopEmail(),item.getInvShopMobile(),
						item.getInvShopGSTIn(),item.getInvCustId(),item.getInvCustName(),item.getInvCustMobile(),item.getInvTotCGST(),
						item.getInvTotSGST(),item.getInvTotIGST(),item.getInvTotDisAmount(),item.getInvTotTaxAmount(),item.getInvTotAmount(),
						item.getInvTotNetPayable(),item.getInvStatus(),item.getInvCoupenId(),item.getInvPaymentMode(),item.getUserName(),
						item.getUserName());
				
				dynamicShoppursShopJdbc.update(invSql,0,item.getInvNo(),item.getInvTransId(),item.getInvShopId(),
						item.getInvShopCode(),item.getInvShopName(),item.getInvShopAddress(),item.getInvShopEmail(),item.getInvShopMobile(),
						item.getInvShopGSTIn(),item.getInvCustId(),item.getInvCustName(),item.getInvCustMobile(),item.getInvTotCGST(),
						item.getInvTotSGST(),item.getInvTotIGST(),item.getInvTotDisAmount(),item.getInvTotTaxAmount(),item.getInvTotAmount(),
						item.getInvTotNetPayable(),item.getInvStatus(),item.getInvCoupenId(),item.getInvPaymentMode(),item.getUserName(),
						item.getUserName());
				
				sql="SELECT INVM_ID  FROM invoice_master where INVM_NO = ?";
				int invshopId = dynamicShopJdbc.queryForObject(sql, Integer.class,item.getInvNo());
				
		
				int invShoppursshopId = dynamicShoppursShopJdbc.queryForObject(sql, Integer.class,item.getInvNo());
				
	            
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
	            	
	            	dynamicShoppursShopJdbc.update(invDetailSql,0,invShoppursshopId,itemDetail.getInvDProdId(),
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

}
