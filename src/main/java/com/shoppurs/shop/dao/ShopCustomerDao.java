package com.shoppurs.shop.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.shoppurs.connection.DaoConnection;
import com.shoppurs.customers.controller.CustomerApiController;
import com.shoppurs.customers.mapper.OrderMapper;
import com.shoppurs.customers.mapper.UserMapper;
import com.shoppurs.customers.model.MyDataSource;
import com.shoppurs.customers.model.MyOrder;
import com.shoppurs.customers.model.MyUser;
import com.shoppurs.customers.model.UserID;
import com.shoppurs.shop.mapper.RetCustomerMapper;
import com.shoppurs.shop.mapper.CustomerSaleMapper;
import com.shoppurs.shop.mapper.ProductSaleMapper;
import com.shoppurs.shop.mapper.ShopCustomerMapper;
import com.shoppurs.shop.model.Customer;
import com.shoppurs.shop.model.CustomerSale;
import com.shoppurs.shop.model.Favourite;
import com.shoppurs.shop.model.ProductSaleObject;
import com.shoppurs.shop.model.Ratings;

public class ShopCustomerDao {
	
private static final Logger log = LoggerFactory.getLogger(CustomerApiController.class);
	
	@Autowired
    private MyDataSource dynamicDataSource;
	
	@Autowired
	@Qualifier("customer-database")
	private JdbcTemplate customerJdbcTemplate;
	
	
	public List<Customer> getCustomerList(UserID item){
		List<Customer> itemList = null;
		String sql="SELECT * FROM customer_info";
		JdbcTemplate dynamicJdbc = getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		try
		   {
		     itemList=dynamicJdbc.query(sql, new ShopCustomerMapper());
		   }catch(Exception e)
		     {
			   log.info("Exception "+e.toString());
		    }
		return itemList;
	}
	
	public Customer isCustomerRegistered(UserID item){
		//List<Customer> itemList = null;
		String sql="SELECT COUNT(CUST_NAME) FROM customer_info where CUST_MOBILENO = ?";
		JdbcTemplate dynamicJdbc = getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		Customer customer = null;
		try
		   {
		     //itemList=dynamicJdbc.query(sql,new Object[] { item.getMobile() }, new ShopCustomerMapper());
			int count = dynamicJdbc.queryForObject(sql,new Object[] { item.getMobile() }, Integer.class);
			if(count > 0) {
				sql="SELECT * FROM customer_info where CUST_MOBILENO = ?";
				customer = dynamicJdbc.query(sql,new Object[] { item.getMobile() }, new RetCustomerMapper()).get(0);
			}else {
				sql="SELECT COUNT(CUST_NAME) FROM customer_info where CUST_MOBILENO = ?";
				count = customerJdbcTemplate.queryForObject(sql,new Object[] { item.getMobile() }, Integer.class);
				if(count > 0) {
					log.info("Selecting data..."+item.getMobile());
					sql="SELECT * FROM customer_info where CUST_MOBILENO = ?";
					List<Customer> itemList = customerJdbcTemplate.query(sql,new Object[] { item.getMobile() }, new RetCustomerMapper());
					log.info("Data is selected.");
					customer = itemList.get(0);
					sql="insert into customer_info (`CUST_ID`,`CUST_CODE`,`CUST_NAME`,`CUST_MOBILENO`,`CUST_EMAILID`,`PASSWORD`,"
							+ "`CUST_PHOTO`, `CUST_ADDRESS`,`CUST_ZIP`,`CUST_PROVINCE`,`CUST_CITY`,"
							+ "`CREATED_DATE`,`CREATED_BY`,`UPDATED_DATE`,`UPDATED_BY`,"
							+ "`USER_TYPE`,`ISACTIVE`,`SERVER_IP`,`DB_NAME`,`DB_USERNAME`,`DB_PASSWORD`,`USER_CREATE_STATUS`)" + 
							" values (?,?,?,?,?,?,?,?,?,?,?,now(),?,now(),?,?,?,?,?,?,?,?)";
					dynamicJdbc.update(sql,customer.getId(),customer.getCode(),customer.getName(),customer.getMobileNo(),
							customer.getEmail(),"",customer.getPhoto(),customer.getAddress(),customer.getPin(),customer.getState(),
							customer.getCity(),customer.getCreatedBy(),customer.getUpdatedBy(),customer.getUserType(),customer.getIsActive(),
							customer.getIp(),customer.getDbName(),customer.getDbUserName(),customer.getDbPassword(),"C");
					
					log.info("Data is inserted.");
				
				}else {
					customer = new Customer();
				}
			}
		   }catch(Exception e)
		     {
			   log.info("Exception "+e.toString());
		    }
		
		return customer;
	}
	
	public void getCustomerDetails(Customer item) {
		
	}
	
