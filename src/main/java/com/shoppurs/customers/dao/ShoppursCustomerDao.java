package com.shoppurs.customers.dao;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.shoppurs.connection.DaoConnection;
import com.shoppurs.customers.controller.CustomerApiController;
import com.shoppurs.customers.mapper.CategoryMapper;
import com.shoppurs.customers.mapper.CustomerMapper;
import com.shoppurs.customers.mapper.MyShopMapper;
import com.shoppurs.customers.mapper.ProductMapper;
import com.shoppurs.customers.mapper.ShopCodeMapper;
import com.shoppurs.customers.mapper.SubCatMapper;
import com.shoppurs.customers.mapper.UserMapper;
import com.shoppurs.customers.model.Category;
import com.shoppurs.customers.model.Customer;
import com.shoppurs.customers.model.MyDataSource;
import com.shoppurs.customers.model.MyProduct;
import com.shoppurs.customers.model.MyShop;
import com.shoppurs.customers.model.ShopCode;
import com.shoppurs.customers.model.MyUser;
import com.shoppurs.customers.model.SubCategory;
import com.shoppurs.customers.model.UserID;
import com.shoppurs.customers.model.UserLogin;
import com.shoppurs.utilities.Constants;


public class ShoppursCustomerDao {
	
	private static final Logger log = LoggerFactory.getLogger(CustomerApiController.class);
	
	@Autowired
    private MyDataSource dynamicDataSource;
	
	@Autowired
	@Qualifier("master-database")
    private JdbcTemplate masterjdbcTemplate;
	
	@Autowired
   	@Qualifier("auth-database")
    private JdbcTemplate authJdbcTemplate;
	
	@Autowired
   	@Qualifier("shop-database")
    private JdbcTemplate shopJdbcTemplate;
	
	@Autowired
	@Qualifier("product-database")
	private JdbcTemplate productJdbcTemplate;
	
	@Autowired
	@Qualifier("customer-database")
	private JdbcTemplate customerJdbcTemplate;
	
	
	/*
	 * @Autowired
	 * 
	 * @Qualifier("shop-database") private JdbcTemplate shopJdbcTemplate;
	 */
	
