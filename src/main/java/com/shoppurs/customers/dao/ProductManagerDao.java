package com.shoppurs.customers.dao;

import java.util.List;

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
import com.shoppurs.shop.mapper.BarCodeMapper;
import com.shoppurs.shop.mapper.ProductMapper;
import com.shoppurs.shop.model.Barcode;
import com.shoppurs.shop.model.MyDataSource;
import com.shoppurs.shop.model.MyProduct;
import com.shoppurs.shop.model.Ratings;
import com.shoppurs.shop.model.requestModel.RetProductReq;

public class ProductManagerDao {
	
	private static final Logger log = LoggerFactory.getLogger(ShoppursApiController.class);
	
	@Autowired
    private MyDataSource myDataSource;
	
	@Autowired
	@Qualifier("product-database")
	private JdbcTemplate productJdbcTemplate;
	
	@Autowired
   	@Qualifier("shop-database")
    private JdbcTemplate shopJdbcTemplate;
	
	public String setRatings(Ratings item) {
		String status = "faliure";
		//JdbcTemplate dynamicJdbc = getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		
		try {
			
			/*String sql="SELECT COUNT(*) FROM product_master where PROD_BARCODE = ?";
			int count = productJdbcTemplate.queryForObject(sql,Integer.class,item.getProdCode());
			if(count > 0) {
				sql="SELECT COUNT(*) FROM product_ratings where PROD_CODE = ? AND CUST_CODE = ?";
				count = productJdbcTemplate.queryForObject(sql,Integer.class,item.getProdCode(),item.getCustCode());
				if(count == 0) {
					sql="insert into product_ratings (`PROD_CODE`,`CUST_CODE`,`RATINGS`)" + 
							" values (?,?,?)";
					productJdbcTemplate.update(sql,item.getProdCode(),item.getCustCode(),item.getRatings());
				}else {
					sql = "UPDATE product_ratings SET " + 
							"`RATINGS` = ? WHERE PROD_CODE = ? AND CUST_CODE = ?"; 
					productJdbcTemplate.update(sql,item.getRatings(),item.getProdCode(),item.getCustCode());	
				}
			}*/
			
			
			String sql = "SELECT * FROM retailer_info WHERE `ret_code` = ?";
		    List<MyShop> shopListDetails =shopJdbcTemplate.query(sql, new MyShopMapper(),item.getShopCode());
		    MyShop myShop = shopListDetails.get(0);
		    JdbcTemplate dynamicShopJdbc = getDynamicDataSource(myShop.getDbname(),myShop.getDbuser(),myShop.getDbpassword());
		    
			sql="SELECT COUNT(*) FROM product_ratings where PROD_CODE = ? AND CUST_CODE = ?";
		    int count = dynamicShopJdbc.queryForObject(sql,Integer.class,item.getProdCode(),item.getCustCode());
		    if(count == 0) {
				sql="insert into product_ratings (`PROD_CODE`,`CUST_CODE`,`RATINGS`)" + 
						" values (?,?,?)";
				dynamicShopJdbc.update(sql,item.getProdCode(),item.getCustCode(),item.getRatings());
			}else {
				sql = "UPDATE product_ratings SET " + 
						"`RATINGS` = ? WHERE PROD_CODE = ? AND CUST_CODE = ?"; 
				dynamicShopJdbc.update(sql,item.getRatings(),item.getProdCode(),item.getCustCode());	
			}
						
			status = "success";
		}catch(Exception e) {
			status = "error";
			e.printStackTrace();
		}
		return status;
	}
	
	public List<MyProduct> getRetailerProductList(RetProductReq item) {
		
	    JdbcTemplate dynamicShopJdbc = getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
			
			String sql="select * from RET_PRODUCT WHERE PROD_SUB_CAT_ID IN (?)";
			String barSql="select PROD_BARCODE from PRODUCT_BARCODES WHERE PROD_PROD_ID = ? AND SOLD_STATUS ='N'";
			try
			   {
			     List<MyProduct> itemList=dynamicShopJdbc.query(sql, new ProductMapper(), item.getSubCatId());
			       if(itemList.size() > 0) {
			    	   for(MyProduct myProduct : itemList) {
			    		   if(myProduct.getIsBarcodeAvailable().equals("Y")) {
			    			   List<Barcode> barcodeList =  dynamicShopJdbc.query(barSql,new BarCodeMapper(),myProduct.getProdId());
			    			   myProduct.setBarcodeList(barcodeList);
			    		   }
			    	   }
				      return itemList;
			       }else {
			    	   log.info("ProductList size is 0");
			    	   return null;
			       }
			   }catch(Exception e)
			     {
				   log.info("Exception "+e.toString());
				     return null;
			    }
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