	public String registerCustomer(Customer item){
		//List<Customer> itemList = null;
		String sql="SELECT COUNT(CUST_NAME) FROM customer_info where CUST_MOBILENO = ?";
		JdbcTemplate dynamicJdbc = getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		JdbcTemplate dynamicShoppursShopJdbc = getDynamicDataSource(DaoConnection.CUSTOMER_DB_NAME,item.getDbUserName(),item.getDbPassword());
		String status = null;
		try
		   {
		     //itemList=dynamicJdbc.query(sql,new Object[] { item.getMobile() }, new ShopCustomerMapper());
			int count = dynamicJdbc.queryForObject(sql,new Object[] { item.getMobileNo() }, Integer.class);
			if(count == 0) {
				sql="SELECT COUNT(CUST_ID) FROM customer_info";
				count = dynamicJdbc.queryForObject(sql, Integer.class);
				count++;
				
				sql="insert into customer_info (`CUST_ID`,`CUST_CODE`,`CUST_NAME`,`CUST_MOBILENO`,`CUST_EMAILID`,`PASSWORD`,"
						+ "`CUST_PHOTO`, `CUST_ADDRESS`,`CUST_ZIP`,`CUST_PROVINCE`,`CUST_CITY`,"
						+ "`CREATED_DATE`,`CREATED_BY`,`UPDATED_DATE`,`UPDATED_BY`,"
						+ "`USER_TYPE`,`ISACTIVE`,`SERVER_IP`,`DB_NAME`,`DB_USERNAME`,`DB_PASSWORD`,`USER_CREATE_STATUS`)" + 
						" values (?,?,?,?,?,?,?,?,?,?,?,now(),?,now(),?,?,?,?,?,?,?,?)";
				
				dynamicJdbc.update(sql,count,"SHPC"+count,item.getName(),item.getMobileNo(),item.getEmail(),"","",item.getAddress(),
						item.getPin(),"","","","","Customer",1,"","","","","S");
				dynamicShoppursShopJdbc.update(sql,count,"SHPC"+count,item.getName(),item.getMobileNo(),item.getEmail(),
						"","",item.getAddress(),item.getPin(),"","","","","Customer",1,"","","","","S");
				status = "success";
			}else {
				status = "Customer is already registered";
			}
		   }catch(Exception e)
		     {
			   status = "error";
			   log.info("Exception "+e.toString());
		    }
		
		return status;
	}
	
	public List<CustomerSale> getCustomerSaleData(UserID item) {
		List<CustomerSale> itemList = null;
		String sql="SELECT ORD_ID,ORD_DATE,SUM(ORD_TOTAL_AMT) as AMOUNT " + 
				"FROM cust_order where ORD_CUST_CODE = ? group by month(ORD_DATE);";
		
		JdbcTemplate dynamicJdbc = getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		
		try
		   {
		     itemList=dynamicJdbc.query(sql,new Object[] { item.getCode() }, new CustomerSaleMapper());
		   }catch(Exception e)
		     {
			   log.info("Exception "+e.toString());
		    }
		
		return itemList;
	}
	
	public List<MyOrder> getPreOrders(UserID item) {
		List<MyOrder> itemList = null;
		JdbcTemplate dynamicJdbc = getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		String sql="SELECT * FROM  CUST_ORDER WHERE ORD_CUST_CODE = ? ORDER BY ORD_DATE DESC LIMIT ? OFFSET ?";
		try {
			itemList = dynamicJdbc.query(sql,new OrderMapper(),item.getCode(),item.getLimit(),item.getOffset());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return itemList;
	}
	
	public String setFavourite(Favourite item) {
		String status = "faliure";
		JdbcTemplate dynamicJdbc = getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		String sql = "UPDATE customer_info SET " + 
				"`IS_FAVOURITE` = ? WHERE `CUST_CODE` = ?"; 
		try {
			dynamicJdbc.update(sql,item.getFavStatus(),item.getCode());
			
			status = "success";
		}catch(Exception e) {
			status = "error";
			e.printStackTrace();
		}
		return status;
	}
	
	public String setRatings(Ratings item) {
		String status = "faliure";
		JdbcTemplate dynamicJdbc = getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		String sql = "UPDATE customer_info SET " + 
				"`RATINGS` = ? WHERE `CUST_CODE` = ?"; 
		try {
			dynamicJdbc.update(sql,item.getRatings(),item.getCustCode());
			
			sql="SELECT COUNT(*) FROM cust_ratings where SHOP_CODE = ? AND CUST_CODE = ?";
			int count = customerJdbcTemplate.queryForObject(sql,Integer.class,item.getShopCode(),item.getCustCode());
			if(count == 0) {
				sql="insert into cust_ratings (`CUST_CODE`,`SHOP_CODE`,`RATINGS`)" + 
						" values (?,?,?)";
				customerJdbcTemplate.update(sql,item.getCustCode(),item.getShopCode(),item.getRatings());
			}else {
				sql = "UPDATE cust_ratings SET " + 
						"`RATINGS` = ? WHERE SHOP_CODE = ? AND CUST_CODE = ?"; 
				customerJdbcTemplate.update(sql,item.getRatings(),item.getShopCode(),item.getCustCode());	
			}
				
			status = "success";
		}catch(Exception e) {
			status = "error";
			e.printStackTrace();
		}
		return status;
	}
	
	
	private JdbcTemplate getDynamicDataSource(String dbName,String dbUserName,String dbPassword) {
		dynamicDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dynamicDataSource.setUrl("jdbc:mysql://"+DaoConnection.BASE_URL+":3306/"+dbName);
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
