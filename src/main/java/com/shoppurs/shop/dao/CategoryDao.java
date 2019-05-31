package com.shoppurs.shop.dao;

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

import com.shoppurs.connection.DaoConnection;
import com.shoppurs.shop.controller.ShoppursApiController;
import com.shoppurs.shop.mapper.CategoryMapper;
import com.shoppurs.shop.mapper.SubCatMapper;
import com.shoppurs.shop.model.Category;
import com.shoppurs.shop.model.MyDataSource;
import com.shoppurs.shop.model.RetailerCategory;
import com.shoppurs.shop.model.SubCategory;
import com.shoppurs.shop.model.UserID;
import com.shoppurs.shop.model.requestModel.CategoryDetailReq;
import com.shoppurs.shop.model.requestModel.DelCategory;
import com.shoppurs.utilities.Constants;

public class CategoryDao {
	
private static final Logger log = LoggerFactory.getLogger(ShoppursApiController.class);


    

}
