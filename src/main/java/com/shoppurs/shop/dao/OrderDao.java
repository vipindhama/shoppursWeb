package com.shoppurs.shop.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.shoppurs.connection.DaoConnection;
import com.shoppurs.customers.mapper.MyShopMapper;
import com.shoppurs.customers.mapper.OrderMapper;
import com.shoppurs.customers.model.MyOrder;
import com.shoppurs.customers.model.MyShop;
import com.shoppurs.shop.controller.ShoppursApiController;
import com.shoppurs.shop.mapper.RetCustomerMapper;
import com.shoppurs.shop.mapper.CustomerMapper;
import com.shoppurs.shop.mapper.OrderProductMapper;
import com.shoppurs.shop.mapper.ProductMapper;
import com.shoppurs.shop.mapper.ProductQtyMapper;
import com.shoppurs.shop.mapper.ProductSaleMapper;
import com.shoppurs.shop.model.Barcode;
import com.shoppurs.shop.model.Customer;
import com.shoppurs.shop.model.Invoice;
import com.shoppurs.shop.model.InvoiceDetail;
import com.shoppurs.shop.model.MyDataSource;
import com.shoppurs.shop.model.MyProduct;
import com.shoppurs.shop.model.ProductQty;
import com.shoppurs.shop.model.SetOrderStatusObject;
import com.shoppurs.shop.model.UserID;

public class OrderDao {
	
	private static final Logger log = LoggerFactory.getLogger(ShoppursApiController.class);
	
	@Autowired
    private DaoConnection daoConnection;
	
	
	public String generateOrder(List<MyOrder> myOrderList) {
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
		
		
		return status+"-"+orderId;
	}
	
	
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
				
