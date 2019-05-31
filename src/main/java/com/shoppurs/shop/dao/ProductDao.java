package com.shoppurs.shop.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.shoppurs.connection.DaoConnection;
import com.shoppurs.customers.mapper.MyShopMapper;
import com.shoppurs.customers.model.MyShop;
import com.shoppurs.shop.controller.ShoppursApiController;
import com.shoppurs.shop.mapper.ProductBarCodeMapper;
import com.shoppurs.shop.mapper.ProductMapper;
import com.shoppurs.shop.mapper.ProductRatingMapper;
import com.shoppurs.shop.mapper.ProductSaleMapper;
import com.shoppurs.shop.mapper.BarCodeMapper;
import com.shoppurs.shop.model.Barcode;
import com.shoppurs.shop.model.MyDataSource;
import com.shoppurs.shop.model.MyProduct;
import com.shoppurs.shop.model.ProductBarcode;
import com.shoppurs.shop.model.ProductRating;
import com.shoppurs.shop.model.ProductSaleObject;
import com.shoppurs.shop.model.UserID;
import com.shoppurs.shop.model.requestModel.CategoryDetailReq;
import com.shoppurs.shop.model.requestModel.DelCategory;
import com.shoppurs.shop.model.requestModel.DelProductReq;
import com.shoppurs.utilities.Constants;

public class ProductDao {
	
	private static final Logger log = LoggerFactory.getLogger(ShoppursApiController.class);
	
	
	

}
