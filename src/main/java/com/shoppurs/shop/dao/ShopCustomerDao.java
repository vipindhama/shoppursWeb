package com.shoppurs.shop.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.shoppurs.connection.DaoConnection;
import com.shoppurs.customers.controller.CustomerApiController;
import com.shoppurs.customers.mapper.OrderMapper;
import com.shoppurs.customers.mapper.UserMapper;
import com.shoppurs.customers.model.MyDataSource;
import com.shoppurs.customers.model.MyOrder;
import com.shoppurs.customers.model.MyUser;
import com.shoppurs.customers.model.UserID;
import com.shoppurs.shop.mapper.RetCustomerMapper;
import com.shoppurs.shop.mapper.CustomerSaleMapper;
import com.shoppurs.shop.mapper.ProductSaleMapper;
import com.shoppurs.shop.mapper.ShopCustomerMapper;
import com.shoppurs.shop.model.Customer;
import com.shoppurs.shop.model.CustomerSale;
import com.shoppurs.shop.model.Favourite;
import com.shoppurs.shop.model.ProductSaleObject;
import com.shoppurs.shop.model.Ratings;

public class ShopCustomerDao {
	
private static final Logger log = LoggerFactory.getLogger(CustomerApiController.class);
	
	

}
