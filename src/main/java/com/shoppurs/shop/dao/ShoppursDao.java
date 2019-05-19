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
	
	private static final Logger log = LoggerFactory.getLogger(ShoppursApiController.class);
	
	@Autowired
    private MyDataSource myDataSource;
	
	@Autowired
	@Qualifier("master-database")
    private JdbcTemplate jdbcTemplate;
	
	@Autowired
   	@Qualifier("auth-database")
    private JdbcTemplate authJdbcTemplate;
	
	@Autowired
   	@Qualifier("shop-database")
    private JdbcTemplate shopJdbcTemplate;
	
	@Autowired
   	@Qualifier("product-database")
    private JdbcTemplate productJdbcTemplate;
	
	//private ScriptUtils scriptUtils;
	
		
	

	public void testRunScript() {
		/*try {
			ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(), 
					new org.springframework.core.io.support.EncodedResource(
							new org.springframework.core.io.ClassPathResource("test_procedure.sql"), 
							StandardCharsets.UTF_8));
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
		}*/
		
		String sql ="DELIMITER $$\r\n" + 
				"CREATE DEFINER=`shoppurs_master`@`%` PROCEDURE `cust_c15`.`test_procedure`()\r\n" + 
				"BEGIN\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"END$$\r\n" + 
				"\r\n" + 
				"DELIMITER ;";
		
		//jdbcTemplate.query(sql,null,null);
		jdbcTemplate.execute(sql);
	}

	
	public Customer getCustomerDetails(String email) {
	
		String sql="select * from RETAILER_MASTER where RET_EMAIL_ID=?";
		try
		   {
		     List<Customer> customerList=jdbcTemplate.query(sql, new RetCustomerMapper(),email);
		       if(customerList.size() > 0) {
			      return customerList.get(0);
		       }else {
		    	   log.info("Customer size is 0");
		    	   return null;
		       }
		   }catch(Exception e)
		     {
			   log.info("Exception "+e.toString());
			     return null;
		    }
	}
	
	

public HashMap<String,Object> syncData(UserID item) {
	List<Object> itemList = new ArrayList();
	HashMap<String,Object> hashMap = new HashMap();
	List<Category> catItemList = null;
	List<SubCategory> subCatItemList = null;
	List<MyProduct> productItemList = null;
	List<ProductBarcode> productBarCodeItemList = null;
	String sql="select * from RET_CATEGORY WHERE DEL_STATUS = 'N'";
	String sql2="select * from RET_SUB_CATEGORY WHERE DEL_STATUS = 'N'";
	String sql3="select * from RET_PRODUCT WHERE DEL_STATUS = 'N'";
	String sql4="select * from PRODUCT_BARCODES WHERE SOLD_STATUS ='N'";
	JdbcTemplate dynamicJdbc = getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
	try
	   {
		 catItemList=dynamicJdbc.query(sql, new CategoryMapper());
	     subCatItemList=dynamicJdbc.query(sql2, new SubCatMapper());
	     productItemList=dynamicJdbc.query(sql3, new ProductMapper());
	     productBarCodeItemList=dynamicJdbc.query(sql4, new ProductBarCodeMapper());
	     hashMap.put("categories", catItemList);
	     hashMap.put("sub_categories", subCatItemList);
	     hashMap.put("products", productItemList);
	     hashMap.put("products_barcodes", productBarCodeItemList);
	   }catch(Exception e)
	     {
		   log.info("Exception "+e.toString());
		   //  return null;
	    }
	
	return hashMap;
}
	
	public List<Country> getCountryList() {
		
		String sql="select * from country_master where NAME = 'India'";
		try
		   {
		     List<Country> countryList=jdbcTemplate.query(sql, new CountryMapper());
		       return countryList;
		   }catch(Exception e)
		     {
			   log.info("Exception "+e.toString());
			     return null;
		    }
	}
	
public List<SimpleItem> getStateList(int countryId) {
		
		String sql="select * from states_master where country_id = ?";
		try
		   {
		     List<SimpleItem> itemList=jdbcTemplate.query(sql, new SimpleItemMapper(),countryId);
		       return itemList;
		   }catch(Exception e)
		     {
			   log.info("Exception "+e.toString());
			     return null;
		    }
	}

public List<SimpleItem> getCityList(int stateId) {
	
	String sql="select * from city_master where state_id = ?";
	try
	   {
	     List<SimpleItem> itemList=jdbcTemplate.query(sql, new SimpleItemMapper(),stateId);
	       return itemList;
	   }catch(Exception e)
	     {
		   log.info("Exception "+e.toString());
		     return null;
	    }
}
	
	private String generateRandom(int count) {
    	String alphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    	StringBuilder builder = new StringBuilder();
    	while(count-- != 0) {
    		int character = (int)(Math.random() * alphaNumericString.length());
    		builder.append(alphaNumericString.charAt(character));
    	}
    	
    	return builder.toString();
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
