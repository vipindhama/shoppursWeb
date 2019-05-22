package com.shoppurs.customers.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.shoppurs.connection.DaoConnection;
import com.shoppurs.customers.mapper.CartMapper;
import com.shoppurs.customers.model.MyCart;
import com.shoppurs.customers.model.MyProduct;
import com.shoppurs.customers.model.UserID;
import com.shoppurs.shop.controller.ShoppursApiController;
import com.shoppurs.shop.model.MyDataSource;

public class CartManagerDao {
	
private static final Logger log = LoggerFactory.getLogger(ShoppursApiController.class);
	
	@Autowired
    private MyDataSource myDataSource;
	
	
	public String addToCart(MyCart item) {
		String status = "";
		String sql = "INSERT INTO CUST_CART " + 
				"(`CART_SHOP_CODE`,`CART_PROD_CODE`,`CART_PROD_QTY`, " + 
				"`CREATED_DATE`,`UPDATED_DATE`) " + 
				"VALUES (?,?,?,now(),now());";
		
		String sql1 = "SELECT COUNT(*) as counter from CUST_CART WHERE `CART_SHOP_CODE` = ? AND `CART_PROD_CODE` = ?";
		
		JdbcTemplate dynamicJdbc = getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		
		try {
			int counter = dynamicJdbc.queryForObject(sql1, new Object[] { item.getShopCode(),item.getProdCode() }, Integer.class);
			if(counter > 0) {
				status = "Product is already added  to cart...";
				return status;
			}
		}catch(Exception e) {
			status = "There is some problem in adding product to cart...";
			e.printStackTrace();
		    return status;
		}
		
		try {
			dynamicJdbc.update(sql,item.getShopCode(),item.getProdCode(),item.getProdQty());
			status = "success";
		}catch(Exception e) {
			status = "There is some problem in adding product to cart...";
			e.printStackTrace();
		}
		
		return status;
	}
	
	public String updateCart(MyCart item) {
		String status = "";
		String sql = "UPDATE CUST_CART SET " + 
				"`CART_PROD_QTY` = ?, " + 
				"`UPDATED_DATE` = now() WHERE `CART_SHOP_CODE` = ? AND `CART_PROD_CODE` = ?"; 
		JdbcTemplate dynamicJdbc = getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		try {
			dynamicJdbc.update(sql,item.getProdQty(),item.getShopCode(),item.getProdCode());
			status = "success";
		}catch(Exception e) {
			status = "There is some problem in updating product...";
			e.printStackTrace();
		}
		
		return status;
	}
	
	public String removeProductFromCart(MyCart item) {
		String status = "";
		String sql = "Delete from CUST_CART " + 
				"WHERE `CART_SHOP_CODE` = ? AND `CART_PROD_CODE` = ?";
		JdbcTemplate dynamicJdbc = getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		try {
			dynamicJdbc.update(sql,item.getShopCode(),item.getProdCode());
			status = "success";
		}catch(Exception e) {
			status = "There is some problem in removing product...";
			e.printStackTrace();
		}
		
		return status;
	}
	
	public List<MyCart> getCartData(UserID item) {
		List<MyCart> myCartProductList = null;
		List<MyCart> tempCartProductList = null;
		JdbcTemplate dynamicJdbc = getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		
		String sql = "SELECT COUNT(CART_SHOP_CODE) as counter FROM cust_cart;";
		int counter = dynamicJdbc.queryForObject(sql, null, Integer.class);
		
		if(counter > 0) {
			sql = "SELECT DISTINCT CART_SHOP_CODE FROM cust_cart;";
			String shopDbname = "";
			myCartProductList = new ArrayList();
			List<Map<String,Object>> shopIdList = dynamicJdbc.queryForList(sql);
			log.info(shopIdList.toString());
			for(Map<String,Object> map : shopIdList) { 
				shopDbname = (String)map.get("CART_SHOP_CODE");
				sql = "SELECT cc.CART_SHOP_CODE,cc.CART_PROD_QTY,rp.*,pb.prod_barcode FROM cust_cart as cc, "
						+ ""+shopDbname+".ret_product as rp,"+shopDbname+".product_barcodes as pb "
								+ "where cc.CART_PROD_CODE = pb.PROD_CODE and pb.prod_code = rp.prod_code and cc.CART_SHOP_CODE = ?"
								+ " Group by cc.CART_PROD_CODE";
				tempCartProductList = dynamicJdbc.query(sql, new CartMapper(),shopDbname);
				for(MyCart myCart : tempCartProductList) {
					myCartProductList.add(myCart);	
				}
			}
			
		}else {
			myCartProductList = new ArrayList<>();
		}
		
		return myCartProductList;
	}
	
	private JdbcTemplate getDynamicDataSource(String dbName,String dbUserName,String dbPassword) {
		myDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		myDataSource.setUrl("jdbc:mysql://"+DaoConnection.BASE_URL+":3306/"+dbName);
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
