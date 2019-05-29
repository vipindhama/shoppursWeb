package com.shoppurs.shop.dao;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.tomcat.util.codec.binary.Base64;
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
import com.shoppurs.shop.controller.ShoppursApiController;
import com.shoppurs.shop.mapper.DbUserMapper;
import com.shoppurs.shop.mapper.MyUserMapper;
import com.shoppurs.shop.mapper.ProductSaleMapper;
import com.shoppurs.shop.mapper.ShopCustomerSaleMapper;
import com.shoppurs.shop.mapper.UserLicenseMapper;
import com.shoppurs.shop.model.Customer;
import com.shoppurs.shop.model.DbUser;
import com.shoppurs.shop.model.MyBank;
import com.shoppurs.shop.model.MyDataSource;
import com.shoppurs.shop.model.MyUser;
import com.shoppurs.shop.model.ProductSaleObject;
import com.shoppurs.shop.model.ShopCustomerSaleData;
import com.shoppurs.shop.model.UserID;
import com.shoppurs.shop.model.UserLicense;
import com.shoppurs.shop.model.UserLogin;
import com.shoppurs.shop.model.requestModel.ShopSaleReq;
import com.shoppurs.shop.model.requestModel.UserDetailsReq;
import com.shoppurs.utilities.Constants;

public class RetailerDao {
	
	private static final Logger log = LoggerFactory.getLogger(ShoppursApiController.class);
	
	@Autowired
    private DaoConnection daoConnection;
	
