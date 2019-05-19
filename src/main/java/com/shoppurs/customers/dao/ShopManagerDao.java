package com.shoppurs.customers.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.shoppurs.customers.mapper.FavShopMapper;
import com.shoppurs.customers.mapper.MyShopMapper;
import com.shoppurs.customers.model.MyDataSource;
import com.shoppurs.customers.model.MyShop;
import com.shoppurs.customers.model.UserID;
import com.shoppurs.shop.model.Favourite;
import com.shoppurs.shop.model.Ratings;
import com.shoppurs.shop.model.requestModel.ShopCode;

public class ShopManagerDao {
	
	@Autowired
    private MyDataSource dynamicDataSource;
	
	@Autowired
	@Qualifier("customer-database")
	private JdbcTemplate customerJdbcTemplate;
	
	@Autowired
   	@Qualifier("shop-database")
    private JdbcTemplate shopJdbcTemplate;
	
	@Autowired
   	@Qualifier("transaction-database")
    private JdbcTemplate transactionJdbcTemplate;
	
	public List<Map<Integer,String>> getFavouriteShops(UserID item) {
		List<Map<Integer,String>> favShopList = null;
		JdbcTemplate dynamicJdbc = getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		try
		{
			String shopquery = "SELECT * FROM favourite_shops";
			favShopList =(List<Map<Integer, String>>) dynamicJdbc.query(shopquery, new FavShopMapper());
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return favShopList;
	}
	
	public float getShopRatings(Ratings item) {
		float ratings = 0.0f;
		String sql = "SELECT * FROM RETAILER_INFO WHERE `ret_code` = ?";
		JdbcTemplate shopJdbc = getDynamicDataSource("shoppurs_shop",item.getDbUserName(),item.getDbPassword());
	    List<MyShop> shopListDetails =shopJdbc.query(sql, new MyShopMapper(),item.getShopCode());
	    MyShop myShop = shopListDetails.get(0);
	    JdbcTemplate dynamicShopJdbc = getDynamicDataSource(myShop.getDbname(),myShop.getDbuser(),myShop.getDbpassword());
		try
		{
			sql = "SELECT avg(co.ORD_RATINGS_BY_CUST) as RATINGS FROM cust_order as co,cust_order_details as cod"
					+ " WHERE co.ORD_ID = cod.ORD_ORD_ID AND cod.ORD_SHOP_CODE = ?";
			ratings =dynamicShopJdbc.queryForObject(sql, Float.class,item.getShopCode());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return ratings;
	}
	
	public String setFavourite(Favourite item) {
		String status = "faliure";
		JdbcTemplate dynamicJdbc = getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		try {
			String sql="SELECT COUNT(*) FROM favourite_shops where SHOP_CODE = ?";
			int count = dynamicJdbc.queryForObject(sql,Integer.class,item.getCode());
			if(item.getFavStatus().equals("Y")) {
				if(count == 0) {
					sql="insert into favourite_shops (`SHOP_CODE`)" + 
							" values (?)";
					dynamicJdbc.update(sql,item.getCode());
					status = "success";
				}else {
					status = "0";
				}
			}else if(item.getFavStatus().equals("N")) {
				if(count == 0) {
					status = "1";
				}else {
					sql="delete from favourite_shops where shop_code = ?";
					dynamicJdbc.update(sql,item.getCode());
					status = "success";
				}
			}
			
		}catch(Exception e) {
			status = "error";
			e.printStackTrace();
		}
		
		return status;
	}
	
	
	public String setRatings(Ratings item) {
		String status = "faliure";
		JdbcTemplate dynamicJdbc = getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		JdbcTemplate transJdbc = getDynamicDataSource("shoppurs_trans",item.getDbUserName(),item.getDbPassword());
		JdbcTemplate shopJdbc = getDynamicDataSource("shoppurs_shop",item.getDbUserName(),item.getDbPassword());
		List<ShopCode> shopCodeList = null;
		try {
			
			String sql="SELECT COUNT(*) FROM CUST_ORDER where ORD_ID = ?";
			int count = transJdbc.queryForObject(sql,Integer.class,item.getId());
			if(count == 0) {
				status = "No Records exist";
			}else {
				sql = "UPDATE CUST_ORDER SET " + 
						"`ORD_RATINGS_BY_CUST` = ?,`ORD_CUST_REMARKS` = ? WHERE ORD_ID = ?"; 
				transJdbc.update(sql,item.getRatings(),item.getRemarks(),item.getId());
				
				shopCodeList = item.getShopCodeList();
				List<MyShop> shopListDetails = null;
				for(ShopCode shopCode: shopCodeList) {
					sql = "SELECT * FROM RETAILER_INFO WHERE `ret_code` = ?";
					shopListDetails =shopJdbc.query(sql, new MyShopMapper(),shopCode.getShopCode());
				    MyShop myShop = shopListDetails.get(0);
				    JdbcTemplate dynamicShopJdbc = getDynamicDataSource(myShop.getDbname(),myShop.getDbuser(),myShop.getDbpassword());
				    sql = "UPDATE CUST_ORDER SET " + 
							"`ORD_RATINGS_BY_CUST` = ?,`ORD_CUST_REMARKS` = ? WHERE ORD_ID = ?"; 
				    dynamicShopJdbc.update(sql,item.getRatings(),item.getRemarks(),item.getId());
				} 
			    dynamicJdbc.update(sql,item.getRatings(),item.getRemarks(),item.getId());
			    status = "success";
			}					
			
		}catch(Exception e) {
			status = "error";
			e.printStackTrace();
		}
		return status;
	}
	
	private JdbcTemplate getDynamicDataSource(String dbName,String dbUserName,String dbPassword) {
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
