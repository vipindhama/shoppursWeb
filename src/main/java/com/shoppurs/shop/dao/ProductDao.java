package com.shoppurs.shop.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.codec.binary.Base64;
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
import com.shoppurs.shop.mapper.ProductBarCodeMapper;
import com.shoppurs.shop.mapper.ProductMapper;
import com.shoppurs.shop.mapper.ProductRatingMapper;
import com.shoppurs.shop.mapper.ProductSaleMapper;
import com.shoppurs.shop.mapper.BarCodeMapper;
import com.shoppurs.shop.model.Barcode;
import com.shoppurs.shop.model.MyDataSource;
import com.shoppurs.shop.model.MyProduct;
import com.shoppurs.shop.model.ProductBarcode;
import com.shoppurs.shop.model.ProductRating;
import com.shoppurs.shop.model.ProductSaleObject;
import com.shoppurs.shop.model.UserID;
import com.shoppurs.shop.model.requestModel.CategoryDetailReq;
import com.shoppurs.utilities.Constants;

public class ProductDao {
	
	private static final Logger log = LoggerFactory.getLogger(ShoppursApiController.class);
	
	@Autowired
    private DaoConnection daoConnection;
	
	
	public String addRetailerProduct(List<MyProduct> listItem) {
		byte[] imageByte1 = null,imageByte2 = null,imageByte3 =null;
        String directory="";
        
        MyProduct myItem = listItem.get(0);
        int status = 1,prodId = 0;
	    String message = "",image1,image2,image3;
	    
	    JdbcTemplate productJdbcTemplate = null;
	    
        try {
        	productJdbcTemplate = daoConnection.getDynamicDataSource(myItem.getDbName(),
            		myItem.getDbUserName(),myItem.getDbPassword());
        	
        	/*String sql = "SELECT COUNT(PROD_ID) FROM ret_product";
        	int count = productJdbcTemplate.queryForObject(sql,Integer.class);
        	if(count > 0) {
        		sql = "SELECT MAX(PROD_ID) FROM ret_product";
            	int maxId = productJdbcTemplate.queryForObject(sql,Integer.class);
            	prodId = maxId + 1;
        	}else {
        		prodId =  1;
        	}*/
        	
        	
        	String sql = "insert into ret_product (`PROD_ID`,`PROD_SUB_CAT_ID`,`PROD_NAME`,`PROD_CODE`," + 
        			"`PROD_DESC`,`PROD_MRP`,`PROD_SP`,`PROD_REORDER_LEVEL`,`PROD_QOH`,`PROD_HSN_CODE`,`PROD_CGST`,`PROD_IGST`," + 
        			"`PROD_SGST`,`PROD_WARRANTY`,`PROD_MFG_DATE`,`PROD_EXPIRY_DATE`,`PROD_MFG_BY`,`PROD_IMAGE_1`," + 
        			"`PROD_IMAGE_2`,`PROD_IMAGE_3`,`CREATED_BY`,`UPDATED_BY`,`CREATED_DATE`,`UPDATED_DATE`,`DEL_STATUS`,`IS_BARCODE_AVAILABLE`) "
        			+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),now(),?,?)";
        	
