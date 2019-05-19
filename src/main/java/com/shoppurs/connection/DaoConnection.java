package com.shoppurs.connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.shoppurs.customers.model.MyDataSource;

public class DaoConnection {
	
	public static final String SHOP_DB_NAME = "shoppurs_shop";
	public static final String CUSTOMER_DB_NAME = "shoppurs_customer";
	public static final String AUTH_DB_NAME = "shoppurs_auth";
	public static final String TRANS_DB_NAME = "shoppurs_trans";
	public static final String SHOPPURS_DB_NAME = "shoppurs_master";
	public static final String PRODUCT_DB_NAME = "shoppurs_product";
	public static final String OFFERS_DB_NAME = "shoppurs_offer";
	
	public static final String SHOPPURS_SHOP_DB_NAME = "SHP1";
	public static final String SHOPPURS_SHOP_CODE = "SHP1";
	
	@Autowired
    private MyDataSource dynamicDataSource;
	
	
	public JdbcTemplate getDynamicDataSource(String dbName,String dbUserName,String dbPassword) {
		dynamicDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dynamicDataSource.setUrl("jdbc:mysql://49.50.77.154:3306/"+dbName);
		dynamicDataSource.setUserName(dbUserName);
		dynamicDataSource.setPassword(dbPassword);
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName(dynamicDataSource.getDriverClassName());
	    dataSource.setUrl(dynamicDataSource.getUrl());
	    dataSource.setUsername(dynamicDataSource.getUserName());
	    dataSource.setPassword(dynamicDataSource.getPassword());
	    return new JdbcTemplate(dataSource);
	}

}
