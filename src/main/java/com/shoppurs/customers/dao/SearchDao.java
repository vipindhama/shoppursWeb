package com.shoppurs.customers.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.shoppurs.customers.controller.CustomerApiController;
import com.shoppurs.customers.mapper.MyShopMapper;
import com.shoppurs.customers.mapper.ProductMapper;
import com.shoppurs.customers.model.MyProduct;
import com.shoppurs.customers.model.MyShop;
import com.shoppurs.customers.model.UserID;
import com.shoppurs.shop.mapper.ShopCustomerMapper;
import com.shoppurs.shop.model.Customer;
import com.shoppurs.shop.model.MyDataSource;
import com.shoppurs.shop.model.requestModel.SearchCustomerReq;
import com.shoppurs.shop.model.requestModel.SearchProductReq;
import com.shoppurs.shop.model.requestModel.SearchShopReq;

public class SearchDao {
	
	private static final Logger log = LoggerFactory.getLogger(CustomerApiController.class);
	
	@Autowired
    private MyDataSource myDataSource;
	
	@Autowired
   	@Qualifier("shop-database")
    private JdbcTemplate shopJdbcTemplate;
	
	public List<MyShop> searchShops(SearchShopReq item){
		List<MyShop> shopList = null;
		String query = "%"+item.getQuery()+"%";
		String sql = "SELECT * FROM retailer_info where ret_name like ? or ret_shop_name like ? or ret_address like ? "
				+ "or ret_state like ? or ret_city like ? LIMIT ? OFFSET ?"; 
		try {
			shopList =shopJdbcTemplate.query(sql, new MyShopMapper(),query,query,query,query,query,item.getLimit(),item.getOffset());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return shopList;
	}
	
	public List<MyProduct> searchProductsByShop(SearchProductReq item){
		String sql = "SELECT * FROM retailer_info WHERE `ret_code` = ?";
	    List<MyShop> shopListDetails =shopJdbcTemplate.query(sql, new MyShopMapper(),item.getShopCode());
	    MyShop myShop = shopListDetails.get(0);
	    JdbcTemplate dynamicShopJdbc = getDynamicDataSource(myShop.getDbname(),myShop.getDbuser(),myShop.getDbpassword());
	    
		List<MyProduct> productList = null;
		String query = "%"+item.getQuery()+"%";
		sql = "SELECT rp.*,pb.prod_barcode FROM ret_product as rp,product_barcodes as pb where rp.prod_name like ? or rp.prod_code like ? "
				+ "or pb.prod_barcode like ? and rp.prod_code = pb.prod_code GROUP BY rp.prod_code"
				+ " LIMIT ? OFFSET ?"; 
		try {
			productList =dynamicShopJdbc.query(sql, new ProductMapper(),query,query,query,item.getLimit(),item.getOffset());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return productList;
	}
	
	public List<Customer> searchCustomer(SearchCustomerReq item){
		List<Customer> itemList = null;
		String query = "%"+item.getQuery()+"%";
		String sql="SELECT * FROM customer_info where cust_name like ? or cust_mobileno like ? or cust_address like ? "
				+ "or cust_city like ? LIMIT ? OFFSET ?";
		JdbcTemplate dynamicJdbc = getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		try
		   {
		     itemList=dynamicJdbc.query(sql, new ShopCustomerMapper(),query,query,query,query,item.getLimit(),item.getOffset());
		   }catch(Exception e)
		     {
			   log.info("Exception "+e.toString());
		    }
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