        	for(MyProduct item : listItem) {
        		
        		sql = "SELECT MAX(PROD_ID) FROM ret_product";
            	prodId = productJdbcTemplate.queryForObject(sql,Integer.class);
            	prodId = prodId + 1;
        		
        		image1 = Constants.IMAGE_URL+"/shops/"+item.getShopCode()
        		+"/products/"+prodId+"/"+"1.jpg";
        		image2 = Constants.IMAGE_URL+"/shops/"+item.getShopCode()
        		+"/products/"+prodId+"/"+"2.jpg";
        		image3 = Constants.IMAGE_URL+"/shops/"+item.getShopCode()
        		+"/products/"+prodId+"/"+"3.jpg";
        		
        		/*if(item.getProdBarCode() == null || item.getProdBarCode().equals("null") || item.getProdBarCode().equals("")) {
        			
        		}else {
        			item.setIsBarcodeAvailable("Y");
        		}*/
        		
        		if(item.getBarcodeList() == null)
    			    item.setIsBarcodeAvailable("N");
    			else
    				item.setIsBarcodeAvailable("Y");	
        		 
            	productJdbcTemplate.update(sql,prodId,item.getProdCatId(),item.getProdName(),item.getProdCode(),
            			item.getProdDesc(),item.getProdMrp(),item.getProdSp(),item.getProdReorderLevel(),item.getProdQoh(),
            			item.getProdHsnCode(),item.getProdCgst(),item.getProdIgst(),item.getProdSgst(),item.getProdWarranty(),
            			item.getProdMfgDate(),item.getProdExpiryDate(),item.getProdMfgBy(),image1,image2,image3,
            			item.getCreatedBy(),item.getUpdatedBy(),"N",item.getIsBarcodeAvailable());
            	
            	
            	
            	if(item.getIsBarcodeAvailable().equals("Y")){
            		for(Barcode barcode: item.getBarcodeList()) {
            			sql = "insert into product_barcodes (`PROD_PROD_ID`,`PROD_CODE`,`PROD_BARCODE`,`SOLD_STATUS`) "
                	    		+ "values(?,?,?,?)";
                	    productJdbcTemplate.update(sql,prodId,item.getProdCode(),barcode.getBarcode(),"N");
            		}
            		
            	}
        		
        	 
        	    File file = new File(Constants.FULL_IMAGE_PATH+"\\shops\\"+item.getShopCode()
        	    +"\\products\\"+prodId+"\\");
        	    
		        if (!file.exists()) {
		            if (file.mkdirs()) {
		                System.out.println("Directory is created!");
		            } else {
		                System.out.println("Failed to create directory!");
		            }
		        }
		        
		        image1 = Constants.FULL_IMAGE_PATH+"\\shops\\"+item.getShopCode()
        		+"\\products\\"+prodId+"\\"+"1.jpg";
        		image2 = Constants.FULL_IMAGE_PATH+"\\shops\\"+item.getShopCode()
        		+"\\products\\"+prodId+"\\"+"2.jpg";
        		image3 = Constants.FULL_IMAGE_PATH+"\\shops\\"+item.getShopCode()
        		+"\\products\\"+prodId+"\\"+"3.jpg";
		        
		        try {
	            	if(!item.getProdImage1().equals("no")) {
	            		
	            		imageByte1=Base64.decodeBase64(item.getProdImage1());
	    				new FileOutputStream(image1).write(imageByte1);
	            	}
	            	
	            	if(!item.getProdImage2().equals("no")) {
	            		
	            		imageByte2=Base64.decodeBase64(item.getProdImage2());
	    				new FileOutputStream(image2).write(imageByte2);
	            	}
	            	
	            	if(!item.getProdImage3().equals("no")) {
	            		
	            		imageByte3=Base64.decodeBase64(item.getProdImage3());
	    				new FileOutputStream(image3).write(imageByte3);
	            	}
	               
				} catch (IOException e) {
					e.printStackTrace();
				}
		        
		        
        	}
        	