	@Autowired
   	@Qualifier("auth-database")
    private JdbcTemplate authJdbcTemplate;

	
public String manageRegistration(MyUser myUser) {
		
		String query = "{ call manage_registration(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }";
	    ResultSet rs;
	    
	    int status = 1;
        String message = "";
        String dbUserName = "",dbPassword = "";
	    
	    try {
	    	Connection conn = authJdbcTemplate.getDataSource().getConnection();
	        CallableStatement stmt = conn.prepareCall(query);
	    	stmt.setString(1, myUser.getUsername());
	    	stmt.setString(2, myUser.getShopName());
	    	stmt.setString(3, myUser.getShopCode());
	    	stmt.setString(4, myUser.getUserEmail());
	    	stmt.setString(5, myUser.getMobile());
	    	stmt.setString(6, myUser.getLanguage());
	    	stmt.setString(7, myUser.getMpassword());
	    	stmt.setString(8, myUser.getCity());
	    	stmt.setString(9, myUser.getProvince());
	    	stmt.setString(10, myUser.getCountry());
	    	stmt.setString(11, myUser.getZip());
	    	stmt.setString(12, myUser.getAddress());
	    	stmt.setString(13, myUser.getImeiNo());
	    	stmt.setString(14, myUser.getPhoto());
	    	stmt.setString(15, myUser.getIdProof());
	    	stmt.setString(16, myUser.getPanNo());
	    	stmt.setString(17, myUser.getAadharNo());
	    	stmt.setString(18, myUser.getGstNo());
	    	stmt.setString(19, myUser.getUserLat());
	    	stmt.setString(20, myUser.getUserLong());
	    	stmt.setString(21, myUser.getUserType());
	    	stmt.setString(22, myUser.getCreatedBy());
	    	stmt.setString(23, myUser.getUpdatedBy());
	    	stmt.setInt(24, 1);
	    	stmt.registerOutParameter(25, java.sql.Types.INTEGER);
	    	stmt.registerOutParameter(26, java.sql.Types.VARCHAR);
	    	stmt.registerOutParameter(27, java.sql.Types.VARCHAR);
	    	stmt.registerOutParameter(28, java.sql.Types.VARCHAR);
	        stmt.execute();
	       // rs = stmt.getResultSet();
	        status = stmt.getInt(25);
        	message = stmt.getString(26);
        	dbUserName = stmt.getString(27);
        	dbPassword = stmt.getString(28);
        	
        	/*String sql = "SELECT RET_CODE FROM shoppurs_shop.retailer_info WHERE RET_ID = 
                            (SELECT max(RET_ID) FROM shoppurs_shop.retailer_info)";
        	
        	String shopCode = shopJdbcTemplate.queryForObject(sql, String.class);*/
        	
        	try {
        		
        		File file = new File(Constants.FULL_IMAGE_PATH+"\\shops\\"+dbUserName+"\\");
		        if (!file.exists()) {
		            if (file.mkdirs()) {
		                System.out.println("Directory is created!");
		            } else {
		                System.out.println("Failed to create directory!");
		            }
		        } 
		        
		        String directory = Constants.FULL_IMAGE_PATH+"\\shops\\"+dbUserName+"\\qrcode.PNG";
		        
				generateQRCodeImage(myUser.getMobile(),350,350,directory);
				
			} catch (WriterException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
	    } catch (SQLException ex) {
	        System.out.println(ex.getMessage());
	    }
	    
	    
				
		
		/*SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(authJdbcTemplate)
				.withProcedureName("manage_registration");
		SqlParameterSource in = new MapSqlParameterSource()
				.addValue("fname", myUser.getFname())
				.addValue("mname", myUser.getMname())
				.addValue("lname", myUser.getLname())
				.addValue("photo", myUser.getPhoto())
				.addValue("email", myUser.getEmail())
				.addValue("pmobile", Integer.parseInt(myUser.getPmobileNo()))
				.addValue("smobile", Integer.parseInt(myUser.getSmobileNo()))
				.addValue("address", myUser.getAddress())
				.addValue("address2", myUser.getAddress2())
				.addValue("city", myUser.getCity())
				.addValue("province", myUser.getState())
				.addValue("zip", myUser.getPin())
				.addValue("passport_no", myUser.getPassportNo())
				.addValue("visa_no", myUser.getVisaNo())
				.addValue("visa_expdate", myUser.getVisaExpDate())
				.addValue("Nationality", myUser.getNationality())
				.addValue("country_code", 91)
                .addValue("dob", myUser.getDob())
                .addValue("Anniversary_Date", myUser.getAnnivarsaryDate())
                .addValue("gender", myUser.getGender())
                .addValue("identification_mark", myUser.getIdentityMark())
                .addValue("server_ip","server_ip")
                .addValue("db_name","db_name")
                .addValue("db_user_name","db_user_name")
                .addValue("db_password","db_password")
                .addValue("user_type","seller")
                .addValue("uname","superadmin")
                .addValue("pass","12345")
				.addValue("action", 1);
		Map<String, Object> out = simpleJdbcCall.execute(in);
		
		int status = (int) out.get("status");
		String message = (String) out.get("message");*/
		return status+"-"+message+"-"+dbUserName+"-"+dbPassword;
	}
	
	public String createCustomer(Customer customer) {
		
		JdbcTemplate jdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.SHOPPURS_DB_NAME,
				customer.getDbUserName(),customer.getDbPassword());
		
		String sql2="select count(*) from RETAILER_MASTER where RET_EMAIL_ID=?";
		int count=jdbcTemplate.queryForObject(sql2,Integer.class,customer.getEmail());
		if(count == 0) {
			String id = "";
	    	boolean isExist = true;
	    	while(isExist) {
	    		id = generateRandom(16);	
	    		log.info(id);
	    		String sql1="select count(*) from RETAILER_MASTER where RET_ID=?";
	    		count=jdbcTemplate.queryForObject(sql1,Integer.class,id);
	    		if(count == 0) {
	    			isExist = false;
	    		}
	    	}
	    	
	    //	customer.setId(id);
	    	
	    	String sql="insert into RETAILER_MASTER (RET_ID,RET_CODE,RET_NAME,"
	    			+ "RET_MOBILE_NO,RET_ADDRESS,RET_PIN_CODE,RET_EMAIL_ID,RET_PHOTO,RET_PASSWORD,"
	    			+ "RET_COUNTRY,RET_STATE,RET_CITY,"
	    			+ "RET_PAN_NO,RET_AADHAR_NO,RET_GST_NO,CREATED_DATE,UPDATED_DATE,CREATED_BY,UPDATED_BY) values "
	    			+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),now(),?,?)";
	    	jdbcTemplate.update(sql, 0,customer.getCode(),customer.getName(),customer.getMobileNo(),
	    			customer.getAddress(),customer.getPin(),customer.getEmail(),customer.getPhoto(),customer.getPassword(),
	    			customer.getCountry(),customer.getState(),customer.getCity(),
	    			customer.getPanNo(),customer.getAadharNo(),customer.getGstNo(),
	    			customer.getCreatedBy(),customer.getUpdatedBy());
	    	
	    	return "success";
		}else {
			return "Email is already registered.";
		}
		
	}
	
