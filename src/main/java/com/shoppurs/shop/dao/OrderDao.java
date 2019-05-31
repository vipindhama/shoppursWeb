package com.shoppurs.shop.dao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
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
import com.shoppurs.shop.model.MyPayment;
import com.shoppurs.shop.model.MyProduct;
import com.shoppurs.shop.model.ProductQty;
import com.shoppurs.shop.model.SetOrderStatusObject;
import com.shoppurs.shop.model.UserID;

public class OrderDao {
	
	private static final Logger log = LoggerFactory.getLogger(ShoppursApiController.class);
	
	
	
}