        	status = 0;
	    	message = "Product added successfully.";
        	
        }catch(Exception e) {
        	e.printStackTrace();
        }finally {
        	try {
				productJdbcTemplate.getDataSource().getConnection().close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	    
        log.info("Return Message "+status+"|"+message+"|"+prodId);
	    
		return status+"-"+message+"-"+prodId;
	}
	
	
	public String updateRetailerProduct(MyProduct item) {
		byte[] imageByte1 = null,imageByte2 = null,imageByte3 =null;
        String directory="";
       
        int status = 1,prodId = item.getProdId();
	    String message = "",image1,image2,image3;
	    
	    JdbcTemplate productJdbcTemplate = null;
	    
        try {
        	productJdbcTemplate = daoConnection.getDynamicDataSource(item.getDbName(),
        			item.getDbUserName(),item.getDbPassword());
        	
        	/*String sql = "SELECT COUNT(PROD_ID) FROM ret_product";
        	int count = productJdbcTemplate.queryForObject(sql,Integer.class);
        	if(count > 0) {
        		sql = "SELECT MAX(PROD_ID) FROM ret_product";
            	int maxId = productJdbcTemplate.queryForObject(sql,Integer.class);
            	prodId = maxId + 1;
        	}else {
        		prodId =  1;
        	}*/
        	
        	
        	String sql = "UPDATE ret_product SET `PROD_NAME` = ?,`PROD_CODE` = ?," + 
        			"`PROD_DESC` = ?,`PROD_MRP` = ?,`PROD_SP` = ?,`PROD_REORDER_LEVEL` = ?,`PROD_QOH` = ?,`PROD_HSN_CODE` = ?,"
        			+ "`PROD_CGST` = ?,`PROD_IGST` = ?," + 
        			"`PROD_SGST` = ?,`PROD_WARRANTY` = ?,`PROD_MFG_DATE` = ?,`PROD_EXPIRY_DATE` = ?,`PROD_MFG_BY` = ?,"
        			+ "`PROD_IMAGE_1` = ?," + 
        			"`PROD_IMAGE_2` = ?,`PROD_IMAGE_3` = ?,`UPDATED_BY` = ?,"
        			+ "`UPDATED_DATE` = now() WHERE  `PROD_ID` = ?";
        	
       
    		
    		image1 = Constants.IMAGE_URL+"/shops/"+item.getShopCode()
    		+"/products/"+prodId+"/"+"1.jpg";
    		image2 = Constants.IMAGE_URL+"/shops/"+item.getShopCode()
    		+"/products/"+prodId+"/"+"2.jpg";
    		image3 = Constants.IMAGE_URL+"/shops/"+item.getShopCode()
    		+"/products/"+prodId+"/"+"3.jpg";
    		
    			
    		 
        	productJdbcTemplate.update(sql,item.getProdName(),item.getProdCode(),
        			item.getProdDesc(),item.getProdMrp(),item.getProdSp(),item.getProdReorderLevel(),item.getProdQoh(),
        			item.getProdHsnCode(),item.getProdCgst(),item.getProdIgst(),item.getProdSgst(),item.getProdWarranty(),
        			item.getProdMfgDate(),item.getProdExpiryDate(),item.getProdMfgBy(),image1,image2,image3,
        			item.getUpdatedBy(),prodId);
        	
        	
    		
    	 
    	    File file = new File(Constants.FULL_IMAGE_PATH+"\\shops\\"+item.getShopCode()
    	    +"\\products\\"+prodId+"\\");
    	    
	        if (!file.exists()) {
	            if (file.mkdirs()) {
	                System.out.println("Directory is created!");
	            } else {
	                System.out.println("Failed to create directory!");
	            }
	        }
	        
	        image1 = Constants.FULL_IMAGE_PATH+"\\shops\\"+item.getShopCode()
    		+"\\products\\"+prodId+"\\"+"1.jpg";
    		image2 = Constants.FULL_IMAGE_PATH+"\\shops\\"+item.getShopCode()
    		+"\\products\\"+prodId+"\\"+"2.jpg";
    		image3 = Constants.FULL_IMAGE_PATH+"\\shops\\"+item.getShopCode()
    		+"\\products\\"+prodId+"\\"+"3.jpg";
	        
	        try {
            	if(!item.getProdImage1().equals("no")) {
            		
            		imageByte1=Base64.decodeBase64(item.getProdImage1());
    				new FileOutputStream(image1).write(imageByte1);
            	}
            	
            	if(!item.getProdImage2().equals("no")) {
            		
            		imageByte2=Base64.decodeBase64(item.getProdImage2());
    				new FileOutputStream(image2).write(imageByte2);
            	}
            	
            	if(!item.getProdImage3().equals("no")) {
            		
            		imageByte3=Base64.decodeBase64(item.getProdImage3());
    				new FileOutputStream(image3).write(imageByte3);
            	}
               
			} catch (IOException e) {
				e.printStackTrace();
			}
        	
        	status = 0;
	    	message = "Product updated successfully.";
        	
        }catch(Exception e) {
        	e.printStackTrace();
        }finally {
        	try {
				productJdbcTemplate.getDataSource().getConnection().close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	    
        log.info("Return Message "+status+"|"+message+"|"+prodId);
	    
		return status+"-"+message+"-"+prodId;
	}
	
	
	
	public String manageAddProduct(List<MyProduct> listItem) {
		
		byte[] imageByte1 = null,imageByte2 = null,imageByte3 =null;
        String directory="";
        
        MyProduct myItem = listItem.get(0);
		
        JdbcTemplate productJdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.PRODUCT_DB_NAME,
        		myItem.getDbUserName(),myItem.getDbPassword());
		
		int status = 1,prodId = 0;
	    String message = "";
	    String query = "{ call add_product(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }";
	    Connection conn = null;
		try {
			conn = productJdbcTemplate.getDataSource().getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(conn !=null) {
			ResultSet rs;
			//MyProduct item = listItem.get(0);
			for(MyProduct item : listItem) {
			
			    try {
			        CallableStatement stmt = conn.prepareCall(query);
			        stmt.setInt(1, item.getProdId());
			    	stmt.setInt(2, item.getProdCatId());
			    	stmt.setString(3, item.getProdName());
			    	stmt.setString(4, item.getProdCode());
			    	stmt.setString(5, item.getProdBarCode());
			    	stmt.setString(6, item.getProdDesc());
			    	stmt.setFloat(7, item.getProdMrp());
			    	stmt.setFloat(8, item.getProdSp());
			    	stmt.setInt(9, item.getProdReorderLevel());
			    	stmt.setInt(10, item.getProdQoh());
			    	stmt.setString(11, item.getProdHsnCode());
			    	stmt.setFloat(12, item.getProdCgst());
			    	stmt.setFloat(13, item.getProdIgst());
			    	stmt.setFloat(14, item.getProdSgst());
			    	stmt.setFloat(15, item.getProdWarranty());
			    	stmt.setString(16, item.getProdMfgDate());
			    	stmt.setString(17, item.getProdExpiryDate());
			    	stmt.setString(18, item.getProdMfgBy());
			    	stmt.setString(19, "");
			    	stmt.setString(20, "");
			    	stmt.setString(21, "");
			    	stmt.setString(22, item.getCreatedBy());
			    	stmt.setString(23, item.getUpdatedBy());
			    	stmt.setString(24, item.getDbName());
			    	stmt.setInt(25, item.getAction());
			    	stmt.registerOutParameter(26, java.sql.Types.INTEGER);
			    	stmt.registerOutParameter(27, java.sql.Types.VARCHAR);
			    	stmt.registerOutParameter(28, java.sql.Types.INTEGER);
			        stmt.execute();
			       // rs = stmt.getResultSet();
			        status = stmt.getInt(26);
			    	message = stmt.getString(27);
			    	prodId = stmt.getInt(28);
			    	
			    	File file = new File("C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\ShoppursImages\\products\\"+item.getProdCatId()+"\\"+
            				prodId+"\\");
			        if (!file.exists()) {
			            if (file.mkdirs()) {
			                System.out.println("Directory is created!");
			            } else {
			                System.out.println("Failed to create directory!");
			            }
			        }
			        
			      /*  file = new File("E:\\ShoppursImages\\products\\"+item.getProdCatId()+"\\"+prodId);
			        if (!file.exists()) {
			            if (file.mkdirs()) {
			                System.out.println("Directory is created!");
			            } else {
			                System.out.println("Failed to create directory!");
			            }
			        }*/
			    	
			    	try {
		            	if(!item.getProdImage1().equals("no")) {
		            		directory = "C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\ShoppursImages\\products\\"+item.getProdCatId()+"\\"+
		            				prodId+"\\prod_"+prodId+"_1.jpg";
		            		imageByte1=Base64.decodeBase64(item.getProdImage1());
		    				new FileOutputStream(directory).write(imageByte1);
		            	}
		            	
		            	if(!item.getProdImage2().equals("no")) {
		            		directory = "C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\ShoppursImages\\products\\"+item.getProdCatId()+"\\"+
		            				prodId+"\\prod_"+prodId+"_2.jpg";
		            		imageByte2=Base64.decodeBase64(item.getProdImage2());
		    				new FileOutputStream(directory).write(imageByte2);
		            	}
		            	
		            	if(!item.getProdImage3().equals("no")) {
		            		directory = "C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\ShoppursImages\\products\\"+item.getProdCatId()+"\\"+
		            				prodId+"\\prod_"+prodId+"_3.jpg";
		            		imageByte3=Base64.decodeBase64(item.getProdImage3());
		    				new FileOutputStream(directory).write(imageByte3);
		            	}
		               
					} catch (IOException e) {
						e.printStackTrace();
					}
			    	
			    	log.info("Message "+message);
			    	log.info("ProdId "+prodId);
			    } catch (SQLException ex) {
			        System.out.println(ex.getMessage());
			    }
			}
		}
		
		log.info("Return Message "+status+"|"+message+"|"+prodId);
	    
		return status+"-"+message+"-"+prodId;
	}
	
	public String addBarCode(ProductBarcode item) {
		String status = "failure";
		JdbcTemplate dynamicJdbc = daoConnection.getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		String sql="select COUNT(*) as counter from RET_PRODUCT WHERE PROD_ID = ?";
		try {
			int counter = dynamicJdbc.queryForObject(sql,Integer.class,item.getProdId());
			if(counter > 0) {
				sql="select COUNT(*) as counter from product_barcodes WHERE PROD_BARCODE = ?";
				counter = dynamicJdbc.queryForObject(sql,Integer.class,item.getProdBarCode());
				if(counter > 0) {
					status = "Product barcode is already added.";
				}else {
					
					sql = "insert into product_barcodes (`PROD_PROD_ID`,`PROD_CODE`,`PROD_BARCODE`,`SOLD_STATUS`) "
	        	    		+ "values(?,?,?,?)";
					dynamicJdbc.update(sql,item.getProdId(),item.getProdCode(),item.getProdBarCode(),"N");
					
					sql="select PROD_QOH from RET_PRODUCT WHERE PROD_ID = ?";
					counter = dynamicJdbc.queryForObject(sql,Integer.class,item.getProdId());
					counter++;
					sql="UPDATE RET_PRODUCT SET PROD_QOH = ? WHERE PROD_ID = ?";
					dynamicJdbc.update(sql,counter,item.getProdId());
					status = "success";
				}
				
			}else {
				status = "Product does not exist in database.";
			}
			
		}catch(Exception e) {
			status = "error";
			e.printStackTrace();
		}
		
		return status;
	}
	
	public List<ProductBarcode> getBarCodes(UserID item){
		List<ProductBarcode> itemList = null;
		JdbcTemplate dynamicJdbc = daoConnection.getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		String sql="select * from PRODUCT_BARCODES WHERE PROD_PROD_ID = ? AND SOLD_STATUS = ?";
		try
		   {
		     itemList=dynamicJdbc.query(sql, new ProductBarCodeMapper(),item.getId(),"N");
		      
		   }catch(Exception e)
		     {
			   log.info("Exception "+e.toString());
		    }
		
		return itemList;
	}
	
public List<MyProduct> getProductList(UserID item) {
		
		JdbcTemplate productJdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.PRODUCT_DB_NAME,
        		item.getDbUserName(),item.getDbPassword());
		
		String sql="select * from PRODUCT_MASTER";
		String barSql="select PROD_BARCODE from PRODUCT_BARCODES WHERE PROD_PROD_ID = ?";
		try
		   {
		     List<MyProduct> itemList=productJdbcTemplate.query(sql, new ProductMapper());
		       if(itemList.size() > 0) {
		    	   for(MyProduct myProduct : itemList) {
		    		   if(myProduct.getIsBarcodeAvailable().equals("Y")) {
		    			   List<Barcode> barcodeList =  productJdbcTemplate.query(barSql,new BarCodeMapper(),myProduct.getProdId());
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
	
	
public List<MyProduct> getRetailerProductList(UserID item) {

    JdbcTemplate dynamicShopJdbc = daoConnection.getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		
		String sql="select * from RET_PRODUCT WHERE PROD_SUB_CAT_ID = ?";
		String barSql="select PROD_BARCODE from PRODUCT_BARCODES WHERE PROD_PROD_ID = ?";
		try
		   {
		     List<MyProduct> itemList=dynamicShopJdbc.query(sql, new ProductMapper(),item.getId());
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
	
public MyProduct getProductDetailsById(int prodId,String dbName,String dbUserName,String dbPassword) {
		
	log.info("Details "+prodId + " "+dbName+" "+dbUserName+" "+dbPassword);
	
	String sql="select * from RET_PRODUCT WHERE PROD_ID = ?";
	String barSql="select PROD_BARCODE from PRODUCT_BARCODES WHERE PROD_PROD_ID = ?";
	JdbcTemplate dynamicJdbc = daoConnection.getDynamicDataSource(dbName,dbUserName,dbPassword);
		try
		   {
		     List<MyProduct> itemList=dynamicJdbc.query(sql, new ProductMapper(),prodId);
		       if(itemList.size() > 0) {
		    	   MyProduct myProduct = itemList.get(0);
		    	   if(myProduct.getIsBarcodeAvailable().equals("Y")) {
	    			   List<Barcode> barcodeList =  dynamicJdbc.query(barSql,new BarCodeMapper(),myProduct.getProdId());
	    			   myProduct.setBarcodeList(barcodeList);
	    		   }
			      return myProduct;
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

	public List<MyProduct> getProductList(CategoryDetailReq item) {
		
		JdbcTemplate productJdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.PRODUCT_DB_NAME,
        		item.getDbUserName(),item.getDbPassword());
	
		String sql="select * from PRODUCT_MASTER where PROD_SUB_CAT_ID IN ("+item.getIds()+")";
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
	
	
	public List<ProductSaleObject> getProductSaleData(UserID item) {
		List<ProductSaleObject> itemList = null;
		String sql="SELECT so.INVM_DATE,sum(sod.INVD_QTY),sum(sod.INVD_TAMOUNT) FROM invoice_master as so, invoice_detail as sod " + 
				" where so.INVM_ID = sod.INVD_INVM_ID and sod.INVD_PROD_ID = ? group by month(so.INVM_DATE)";
		
		JdbcTemplate dynamicJdbc = daoConnection.getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		
		try
		   {
		     itemList=dynamicJdbc.query(sql,new Object[] { item.getId() }, new ProductSaleMapper());
		   }catch(Exception e)
		     {
			   log.info("Exception "+e.toString());
		    }
		
		return itemList;
	}
	
	public ProductRating getProductRatings(UserID item) {
		ProductRating productRating = null;
		String sql = null;
		JdbcTemplate dynamicJdbc = daoConnection.getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
		
		sql="SELECT avg(ORD_RATINGS_BY_CUST),count(ORD_RATINGS_BY_CUST) FROM cust_order as co,cust_order_details as cod " + 
				"WHERE co.ORD_ID = cod.ORD_ORD_ID AND cod.PROD_ID = ?";
		
		try
		   {
			productRating=dynamicJdbc.query(sql,new ProductRatingMapper(),item.getId()).get(0);
		   }catch(Exception e)
		     {
			   log.info("Exception "+e.toString());
		    }
		
		return productRating;
	}
	

}
