package com.shoppurs.shop.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.shoppurs.connection.DaoConnection;
import com.shoppurs.shop.controller.ShoppursApiController;
import com.shoppurs.shop.mapper.CategoryMapper;
import com.shoppurs.shop.mapper.SubCatMapper;
import com.shoppurs.shop.model.Category;
import com.shoppurs.shop.model.MyDataSource;
import com.shoppurs.shop.model.RetailerCategory;
import com.shoppurs.shop.model.SubCategory;
import com.shoppurs.shop.model.UserID;
import com.shoppurs.shop.model.requestModel.CategoryDetailReq;
import com.shoppurs.shop.model.requestModel.DelCategory;
import com.shoppurs.utilities.Constants;

public class CategoryDao {
	
private static final Logger log = LoggerFactory.getLogger(ShoppursApiController.class);


    @Autowired
    private DaoConnection daoConnection;
	

public String createCategory(Category category) {
	
	String status = null;
	
	    JdbcTemplate jdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.SHOPPURS_DB_NAME,
	    		category.getDbUserName(),category.getDbPassword());
	    
	    try
	    {
	    	String sql2="select count(*) from CATEGORY_MASTER where CATEGORY_NAME=?";
			int count=jdbcTemplate.queryForObject(sql2,Integer.class,category.getCatName());
			if(count == 0) {
				
		    	String sql="insert into CATEGORY_MASTER values (null,?,?,?,?,?,now(),now())";
		    	jdbcTemplate.update(sql,category.getCatName(),category.getImageUrl(),category.getDelStatus(),category.getCreatedBy(),category.getUpdatedBy());
		    	CategoryDetailReq item = new CategoryDetailReq();
		    	item.setCatName(category.getCatName());
		    	item.setDbName(DaoConnection.SHOPPURS_DB_NAME);
		    	item.setDbPassword(category.getDbPassword());
		    	item.setDbUserName(category.getDbUserName());
		    	int id = getCategoryDetails(item).getCatId();
		    	sql="update  CATEGORY_MASTER set CAT_IMAGE = ? where CATEGORY_ID = ?";
		    	jdbcTemplate.update(sql,Constants.IMAGE_URL+"/categories/"+id+".jpg",id);
		    	status = "success";
			}else {
				status =  "Category name is already exist.";
			}
	    }catch(Exception e) {
	    	status = "Error";
	    	e.printStackTrace();
	    }finally {
	    	try {
	    		jdbcTemplate.getDataSource().getConnection().close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
		
		return status;
		
	}

public String createSubCategory(SubCategory item) {
	
	String status = "failure";
	
	JdbcTemplate jdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.SHOPPURS_DB_NAME,
			item.getDbUserName(),item.getDbPassword());
	
	try {
		String sql2="select count(*) from SUB_CATEGORY_MASTER where SUB_CATEGORY_NAME=?";
		int count=jdbcTemplate.queryForObject(sql2,Integer.class,item.getSubCatName());
		if(count == 0) {
			
	    	String sql="insert into SUB_CATEGORY_MASTER values (null,?,?,?,?,?,?,now(),now())";
	    	jdbcTemplate.update(sql,item.getSubCatName(),item.getCatId(),item.getImageUrl(),item.getDelStatus(),item.getCreatedBy(),item.getUpdatedBy());
	    	CategoryDetailReq reqItem = new CategoryDetailReq();
	    	reqItem.setCatName(item.getSubCatName());
	    	reqItem.setDbName(DaoConnection.SHOPPURS_DB_NAME);
	    	reqItem.setDbPassword(item.getDbPassword());
	    	reqItem.setDbUserName(item.getDbUserName());
	    	int id = getSubCategoryDetails(reqItem).getSubCatId();
	    	sql="update  SUB_CATEGORY_MASTER set SUB_CAT_IMAGE = ? where SUB_CATEGORY_ID = ?";
	    	jdbcTemplate.update(sql,Constants.IMAGE_URL+"/subcategories/"+id+".jpg",id);
	    	status = "success";
		}else {
			status = "SubCategory name is already exist.";
		}
	}catch(Exception e) {
		status = "error";
		e.printStackTrace();
	}finally {
		try {
    		jdbcTemplate.getDataSource().getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	return status;
}


public String createRetailerCategory(List<RetailerCategory> listItem) {
	JdbcTemplate dynamicJdbc = null;
	String  status = "failure";
	try {
		for(RetailerCategory item : listItem) {
			String sql2="select count(*) from RET_CATEGORY where CATEGORY_ID=?";
			if(dynamicJdbc == null)
				dynamicJdbc = daoConnection.getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
			int count=dynamicJdbc.queryForObject(sql2,Integer.class,item.getRetCatId());
			if(count == 0) {
				
		    	String sql="insert into RET_CATEGORY values (?,?,?,?,?,?,now(),now())";
		    	
		    	dynamicJdbc.update(sql,item.getRetCatId(),item.getCatName(),item.getImageUrl(),item.getDelStatus(),item.getCreatedBy(),item.getUpdatedBy());
		    	
		    	status = "success";
			}else {
				String sql="UPDATE ret_category SET DEL_STATUS = 'N' where CATEGORY_ID=?";
				dynamicJdbc.update(sql,item.getRetCatId());						
			}
		}
		
		status = "success";
		
	}catch(Exception e) {
		status = "error";
		e.printStackTrace();
	}finally {
		try {
			dynamicJdbc.getDataSource().getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	return status;

}

public String createRetailerSubCategory(List<RetailerCategory> listItem) {
	//RetailerCategory item = listItem.get(0);
	JdbcTemplate dynamicJdbc = null;
	String status = "failure";
	String subCatIds = null,shopId = null;
	JdbcTemplate shopJdbcTemplate = null;
	try {
		
		for(RetailerCategory item : listItem) {
			String sql2="select count(*) from RET_SUB_CATEGORY where SUB_CATEGORY_ID=?";
			
			if(dynamicJdbc == null)
			dynamicJdbc = daoConnection.getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
			if(shopJdbcTemplate == null) {
				shopJdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.SHOP_DB_NAME,
						item.getDbUserName(),item.getDbPassword());
			}
			
			int count=dynamicJdbc.queryForObject(sql2,Integer.class,item.getRetSubCatId());
			if(count == 0) {
				
				if(subCatIds == null) {
					subCatIds = ""+item.getRetSubCatId();
					shopId = item.getRetShopCode();
				}else {
					subCatIds = subCatIds+","+item.getRetSubCatId();
				}
				
		    	String sql="insert into RET_SUB_CATEGORY values (?,?,?,?,?,?,?,now(),now())";
		    	
		    	dynamicJdbc.update(sql,item.getRetSubCatId(),item.getSubCatName(),item.getRetCatId(),item.getImageUrl(),item.getDelStatus(),item.getCreatedBy(),item.getUpdatedBy());
		    	
		    	sql="insert into SUBCATEGORY_SHOPS values (?,?,?,?,?,?,?)";
		    	shopJdbcTemplate.update(sql,0,item.getRetCatId(),item.getRetSubCatId(),item.getCatName(),item.getSubCatName(),shopId,"N");
		    	status = "success";
		    	//return "success";
			}else {
				
			}
		}
		
	}catch(Exception e) {
		status = "Some error occured in adding sub category.";
		e.printStackTrace();
	}finally {
		try {
			dynamicJdbc.getDataSource().getConnection().close();
			shopJdbcTemplate.getDataSource().getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	return status;
	
}

public List<Category> getMasterCategories(UserID item) {
	
	JdbcTemplate jdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.SHOPPURS_DB_NAME,
			item.getDbUserName(),item.getDbPassword());
	
	List<Category> itemList = null;
	
	try {
		String sql="select * from CATEGORY_MASTER";
		try
		   {
		     itemList=jdbcTemplate.query(sql, new CategoryMapper());
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
	}catch(Exception e) {
		e.printStackTrace();
	}finally {
		try {
			jdbcTemplate.getDataSource().getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
		
	return itemList;
	/*String query = "{ call get_all_categories() }";
    ResultSet rs;
    List<Category> itemList = new ArrayList<Category>();
    
    try (Connection conn = jdbcTemplate.getDataSource().getConnection();
            CallableStatement stmt = conn.prepareCall(query)) {
    	//stmt.setInt(1, candidateId);
        rs = stmt.executeQuery();
        while (rs.next()) {
        	Category item = new Category();
    		item.setCatId(rs.getInt("CATEGORY_ID"));
    		item.setCatName(rs.getString("CATEGORY_NAME"));
    		item.setImageUrl(rs.getString("CAT_IMAGE"));
    		item.setDelStatus(rs.getString("DEL_STATUS"));
    		item.setCreatedBy(rs.getString("CREATED_BY"));
    		item.setUpdatedBy(rs.getString("UPDATED_BY"));
    		item.setCreatedDate(rs.getString("CREATED_DATE"));
    		item.setUpdatedDate(rs.getString("UPDATED_DATE"));
    		itemList.add(item);
        }
    } catch (SQLException ex) {
        System.out.println(ex.getMessage());
    }*/
	
	/*SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
			.withProcedureName("get_all_categories");
	
	Map<String, Object> out = simpleJdbcCall.execute();
		
	List<Category> itemList = null;*/
	
	}


public List<Category> getCategories(UserID item) {
	List<Category> itemList = null;
	String sql="select * from RET_CATEGORY";
	
	JdbcTemplate dynamicJdbc = daoConnection.getDynamicDataSource(item.getDbName(),item.getDbUserName(),item.getDbPassword());
	try
	   {
	     itemList=dynamicJdbc.query(sql, new CategoryMapper());
	       if(itemList.size() > 0) {
		      //return itemList.get(0);
	       }else {
	    	   log.info("CategoryList size is 0");
	    	  // return null;
	       }
	   }catch(Exception e)
	     {
		   log.info("Exception "+e.toString());
		   //  return null;
	    }finally {
			try {
				dynamicJdbc.getDataSource().getConnection().close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	return itemList;
}


public SubCategory getSubCategoryDetails(CategoryDetailReq item) {
	
	JdbcTemplate jdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.SHOPPURS_DB_NAME,
			item.getDbUserName(),item.getDbPassword());
	
	String sql="select * from SUB_CATEGORY_MASTER where SUB_CATEGORY_NAME=?";
	try
	   {
	     List<SubCategory> itemList=jdbcTemplate.query(sql, new SubCatMapper(),item.getCatName());
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
	    }finally {
			try {
				jdbcTemplate.getDataSource().getConnection().close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}


public List<SubCategory> getSubCategoryList(CategoryDetailReq item) {
	
	JdbcTemplate jdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.SHOPPURS_DB_NAME,
			item.getDbUserName(),item.getDbPassword());
	
	String sql="select * from SUB_CATEGORY_MASTER where SUB_CATEGORY_CAT_ID IN ("+item.getIds()+")";
	try
	   {
	     List<SubCategory> itemList=jdbcTemplate.query(sql, new SubCatMapper());
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
	    }finally {
			try {
				jdbcTemplate.getDataSource().getConnection().close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}

public Category getCategoryDetails(CategoryDetailReq item) {
	
	JdbcTemplate jdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.SHOPPURS_DB_NAME,
			item.getDbUserName(),item.getDbPassword());
	
	String sql="select * from CATEGORY_MASTER where CATEGORY_NAME=?";
	try
	   {
	     List<Category> itemList=jdbcTemplate.query(sql, new CategoryMapper(),item.getCatName());
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
	    }finally {
			try {
				jdbcTemplate.getDataSource().getConnection().close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}

public String deleteCategory(DelCategory item) {
   String status = "failure";
   
   JdbcTemplate jdbcTemplate = daoConnection.getDynamicDataSource(item.getDbName(),
			item.getDbUserName(),item.getDbPassword());
	
	String sql="UPDATE ret_category SET DEL_STATUS = 'Y' where CATEGORY_ID=?";
	
	try {
		String[] catIds = item.getCatIds().split(",");
		for(int i = 0; i< catIds.length; i++) {
			log.info("catID "+Integer.parseInt(catIds[i]));
			jdbcTemplate.update(sql,Integer.parseInt(catIds[i]));
		}
		
		status = "success";
	}catch(Exception e) {
		e.printStackTrace();
		status = "error";
	}
	
   return status;
}

}