				shopCode = myorder.getShopCode();
		        sql = "SELECT * FROM retailer_info WHERE `ret_code` = ?";
			    List<MyShop> shopListDetails =shopJdbcTemplate.query(sql, new MyShopMapper(),shopCode);
			    MyShop myShop = shopListDetails.get(0);
				dynamicShopJdbc = daoConnection.getDynamicDataSource(myorder.getDbName(),myorder.getDbUserName(),myorder.getDbPassword());
				
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
		
		
		return status+"-"+orderId;
	}
	
	public String placeOrder(List<MyOrder> myOrderList) {
		String status = null;
		//String shopDbName = null;
		String custCode = "";
		String shopCode = "";
		JdbcTemplate dynamicShopJdbc = null,transactionJdbcTemplate =null,shopJdbcTemplate = null;
		String sql="";
		int orderId = 0;
		String ordNum = "";
		List<com.shoppurs.customers.model.MyProduct> myProductList = null;
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
		
		try {
			
		for(MyOrder myorder : myOrderList) {
			
			 shopCode = myorder.getShopCode();
			 
			if(orderId == 0)
			orderId = myorder.getOrderId();
			
			ordNum = myorder.getCustCode()+"-"+orderId;
			if(dynamicShopJdbc == null)
				dynamicShopJdbc = daoConnection.getDynamicDataSource(myorder.getDbName(),myorder.getDbUserName(),myorder.getDbPassword());
			
			sql = "SELECT * FROM retailer_info WHERE `ret_code` = ?";
		    List<MyShop> shopListDetails =dynamicShopJdbc.query(sql, new MyShopMapper(),shopCode);
		    MyShop myShop = shopListDetails.get(0);
		    
		    
			
			custCode = myorder.getCustCode();
			Customer customer = null;
			if(custCode == null || custCode.equals("")) {
				
			}else {
				sql="SELECT * FROM CUSTOMER_INFO where CUST_CODE = ?";
				JdbcTemplate customerJdbc = daoConnection.getDynamicDataSource(DaoConnection.CUSTOMER_DB_NAME,
						myorder.getDbUserName(),myorder.getDbPassword());
				customer = customerJdbc.query(sql,new CustomerMapper(),myorder.getCustCode()).get(0);
				dynamicCustJdbc = daoConnection.getDynamicDataSource(customer.getDbName(),
						myorder.getDbUserName(),myorder.getDbPassword());
			}
	        
			
			
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
				if(dynamicCustJdbc != null) {
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
				
			}	
			
			increament++;
			
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
					myProduct = dynamicShopJdbc.query(sql,new com.shoppurs.customers.mapper.ProductMapper(),myProduct.getProdId()).get(0);
				}else {
					sql="select * from RET_PRODUCT as rp, product_barcodes as pb WHERE rp.PROD_ID = pb.PROD_PROD_ID AND "
							+ "pb.PROD_BARCODE = ? GROUP BY rp.PROD_ID";
					myProduct = dynamicShopJdbc.query(sql,new com.shoppurs.customers.mapper.ProductMapper(),barCode).get(0);
				}
				
				int ordId = 0;
				if(barCode == null || barCode.equals("null") || barCode.equals("")) {
					sql="SELECT ORD_ID FROM cust_order_details where PROD_ID = ? AND ORD_SHOP_CODE = ? AND ORD_ORD_ID = ?";
					ordId = transactionJdbcTemplate.queryForObject(sql, Integer.class,myProduct.getProdId(),shopCode,orderId);
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
				
				if(dynamicCustJdbc != null) {
				
					dynamicCustJdbc.update(sql,ordId,orderId,myorder.getTransactionId(),qty,myProduct.getOfferId(),shopCode,myShop.getRetshopname(),
							myShop.getRetaddress(),myShop.getRetmobile(),myorder.getOrderDeliveryMode(),myorder.getOrderStatus(),
							myorder.getOderPaymentStatus(),myProduct.getProdName(),barCode,
							myProduct.getProdDesc(),myProduct.getProdMrp(),myProduct.getProdSp(),myProduct.getProdCgst(),
							myProduct.getProdIgst(),myProduct.getProdSgst(),myProduct.getProdImage1(),myProduct.getProdImage2(),
							myProduct.getProdImage3(),myProduct.getProdCode(),myProduct.getProdId(),myProduct.getIsBarcodeAvailable());
				}
				
				
				
				sql="select PROD_QOH from RET_PRODUCT WHERE PROD_ID = ?";
				int counter = dynamicShopJdbc.queryForObject(sql,Integer.class,myProduct.getProdId());
				counter = counter - qty;
				sql="UPDATE RET_PRODUCT SET PROD_QOH = ? WHERE PROD_ID = ?";
				dynamicShopJdbc.update(sql,counter,myProduct.getProdId());
				
				if(barCode == null || barCode.equals("null") || barCode.equals("")) {
					//sql="UPDATE product_barcodes SET SOLD_STATUS = ? WHERE PROD_PROD_ID = ?";
					//dynamicShopJdbc.update(sql,"Y",myProduct.getProdId());
				}else {
					for(Barcode barcode : barcodeList) {
						sql="UPDATE product_barcodes SET SOLD_STATUS = ? WHERE PROD_BARCODE = ?";
						dynamicShopJdbc.update(sql,"Y",barcode.getBarcode());
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
            invoice.setInvShopId(myShop.getRetId());
            invoice.setInvShopCode(myShop.getRetcode());
            invoice.setInvShopName(myShop.getRetshopname());
            invoice.setInvShopAddress(myShop.getRetaddress());
            invoice.setInvShopEmail(myShop.getRetemail());
            invoice.setInvShopMobile(myShop.getRetmobile());
            invoice.setInvShopGSTIn(myShop.getRetGstIn());
            invoice.setInvCustId(customer.getId());
            invoice.setCustCode(custCode);
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
				
				sql="SELECT INVM_ID  FROM invoice_master where INVM_NO = ?";
				int invshopId = dynamicShopJdbc.queryForObject(sql, Integer.class,item.getInvNo());
				int invcustId = 0;
	            if(item.getCustCode() != null && !item.getCustCode().equals("")) {
					
					sql="SELECT * FROM CUSTOMER_INFO where CUST_CODE = ?";
					JdbcTemplate customerJdbc = daoConnection.getDynamicDataSource(DaoConnection.CUSTOMER_DB_NAME,
							item.getDbUserName(),item.getDbPassword());
					Customer customer = customerJdbc.query(sql,new CustomerMapper(),item.getCustCode()).get(0);
					dynamicCustJdbc = daoConnection.getDynamicDataSource(customer.getDbName(),
							item.getDbUserName(),item.getDbPassword());
					
					dynamicCustJdbc.update(invSql,0,item.getInvNo(),item.getInvTransId(),item.getInvShopId(),
							item.getInvShopCode(),item.getInvShopName(),item.getInvShopAddress(),item.getInvShopEmail(),item.getInvShopMobile(),
							item.getInvShopGSTIn(),item.getInvCustId(),item.getInvCustName(),item.getInvCustMobile(),item.getInvTotCGST(),
							item.getInvTotSGST(),item.getInvTotIGST(),item.getInvTotDisAmount(),item.getInvTotTaxAmount(),item.getInvTotAmount(),
							item.getInvTotNetPayable(),item.getInvStatus(),item.getInvCoupenId(),item.getInvPaymentMode(),item.getUserName(),
							item.getUserName());
					
					sql="SELECT INVM_ID  FROM invoice_master where INVM_NO = ?";
					invcustId = dynamicCustJdbc.queryForObject(sql, Integer.class,item.getInvNo());
					
				}
	            
	            
	            
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
	            	
	            	if(dynamicCustJdbc != null) {
	            		
	            		dynamicCustJdbc.update(invDetailSql,0,invcustId,itemDetail.getInvDProdId(),
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
		}
		
		return status;
	}
	
	
	public List<MyProduct> getProductList(UserID item){
		List<MyProduct> itemList = null;		
		JdbcTemplate dynamicJdbc = daoConnection.getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		
		String sql="SELECT * FROM cust_order_details where ORD_ORD_ID = ?";
		
		try
		   {
		     itemList=dynamicJdbc.query(sql,new Object[] { item.getId() }, new OrderProductMapper());
		   }catch(Exception e)
		     {
			   log.info("Exception "+e.toString());
		    }finally {
		    	try {
					dynamicJdbc.getDataSource().getConnection().close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		
		return itemList;
	}
	
	public List<MyOrder> getPendingShopOrder(UserID item) {
		List<MyOrder> itemList = null;
		JdbcTemplate dynamicJdbc = daoConnection.getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		String sql="SELECT co.*,cod.ORD_STATUS,cod.ORD_REASON,cod.ORD_PAYMENT_STATUS FROM CUST_ORDER as co,CUST_ORDER_DETAILS as cod "
				+ "WHERE co.ORD_ID = cod.ORD_ORD_ID AND cod.ORD_SHOP_CODE = ? AND (cod.ORD_STATUS = ? or cod.ORD_STATUS = ?)"
				+ " GROUP BY co.ORD_ID ORDER BY co.ORD_DATE DESC LIMIT ? OFFSET ?";
		try {
			itemList = dynamicJdbc.query(sql,new OrderMapper(),item.getDbName(),"pending","Accepted",item.getLimit(),item.getOffset());
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
	    	try {
				dynamicJdbc.getDataSource().getConnection().close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
		
		return itemList;
	}
	
	public List<MyOrder> getShopOrder(UserID item) {
		List<MyOrder> itemList = null;
		JdbcTemplate dynamicJdbc = daoConnection.getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		String sql="SELECT co.*,cod.ORD_STATUS,cod.ORD_REASON,cod.ORD_PAYMENT_STATUS FROM CUST_ORDER as co,CUST_ORDER_DETAILS as cod "
				+ "WHERE co.ORD_ID = cod.ORD_ORD_ID"
				+ " GROUP BY co.ORD_ID ORDER BY co.ORD_DATE DESC LIMIT ? OFFSET ?";
		try {
			itemList = dynamicJdbc.query(sql,new OrderMapper(),item.getLimit(),item.getOffset());
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
	    	try {
				dynamicJdbc.getDataSource().getConnection().close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
		
		return itemList;
	}
	
	public String acceptOrder(SetOrderStatusObject item) {
		String status = "failure";
		JdbcTemplate dynamicJdbc = daoConnection.getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		JdbcTemplate transactionJdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.TRANS_DB_NAME,item.getDbUserName(),item.getDbPassword());
		JdbcTemplate dynamicCustomerJdbc = null;
		String sql="UPDATE CUST_ORDER_DETAILS SET ORD_STATUS = ?,ORD_DELIVER_BY = ? WHERE ORD_ORD_ID = ? AND ORD_SHOP_CODE = ?";
		try {
			dynamicJdbc.update(sql,"Accepted",item.getDeliveryBy(),item.getId(),item.getDbName());
			/*sql="UPDATE SHOP_ORDER_DETAILS SET ORD_STATUS = ? WHERE ORD_ORD_ID =?";
			dynamicJdbc.update(sql,"Accepted",item.getId());
			
		
			sql="UPDATE CUST_ORDER_DETAILS SET ORD_STATUS = ? WHERE ORD_ORD_ID =?";*/
			transactionJdbcTemplate.update(sql,"Accepted",item.getDeliveryBy(),item.getId(),item.getDbName());
			
			String sqlCust="SELECT * FROM CUSTOMER_INFO where CUST_CODE = ?";
			Customer customer = dynamicJdbc.query(sqlCust,new RetCustomerMapper(),item.getCustCode()).get(0);
			dynamicCustomerJdbc = daoConnection.getDynamicDataSource(customer.getDbName(),customer.getDbUserName(),customer.getDbPassword());
			
		
			//sql="UPDATE CUST_ORDER_DETAILS SET ORD_STATUS = ? WHERE ORD_ORD_ID =?";
			dynamicCustomerJdbc.update(sql,"Accepted",item.getDeliveryBy(),item.getId(),item.getDbName());
			
			String statusQuery="SELECT ORD_STATUS FROM CUST_ORDER_DETAILS WHERE ORD_ORD_ID = ?";
			List<Map<String,Object>> statusList = dynamicCustomerJdbc.queryForList(statusQuery,item.getId());
			
			if(isStatusChange(statusList,"Accepted")) {
				statusQuery="UPDATE CUST_ORDER SET ORD_STATUS = ? WHERE ORD_ID = ?";
				dynamicCustomerJdbc.update(statusQuery,"Accepted",item.getId());
				transactionJdbcTemplate.update(statusQuery,"Accepted",item.getId());
				dynamicJdbc.update(statusQuery,"Accepted",item.getId());
			}
			
			status = "success";
			
		}catch(Exception e) {
			e.printStackTrace();
			status = "error";
		}finally {
	    	try {
				dynamicJdbc.getDataSource().getConnection().close();
				transactionJdbcTemplate.getDataSource().getConnection().close();
				dynamicCustomerJdbc.getDataSource().getConnection().close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
		
		return status;
	}
	
	public String declineOrder(SetOrderStatusObject item) {
		String status = "failure";
		JdbcTemplate dynamicJdbc = daoConnection.getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		JdbcTemplate transactionJdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.TRANS_DB_NAME,item.getDbUserName(),item.getDbPassword());
		JdbcTemplate dynamicCustomerJdbc = null;
		String sql="UPDATE CUST_ORDER_DETAILS SET ORD_STATUS = ?,ORD_REASON = ? WHERE ORD_ORD_ID =? AND ORD_SHOP_CODE = ?";
		try {
			dynamicJdbc.update(sql,"Cancelled",item.getReason(),item.getId(),item.getDbName());
			
			/*sql="UPDATE SHOP_ORDER_DETAILS SET ORD_STATUS = ? WHERE ORD_ORD_ID =?";
			dynamicJdbc.update(sql,"Accepted",item.getId());
			
		
			sql="UPDATE CUST_ORDER_DETAILS SET ORD_STATUS = ? WHERE ORD_ORD_ID =?";*/
			transactionJdbcTemplate.update(sql,"Cancelled",item.getReason(),item.getId(),item.getDbName());
			
			String sqlCust="SELECT * FROM CUSTOMER_INFO where CUST_CODE = ?";
			Customer customer = dynamicJdbc.query(sqlCust,new RetCustomerMapper(),item.getCustCode()).get(0);
			dynamicCustomerJdbc = daoConnection.getDynamicDataSource(customer.getDbName(),customer.getDbUserName(),customer.getDbPassword());
			
		
			//sql="UPDATE CUST_ORDER_DETAILS SET ORD_STATUS = ? WHERE ORD_ORD_ID =?";
			dynamicCustomerJdbc.update(sql,"Cancelled",item.getReason(),item.getId(),item.getDbName());
			
			sql="SELECT * FROM cust_order_details where ORD_ORD_ID = ?";
			
			List<ProductQty> productQtyList =  dynamicJdbc.query(sql,new ProductQtyMapper(),item.getId());
			
			for(ProductQty productQty : productQtyList) {
				sql="select PROD_QOH from RET_PRODUCT WHERE PROD_ID = ?";
				int counter = dynamicJdbc.queryForObject(sql,Integer.class,productQty.getProdId());
				counter = counter + productQty.getQty();
				sql="UPDATE RET_PRODUCT SET PROD_QOH = ? WHERE PROD_ID = ?";
				dynamicJdbc.update(sql,productQty.getProdId());
				
				if(productQty.getIsBarCodeAvailable().equals("Y")){
					sql="UPDATE product_barcodes SET SOLD_STATUS = ? WHERE PROD_ID = ? AND PROD_BARCODE = ?";
					dynamicJdbc.update(sql,"N",productQty.getProdId(),productQty.getProdBarCode());
				}
				
			}
			
			String statusQuery="SELECT ORD_STATUS FROM CUST_ORDER_DETAILS WHERE ORD_ORD_ID = ?";
			List<Map<String,Object>> statusList = dynamicCustomerJdbc.queryForList(statusQuery,item.getId());
			
			if(isStatusChange(statusList,"Cancelled")) {
				statusQuery="UPDATE CUST_ORDER SET ORD_STATUS = ? WHERE ORD_ID = ?";
				dynamicCustomerJdbc.update(statusQuery,"Cancelled",item.getId());
				transactionJdbcTemplate.update(statusQuery,"Cancelled",item.getId());
				dynamicJdbc.update(statusQuery,"Cancelled",item.getId());
			}
			
			status = "success";
			
		}catch(Exception e) {
			e.printStackTrace();
			status = "error";
		}finally {
	    	try {
				dynamicJdbc.getDataSource().getConnection().close();
				transactionJdbcTemplate.getDataSource().getConnection().close();
				dynamicCustomerJdbc.getDataSource().getConnection().close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
		
		return status;
	}
	
	public String orderDelivered(SetOrderStatusObject item) {
		String status = "failure";
		JdbcTemplate dynamicJdbc = daoConnection.getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		JdbcTemplate transactionJdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.TRANS_DB_NAME,item.getDbUserName(),item.getDbPassword());
		JdbcTemplate dynamicCustomerJdbc = null;
		String sql="UPDATE CUST_ORDER_DETAILS SET ORD_STATUS = ? WHERE ORD_ORD_ID =? AND ORD_SHOP_CODE = ?";
		try {
			dynamicJdbc.update(sql,"Delivered",item.getId(),item.getDbName());
			/*sql="UPDATE SHOP_ORDER_DETAILS SET ORD_STATUS = ? WHERE ORD_ORD_ID =?";
			dynamicJdbc.update(sql,"Delivered",item.getId());
			
		
			sql="UPDATE CUST_ORDER_DETAILS SET ORD_STATUS = ? WHERE ORD_ORD_ID =?";*/
			transactionJdbcTemplate.update(sql,"Delivered",item.getId(),item.getDbName());
			
			if(item.getCustCode() != null && !item.getCustCode().equals("")) {
				String sqlCust="SELECT * FROM CUSTOMER_INFO where CUST_CODE = ?";
				Customer customer = dynamicJdbc.query(sqlCust,new RetCustomerMapper(),item.getCustCode()).get(0);
				dynamicCustomerJdbc = daoConnection.getDynamicDataSource(customer.getDbName(),customer.getDbUserName(),customer.getDbPassword());
				dynamicCustomerJdbc.update(sql,"Delivered",item.getId(),item.getDbName());
			}
			
			
			
			String statusQuery="SELECT ORD_STATUS FROM CUST_ORDER_DETAILS WHERE ORD_ORD_ID = ?";
			List<Map<String,Object>> statusList = dynamicCustomerJdbc.queryForList(statusQuery,item.getId());
			
			if(isStatusChange(statusList,"Delivered")) {
				statusQuery="UPDATE CUST_ORDER SET ORD_STATUS = ? WHERE ORD_ID = ?";
				if(item.getCustCode() != null && !item.getCustCode().equals("")) {
					dynamicCustomerJdbc.update(statusQuery,"Delivered",item.getId());
				}
				transactionJdbcTemplate.update(statusQuery,"Delivered",item.getId());
				dynamicJdbc.update(statusQuery,"Delivered",item.getId());
			}
			
			status = "success";
			
		}catch(Exception e) {
			e.printStackTrace();
			status = "error";
		}finally {
	    	try {
				dynamicJdbc.getDataSource().getConnection().close();
				transactionJdbcTemplate.getDataSource().getConnection().close();
				if(dynamicCustomerJdbc != null)
				dynamicCustomerJdbc.getDataSource().getConnection().close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
		
		return status;
	}
	
	private boolean isStatusChange(List<Map<String,Object>> statusList , String status) {
		boolean change = false;
		String ordStatus = null;
		int statusValue = 0;
		int minStatusValue = 6;
		for(Map<String,Object> map : statusList) { 
			ordStatus = (String)map.get("ORD_STATUS");
			statusValue = getStatusValue(ordStatus);
			if(minStatusValue >= statusValue) {
				minStatusValue = statusValue;
			}
		}
		
		if(minStatusValue >= getStatusValue(status)) {
			change = true;
		}else {
			change = false;
		}
		
		return change;
	}
	
	private int getStatusValue(String status) {
		int statusValue = 0;
		if(status.equals("pending")) {
			statusValue = 1;
		}else if(status.equals("Cancelled")) {
			statusValue = 5;
		}else if(status.equals("Accepted")) {
			statusValue = 2;
		}else if(status.equals("Delivered")) {
			statusValue = 3;
		}
		
		return statusValue;
	}
	
}
