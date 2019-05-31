package com.shoppurs.shop.dao;

import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import com.shoppurs.connection.DaoConnection;
import com.shoppurs.shop.controller.ShoppursApiController;
import com.shoppurs.shop.mapper.CategoryMapper;
import com.shoppurs.shop.mapper.CountryMapper;
import com.shoppurs.shop.mapper.RetCustomerMapper;
import com.shoppurs.shop.mapper.MyUserMapper;
import com.shoppurs.shop.mapper.ProductBarCodeMapper;
import com.shoppurs.shop.mapper.ProductMapper;
import com.shoppurs.shop.mapper.SimpleItemMapper;
import com.shoppurs.shop.mapper.SubCatMapper;
import com.shoppurs.shop.model.Category;
import com.shoppurs.shop.model.Country;
import com.shoppurs.shop.model.Customer;
import com.shoppurs.shop.model.MyBank;
import com.shoppurs.shop.model.MyDataSource;
import com.shoppurs.shop.model.MyProduct;
import com.shoppurs.shop.model.MyUser;
import com.shoppurs.shop.model.ProductBarcode;
import com.shoppurs.shop.model.RetailerCategory;
import com.shoppurs.shop.model.SimpleItem;
import com.shoppurs.shop.model.SubCategory;
import com.shoppurs.shop.model.UserID;
import com.shoppurs.shop.model.UserLogin;

public class ShoppursDao {
	
	

}
