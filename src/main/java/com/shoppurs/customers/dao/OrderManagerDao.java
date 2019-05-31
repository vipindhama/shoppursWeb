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
	
	
}