public Object loginRetailer(UserLogin userLogin) {
		
		MyUser myUser = null;
		String status = "failure";
		String sql="select count(*) from USER_INFO where MOBILE=? and PASSWORD = ?";
		int count=authJdbcTemplate.queryForObject(sql,Integer.class,userLogin.getMobile(),userLogin.getPassword());
		if(count == 0) {
			status = "Authentication failed. Mobile number or password is incorrect.";
			return status;
		}else {
			sql="select DB_NAME,DB_USERNAME,DB_PASSWORD from USER_INFO where MOBILE=?";
			DbUser dbUser=authJdbcTemplate.query(sql, new DbUserMapper(),userLogin.getMobile()).get(0);
			JdbcTemplate dynamicShopJdbc = daoConnection.getDynamicDataSource(dbUser.getDbName(),dbUser.getDbUserName(),dbUser.getDbPassword());
			sql="select * from RETAILER_INFO where RET_MOBILE_NO=?";
			MyUser item=dynamicShopJdbc.query(sql, new MyUserMapper(),userLogin.getMobile()).get(0);
			return item;
		}
	}	
	
public String updateBankDetails(MyBank item) {
	
	String status = "faliure";
	JdbcTemplate shopJdbcTemplate = null,dynamicJdbc = null;
	String directory="";
	byte[] imageByte1 = null;
	try {
		shopJdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.SHOP_DB_NAME,
				item.getDbUserName(),item.getDbPassword());
		
		if(!item.getChequeImage().equals("no")) {
			
			File file = new File(Constants.FULL_IMAGE_PATH+"\\shops\\"+item.getDbName()+"\\");
	        if (!file.exists()) {
	            if (file.mkdirs()) {
	                System.out.println("Directory is created!");
	            } else {
	                System.out.println("Failed to create directory!");
	            }
	        } 
	        
	        directory = Constants.FULL_IMAGE_PATH+"\\shops\\"+item.getDbName()+"\\"+
    				"cheque.jpg";
    		imageByte1=Base64.decodeBase64(item.getChequeImage());
			new FileOutputStream(directory).write(imageByte1);
			
		}
		
		item.setChequeImage(Constants.IMAGE_URL+"/shops/"+item.getDbName()+"/cheque.jpg");
			
		String sql="UPDATE RETAILER_INFO SET RET_SHOP_NAME = ?, RET_BANK_NAME = ?, RET_BANK_ACCOUNT = ?, RET_BANK_IFSC_CODE = ?, "
				+ "RET_BANK_BRANCH_ADDRESS = ?, RET_CHEQUE_IMAGE = ? where RET_MOBILE_NO = ?";
		log.info("Password "+item.getDbPassword());
		shopJdbcTemplate.update(sql,item.getBussinessName(),item.getBankName(),item.getAcctNo(),item.getIfscCode(),
				item.getBranchAddress(),item.getMobile(),item.getChequeImage());
		dynamicJdbc = daoConnection.getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		
		dynamicJdbc.update(sql,item.getBussinessName(),item.getBankName(),item.getAcctNo(),
				item.getIfscCode(),item.getBranchAddress(),item.getChequeImage(),item.getMobile());
		
		
		
		status = "success";
	}catch(Exception e) {
		status = "error";
		e.printStackTrace();
	}finally {
		try {
			shopJdbcTemplate.getDataSource().getConnection().close();
			dynamicJdbc.getDataSource().getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	return status;
		
	}

public MyUser getUserDetails(UserDetailsReq item) {
	
	JdbcTemplate shopJdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.SHOP_DB_NAME,
			item.getDbUserName(),item.getDbPassword());
	
	String sql="select * from RETAILER_INFO where RET_EMAIL_ID=?";
	try
	   {
	     List<MyUser> itemList=shopJdbcTemplate.query(sql, new MyUserMapper(),item.getHeader());
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

public MyUser getUserDetailsWithMobile(UserDetailsReq item) {
	
	JdbcTemplate shopJdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.SHOPPURS_DB_NAME,
			item.getDbUserName(),item.getDbPassword());
	
	String sql="select * from RETAILER_INFO where RET_MOBILE_NO=?";
	try
	   {
	     List<MyUser> itemList=shopJdbcTemplate.query(sql, new MyUserMapper(),item.getHeader());
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

public HashMap<String,Object> getShopSaleData(ShopSaleReq item) {
	List<ShopCustomerSaleData> itemCustomerSaleList = null;
	HashMap<String,Object> hashMap = new HashMap();
	List<ProductSaleObject> itemMonthlySaleList = null;
	String sql="SELECT so.INVM_DATE,sum(sod.INVD_QTY),sum(sod.INVD_TAMOUNT) FROM invoice_master as so, invoice_detail as sod " + 
			" where so.INVM_ID = sod.INVD_INVM_ID and so.INVM_DATE BETWEEN  ? AND ? AND so.INVM_STATUS = 'Generated' group by month(so.INVM_DATE)";
	
	String sqlCustomerData="SELECT INVM_ID,INVM_DATE,INVM_CUST_NAME,INVM_TOT_NET_PAYABLE FROM invoice_master WHERE "
			+ "INVM_DATE BETWEEN  ? AND ? AND INVM_STATUS = 'Generated'";
	
	JdbcTemplate dynamicJdbc = daoConnection.getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
	
	try
	   {
		itemMonthlySaleList=dynamicJdbc.query(sql,new ProductSaleMapper(),item.getFromDate(),item.getToDate());
		itemCustomerSaleList=dynamicJdbc.query(sqlCustomerData,new ShopCustomerSaleMapper(),item.getFromDate(),item.getToDate());
		hashMap.put("monthlyGraphData", itemMonthlySaleList);
	     hashMap.put("customerSaleData", itemCustomerSaleList);
	   }catch(Exception e)
	     {
		   log.info("Exception "+e.toString());
	    }
	
	return hashMap;
}

public String buyUserLicense(UserLicense item) {
	String status = "failure";	
	JdbcTemplate dynamicJdbc = daoConnection.getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
	JdbcTemplate dynamicShopJdbc = daoConnection.getDynamicDataSource(DaoConnection.SHOP_DB_NAME,item.getDbUserName(),item.getDbPassword());
	
	   try {
		   String sql="insert into USER_LICENSE (`UL_ID`," + 
		   		"`UL_SHOP_CODE`," + 
		   		"`UL_SHOP_MOBILE`," + 
		   		"`UL_NO_OF_USER`," + 
		   		"`UL_SCHEME`," + 
		   		"`UL_AMOUNT`," + 
		   		"`UL_LICENSE_PURCHASE_DATE`," + 
		   		"`UL_LICENSE_RENEW_DATE`," + 
		   		"`UL_LICENSE_EXPIRE_DATE`," + 
		   		"`UL_LICENSE_TYPE`," + 
		   		"`CREATED_BY`," + 
		   		"`UPDATED_BY`," + 
		   		"`CREATED_DATE`," + 
		   		"`UPDATED_DATE`) values "
	    			+ "(0,?,?,?,?,?,now(),now(),?,?,?,?,now(),now())";
		   
		   Calendar calendar = Calendar.getInstance(Locale.getDefault());
		   if(item.getScheme().equals("Monthly")) {
			   calendar.add(Calendar.MONTH, 1);
		   }else if(item.getScheme().equals("Quarterly")) {
			   calendar.add(Calendar.MONTH, 6); 
		   }else if(item.getScheme().equals("Yearly")) {
			   calendar.add(Calendar.YEAR, 1);
		   }
		   String timeStamp=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
		   
		   dynamicJdbc.update(sql,item.getShopCode(),item.getShopMobile(),item.getNumOfUsers(),item.getScheme(),item.getAmount(),timeStamp,
				   item.getLicenseType(),item.getUserName(),item.getUserName());
		   dynamicShopJdbc.update(sql,item.getShopCode(),item.getShopMobile(),item.getNumOfUsers(),item.getScheme(),item.getAmount(),timeStamp,
				   item.getLicenseType(),item.getUserName(),item.getUserName());
		   status = "success";
	    	
	   }catch(Exception e) {
		   status = "error";
		   e.printStackTrace();
	   }
	
	return status;
}

public List<UserLicense> getUserLicenses(UserID item) {
	
	JdbcTemplate shopJdbcTemplate = daoConnection.getDynamicDataSource(item.getDbName(),
			item.getDbUserName(),item.getDbPassword());
	
	List<UserLicense> itemList = new ArrayList();
	
	String sql="select * from user_license";
	try
	   {
	     itemList=shopJdbcTemplate.query(sql, new UserLicenseMapper());
	       
	   }catch(Exception e)
	     {
		   log.info("Exception "+e.toString());
		    
	    }
	
	return itemList;
}

private void generateQRCodeImage(String text, int width, int height, String filePath)
        throws WriterException, IOException {
	
    QRCodeWriter qrCodeWriter = new QRCodeWriter();
    BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

    Path path = FileSystems.getDefault().getPath(filePath);
    MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
}

private byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
    QRCodeWriter qrCodeWriter = new QRCodeWriter();
    BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
    
    ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
    MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
    byte[] pngData = pngOutputStream.toByteArray(); 
    return pngData;
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

}