	public String manageRegistration(MyUser myUser) {
		String query = "{ call customer_registration(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }";
	    ResultSet rs;
	    
	    int status = 0;
        String message = "";
	    
	    try {
	    	Connection conn = authJdbcTemplate.getDataSource().getConnection();
	        CallableStatement stmt = conn.prepareCall(query);
	    	stmt.setString(1, myUser.getUsername());
	    	stmt.setString(2, myUser.getUser_email());
	    	stmt.setString(3, myUser.getMobile());
	    	stmt.setString(4, myUser.getMpassword());
	    	stmt.setString(5, myUser.getCity());
	    	stmt.setString(6, myUser.getProvince());
	    	stmt.setString(7, myUser.getCountry());
	    	stmt.setString(8, myUser.getZip());
	    	stmt.setString(9, myUser.getAddress());
	    	stmt.setString(10, myUser.getPhoto());
	    	stmt.setString(11, myUser.getUserLat());
	    	stmt.setString(12, myUser.getUserLong());
	    	stmt.setString(13, myUser.getUser_type());
	    	stmt.setString(14, myUser.getCreated_by());
	    	stmt.setString(15, myUser.getUpdated_by());
	    	stmt.setInt(16, myUser.getAction());
	    
	    	
	    	//stmt.setInt(10, 1);
	    	stmt.registerOutParameter(17, java.sql.Types.INTEGER);
	    	stmt.registerOutParameter(18, java.sql.Types.VARCHAR);
	        stmt.execute();
	       // rs = stmt.getResultSet();
	        status = stmt.getInt(10);
        	message = stmt.getString(11);
        	
        	String sql = "SELECT CUST_CODE FROM customer_info WHERE CUST_ID = " + 
        			"(SELECT max(CUST_ID) FROM customer_info)";
        	
        	String custCode = shopJdbcTemplate.queryForObject(sql, String.class);
        	
           try {
        		
        		File file = new File(Constants.FULL_IMAGE_PATH+"\\customers\\"+custCode+"\\");
		        if (!file.exists()) {
		            if (file.mkdirs()) {
		                System.out.println("Directory is created!");
		            } else {
		                System.out.println("Failed to create directory!");
		            }
		        } 
		        
		        String directory = Constants.FULL_IMAGE_PATH+"\\customers\\"+custCode+"\\qrcode.PNG";
		        
				generateQRCodeImage(myUser.getMobile(),350,350,directory);
				
			} catch (WriterException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
	    } catch (SQLException ex) {
	        System.out.println(ex.getMessage());
	    }
	    return status+"|"+message;
	}
	    

	public MyUser loginCustomer(UserLogin userLogin) {
		String status = "success";
		MyUser user =null;
		String sql2="select count(*) from shoppurs_auth.user_info where MOBILE=? and PASSWORD = ?";
		int count=authJdbcTemplate.queryForObject(sql2,Integer.class,userLogin.getMobile(),userLogin.getPassword());
		if(count == 0) {   // user not valid 
			status = "0";
		}else {
			status = "1";  // user match
			user= getCustomerDetails(userLogin.getMobile());
		}
		return user;
	}
	
	public MyUser getCustomerDetails(String mobile) {
	
		String sql="select * from shoppurs_auth.user_info where MOBILE=?";
		try
		   {
		     List<MyUser> userlist=authJdbcTemplate.query(sql, new UserMapper(),mobile);
		       if(userlist.size() > 0) {
			      return userlist.get(0);
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
	
	
	
	public List<Category> getCategories() {
		
		
		
		/*	String sql="select * from CATEGORY_MASTER";
			try
			   {
			     List<Category> itemList=jdbcTemplate.query(sql, new CategoryMapper());
			       if(itemList.size() > 0) {
				      return itemList;
			       }else {
			    	   log.info("CategoryList size is 0");
			    	   return null;
			       }
			   }catch(Exception e)
			     {
				   log.info("Exception "+e.toString());
				     return null;
			    }*/
		
		String query = "{ call get_all_categories() }";
	    ResultSet rs;
	    List<Category> itemList = new ArrayList<Category>();
	    
	    try (Connection conn = masterjdbcTemplate.getDataSource().getConnection();
	            CallableStatement stmt = conn.prepareCall(query)) {
	    	//stmt.setInt(1, candidateId);
	        rs = stmt.executeQuery();
	        while (rs.next()) {
	        	Category item = new Category();
	    		item.setCatId(rs.getInt("CATEGORY_ID"));
	    		item.setCatName(rs.getString("CATEGORY_NAME"));
	    		item.setDelStatus(rs.getString("DEL_STATUS"));
	    		item.setCreatedBy(rs.getString("CREATED_BY"));
	    		item.setUpdatedBy(rs.getString("UPDATED_BY"));
	    		item.setCreatedDate(rs.getString("CREATED_DATE"));
	    		item.setUpdatedDate(rs.getString("UPDATED_DATE"));
	    		itemList.add(item);
	        }
	    } catch (SQLException ex) {
	        System.out.println(ex.getMessage());
	    }
		
		/*SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
				.withProcedureName("get_all_categories");
		
		Map<String, Object> out = simpleJdbcCall.execute();
			
		List<Category> itemList = null;*/
		return itemList;
		
		}
	
	// get subcategory list by catid
	public List<SubCategory> getSubCategoryList(int catId) {
		
		String sql="select * from SUB_CATEGORY_MASTER where SUB_CATEGORY_CAT_ID = ?";
		try
		   {
		     List<SubCategory> itemList=masterjdbcTemplate.query(sql, new SubCatMapper(),catId);
		       if(itemList.size() > 0) {
			      return itemList;
		       }else {
		    	   log.info("CategoryList size is 0");
		    	   return null;
		       }
		   }catch(Exception e)
		     {
			   log.info("Exception "+e.toString());
			     return null;
		    }
	}


	public SubCategory getSubCategoryDetails(String name) {
		
		String sql="select * from SUB_CATEGORY_MASTER where SUB_CATEGORY_NAME=?";
		try
		   {
		     List<SubCategory> itemList=masterjdbcTemplate.query(sql, new SubCatMapper(),name);
		       if(itemList.size() > 0) {
			      return itemList.get(0);
		       }else {
		    	   log.info("CategoryList size is 0");
		    	   return null;
		       }
		   }catch(Exception e)
		     {
			   log.info("Exception "+e.toString());
			     return null;
		    }
	}
	
	
	public List<MyProduct> getProductList() {
		
		String sql="select * from PRODUCT_MASTER";
		try
		   {
		     List<MyProduct> itemList= productJdbcTemplate.query(sql, new ProductMapper());
		       if(itemList.size() > 0) {
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
	
	
	
	public List<MyProduct> getProductList(String subCats) {
		log.info("SubCats "+subCats);
		String sql="select * from PRODUCT_MASTER where PROD_SUB_CAT_ID IN ("+subCats+")";
		try
		   {
		     List<MyProduct> itemList=productJdbcTemplate.query(sql, new ProductMapper());
		       if(itemList.size() > 0) {
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

	public HashMap<String,Object> get_cat_subcat() {
		List<Object> itemList = new ArrayList();
		HashMap<String,Object> hashMap = new HashMap();
		List<Category> catItemList = null;
		List<SubCategory> subCatItemList = null;
		List<MyProduct> productItemList = null;
		String sql="select * from category_master";
		String sql2="";
		//String sql3="select * from product_master";
		//JdbcTemplate dynamicJdbc = getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		try
		   {
			 catItemList=masterjdbcTemplate.query(sql, new CategoryMapper());
			 for(Category cat : catItemList) {
				 subCatItemList = new ArrayList();
				 sql2="select * from sub_category_master WHERE SUB_CATEGORY_CAT_ID = ? ORDER BY SUB_CATEGORY_ID"; 
				 subCatItemList=masterjdbcTemplate.query(sql2, new SubCatMapper(),cat.getCatId());
				 cat.setSubCatList(subCatItemList);
			 }
		     
		    // productItemList=productJdbcTemplate.query(sql3, new ProductMapper());
		     hashMap.put("categories", catItemList);
		    // hashMap.put("sub_categories", subCatItemList);
		     //hashMap.put("products", productItemList);
		   }catch(Exception e)
		     {
			   log.info("Exception "+e.toString());
			   //  return null;
		    }
		
		return hashMap;
	}
	
	
	public HashMap<String,Object> shoplist(int subcat_id) {
		HashMap<String,Object> hashMap = new HashMap();
		StringBuilder shopList = new StringBuilder();
		String query = "select * from subcategory_shops where subcat_id = ?";
		try
		   {
		     List<ShopCode> itemList=shopJdbcTemplate.query(query, new ShopCodeMapper(),subcat_id);
		       if(itemList.size() > 0) {
			     for(ShopCode shop: itemList) {
			    	 shopList.append("'"+shop.getShopcode()+"'");
			    	 shopList.append(",");
			     }
			     
			     String mShopCode = shopList.substring(0, shopList.length() - 1);
			     System.out.println("mShopList "+mShopCode);
			     String shopquery = "SELECT * FROM shoppurs_shop.retailer_info WHERE `ret_code` IN ("+mShopCode+")";
			     List<MyShop> shopListDetails =shopJdbcTemplate.query(shopquery, new MyShopMapper());
			     hashMap.put("shoplist", shopListDetails);		
		       }else {
		    	   log.info("ShopList size is 0");
		    	   return null;
		       }
		   }catch(Exception e)
		     {
			   log.info("Exception "+e.toString());
			     return null;
		    }
		
		return hashMap;
	}
	
	
	public HashMap<String,Object> get_producfromshop(UserID item) {
		List<Object> itemList = new ArrayList();
		HashMap<String,Object> hashMap = new HashMap();
		
		List<MyProduct> productItemList = null;
		String sql3="select * from ret_product where prod_sub_cat_id=?";
		JdbcTemplate dynamicJdbc = getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		try
		   {
		     productItemList=dynamicJdbc.query(sql3, new ProductMapper(), item.getSubcatid());
		     hashMap.put("products", productItemList);
		   }catch(Exception e)
		     {
			   log.info("Exception "+e.toString());
			   //  return null;
		    }
		
		return hashMap;
	}
	
	
	
public MyUser getUserDetails(String email) {
		
		String sql="select * from customer_info where CUST_EMAILID=?";
		try
		   {
		     List<MyUser> itemList=customerJdbcTemplate.query(sql, new UserMapper(),email);
		       if(itemList.size() > 0) {
			      return itemList.get(0);
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


public String generateQrCode(UserID item) {
	String status = "failure";
	
	try {
		
		File file = new File(Constants.FULL_IMAGE_PATH+"\\customers\\"+item.getCode()+"\\");
        if (!file.exists()) {
            if (file.mkdirs()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        } 
        
        String directory = Constants.FULL_IMAGE_PATH+"\\customers\\"+item.getCode()+"\\qrcode.PNG";
        
		generateQRCodeImage(item.getMobile(),350,350,directory);
		
		status = "success";
		
	} catch (WriterException | IOException e) {
		// TODO Auto-generated catch block
		status = "error";
		e.printStackTrace();
	}
	
	return status;
}

private void generateQRCodeImage(String text, int width, int height, String filePath)
        throws WriterException, IOException {
	
    QRCodeWriter qrCodeWriter = new QRCodeWriter();
    BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

    Path path = FileSystems.getDefault().getPath(filePath);
    MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
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
