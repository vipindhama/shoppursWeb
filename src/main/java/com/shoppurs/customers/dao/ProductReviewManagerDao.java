package com.shoppurs.customers.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.shoppurs.customers.mapper.CartMapper;
import com.shoppurs.customers.mapper.ReviewMapper;
import com.shoppurs.customers.model.GetReviewObject;
import com.shoppurs.customers.model.MyCart;
import com.shoppurs.customers.model.ProductReview;
import com.shoppurs.customers.model.UserID;
import com.shoppurs.shop.model.MyDataSource;

public class ProductReviewManagerDao {
	
	@Autowired
    private MyDataSource myDataSource;
	
	public String addProductReview(ProductReview item) {
		String status = "";
		
		String sql = "INSERT INTO product_review " + 
				"(`SHOP_ID`,`PROD_ID`,`CUST_ID`,`CUST_NAME`,`REVIEW_MESSAGE`,`RATING`," + 
				"`CREATED_DATE`,`UPDATED_DATE`)" + 
				"VALUES (?,?,?,?,?,?,now(),now());";
		
		JdbcTemplate dynamicJdbc = getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		try {
			dynamicJdbc.update(sql,item.getShopId(),item.getProdId(),item.getCustomerId(),
					item.getCustomerName(),item.getReviewMessage(),item.getRating());
			status = "success";
		}catch(Exception e) {
			status = "There is some problem in adding product review...";
			e.printStackTrace();
		}
		return status;
	}
	
	public List<ProductReview> getProductReview(GetReviewObject item) {
		List<ProductReview> itemList = null;
		JdbcTemplate dynamicJdbc = getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		String shopDbName = "shop_"+item.getShopId();
		String sql = "SELECT * FROM "+shopDbName+".PRODUCT_REVIEW WHERE PROD_ID = ?  LIMIT ? OFFSET ?";
		itemList = dynamicJdbc.query(sql,new Object[] { item.getProdId(),item.getLimit(),item.getOffset() }, new ReviewMapper());
		return itemList;
	}	
	
	
	private JdbcTemplate getDynamicDataSource(String dbName,String dbUserName,String dbPassword) {
		myDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		myDataSource.setUrl("jdbc:mysql://49.50.77.154:3306/"+dbName);
		//myDataSource.setUrl("jdbc:mysql://localhost:3306/"+dbName);
		myDataSource.setUserName(dbUserName);
		myDataSource.setPassword(dbPassword);
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName(myDataSource.getDriverClassName());
	    dataSource.setUrl(myDataSource.getUrl());
	    dataSource.setUsername(myDataSource.getUserName());
	    dataSource.setPassword(myDataSource.getPassword());
	    return new JdbcTemplate(dataSource);
	}

}
