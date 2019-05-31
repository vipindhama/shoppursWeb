package com.shoppurs.shop.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import com.shoppurs.connection.DaoConnection;
import com.shoppurs.shop.controller.ShoppursApiController;
import com.shoppurs.shop.mapper.CategoryMapper;
import com.shoppurs.shop.mapper.CouponMapper;
import com.shoppurs.shop.mapper.OfferMasterMapper;
import com.shoppurs.shop.mapper.ProductComboOfferDetailMapper;
import com.shoppurs.shop.mapper.ProductComboOfferMapper;
import com.shoppurs.shop.mapper.ProductDiscountOfferMapper;
import com.shoppurs.shop.mapper.ProductPriceOfferDetailMapper;
import com.shoppurs.shop.mapper.ProductPriceOfferMapper;
import com.shoppurs.shop.model.Category;
import com.shoppurs.shop.model.Coupon;
import com.shoppurs.shop.model.MyOffer;
import com.shoppurs.shop.model.MyProduct;
import com.shoppurs.shop.model.OfferMaster;
import com.shoppurs.shop.model.ProductComboDetails;
import com.shoppurs.shop.model.ProductComboOffer;
import com.shoppurs.shop.model.ProductDiscountOffer;
import com.shoppurs.shop.model.UserID;

public class OfferDao {
	private static final Logger log = LoggerFactory.getLogger(ShoppursApiController.class);
	
	@Autowired
    private DaoConnection daoConnection;
		
	
	public List<OfferMaster> getMasterOffers(UserID item) {
		JdbcTemplate jdbcTemplate = daoConnection.getDynamicDataSource(DaoConnection.OFFERS_DB_NAME,
				item.getDbUserName(),item.getDbPassword());
		
		List<OfferMaster> offerMasterList = new ArrayList();
		
		String sql="select * from shoppurs_offer_master WHERE DEL_STATUS = 'N'";
		
		try{
			
			offerMasterList=jdbcTemplate.query(sql, new OfferMasterMapper());
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		return offerMasterList;
		
	}
	
	public Map<String,Object> getAllOffers(UserID item) {
		JdbcTemplate jdbcTemplate = daoConnection.getDynamicDataSource(item.getDbName(),
				item.getDbUserName(),item.getDbPassword());
		
		Map<String,Object> map = new HashMap();
		map.put("freeOfferList",getProductDiscountOffer(item));
		map.put("comboOfferList",getProductComboOffer(item));
		map.put("priceOfferList",getProductPriceOffer(item));
		return map;
		
	}
	
	
	public List<ProductDiscountOffer> getProductDiscountOffer(UserID item) {
		JdbcTemplate jdbcTemplate = daoConnection.getDynamicDataSource(item.getDbName(),
				item.getDbUserName(),item.getDbPassword());
		
		List<ProductDiscountOffer> offerMasterList = new ArrayList();
		
		String sql="select * from prod_disc_offer WHERE PDO_STATUS = '1'";
		
		try{
			
			offerMasterList=jdbcTemplate.query(sql, new ProductDiscountOfferMapper());
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		return offerMasterList;
		
	}
	
	public List<ProductComboOffer> getProductComboOffer(UserID item) {
		JdbcTemplate jdbcTemplate = daoConnection.getDynamicDataSource(item.getDbName(),
				item.getDbUserName(),item.getDbPassword());
		
		List<ProductComboOffer> offerMasterList = new ArrayList();
		
		String sql="select * from prod_combo_offer WHERE PCO_STATUS = '1'";
		String detailSql="select * from prod_combo_offer_details WHERE PCOD_PCO_ID = ?";
		
		try{
			
			offerMasterList=jdbcTemplate.query(sql, new ProductComboOfferMapper());
			
			for(ProductComboOffer productComboOffer : offerMasterList) {
				productComboOffer.setProductComboOfferDetails(jdbcTemplate.query(detailSql, 
						new ProductComboOfferDetailMapper(),productComboOffer.getId()));
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		return offerMasterList;
		
	}
	
	
	public List<ProductComboOffer> getProductPriceOffer(UserID item) {
		JdbcTemplate jdbcTemplate = daoConnection.getDynamicDataSource(item.getDbName(),
				item.getDbUserName(),item.getDbPassword());
		
		List<ProductComboOffer> offerMasterList = new ArrayList();
		
		String sql="select * from prod_price_offer WHERE PPO_STATUS = '1'";
		String detailSql="select * from prod_price_offer_details WHERE PPOD_PPO_ID = ?";
		
		try{
			
			offerMasterList=jdbcTemplate.query(sql, new ProductPriceOfferMapper());
			
			for(ProductComboOffer productComboOffer : offerMasterList) {
				productComboOffer.setProductComboOfferDetails(jdbcTemplate.query(detailSql, 
						new ProductPriceOfferDetailMapper(),productComboOffer.getId()));
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		return offerMasterList;
		
	}
	
	
	
	public List<Coupon> getCouponOffers(UserID item) {
		JdbcTemplate jdbcTemplate = daoConnection.getDynamicDataSource(item.getDbName(),
				item.getDbUserName(),item.getDbPassword());
		
		List<Coupon> offerMasterList = new ArrayList();
		
		String sql="select * from coupon_offer WHERE COP_STATUS = '1'";
		
		try{
			
			offerMasterList=jdbcTemplate.query(sql, new CouponMapper());
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		return offerMasterList;
		
	}
	
	public ProductDiscountOffer createProductDiscountOffer(ProductDiscountOffer item) {
		
		JdbcTemplate jdbcTemplate = daoConnection.getDynamicDataSource(item.getDbName(),
				item.getDbUserName(),item.getDbPassword());
		
		try {
			
			String sql = "INSERT INTO `prod_disc_offer`" + 
					"(`PDO_ID`," + 
					"`PDO_NAME`," + 
					"`PDO_PRD_ID_BUY`," + 
					"`PDO_PRD_ID_FREE`," + 
					"`PDO_PRD_BUY_QTY`," + 
					"`PDO_PRD_FREE_QTY`," + 
					"`PDO_START_DATE`," + 
					"`PDO_END_DATE`," + 
					"`PDO_STATUS`," + 
					"`CREATED_BY`," + 
					"`UPDATED_BY`," + 
					"`CREATED_DATE`," + 
					"`UPDATED_DATE`)" + 
					"VALUES\r\n" + 
					"(0,?,?,?,?,?,?,?,?,?,?,now(),now())";
			
			jdbcTemplate.update(sql,item.getOfferName(),item.getProdBuyId(),item.getProdFreeId(),item.getProdBuyQty(),
					item.getProdFreeQty(),item.getStartDate(),item.getEndDate(),item.getStatus(),item.getUserName(),item.getUserName());
			
			
		    sql="select max(PDO_ID) from prod_disc_offer";
		    item.setId(jdbcTemplate.queryForObject(sql, Integer.class));
		    return item;
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;	
		}
		
	}
	
	
	
public ProductComboOffer createProductComboOffer(ProductComboOffer item) {
		
		JdbcTemplate jdbcTemplate = daoConnection.getDynamicDataSource(item.getDbName(),
				item.getDbUserName(),item.getDbPassword());
		
		try {
			
			String sql = "INSERT INTO `prod_combo_offer`" + 
					"(`PCO_ID`,\r\n" + 
					"`PCO_NAME`,\r\n" + 
					"`PCO_START_DATE`,\r\n" + 
					"`PCO_END_DATE`,\r\n" + 
					"`PCO_STATUS`,\r\n" + 
					"`CREATED_BY`,\r\n" + 
					"`UPDATED_BY`,\r\n" + 
					"`CREATED_DATE`,\r\n" + 
					"`UPDATED_DATE`) " + 
					"VALUES" + 
					"(0,?,?,?,?,?,?,now(),now())";
			
			String detailSql = "INSERT INTO `prod_combo_offer_details`" + 
					"(`PCOD_ID`,\r\n" + 
					"`PCOD_PCO_ID`,\r\n" + 
					"`PCOD_PROD_ID`,\r\n" + 
					"`PCOD_PROD_QTY`,\r\n" + 
					"`PCOD_PROD_PRICE`,\r\n" + 
					"`PCOD_STATUS`,\r\n" + 
					"`CREATED_BY`,\r\n" + 
					"`UPDATED_BY`,\r\n" + 
					"`CREATED_DATE`,\r\n" + 
					"`UPDATED_DATE`) " + 
					 "VALUES" + 
					"(0,?,?,?,?,?,?,?,now(),now())";
			
			jdbcTemplate.update(sql,item.getOfferName(),item.getStartDate(),item.getEndDate(),item.getStatus(),
					item.getUserName(),item.getUserName());
			
			sql="select max(PCO_ID) from prod_combo_offer";
		    item.setId(jdbcTemplate.queryForObject(sql, Integer.class));
			
			List<ProductComboDetails> productComboOfferDetailsList = item.getProductComboOfferDetails();
			
			sql="select max(PCOD_ID) from prod_combo_offer_details";
			
			for(ProductComboDetails productComboDetails : productComboOfferDetailsList) {
				
				jdbcTemplate.update(detailSql,item.getId(),productComboDetails.getPcodProdId(),
						productComboDetails.getPcodProdQty(),productComboDetails.getPcodPrice(),productComboDetails.getStatus(),
						item.getUserName(),item.getUserName());
				
				
				productComboDetails.setId(jdbcTemplate.queryForObject(sql, Integer.class));
				productComboDetails.setPcodPcoId(item.getId());
			    
			}
			
		    return item;
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;	
		}
		
	}


public ProductComboOffer createProductPriceOffer(ProductComboOffer item) {
	
	JdbcTemplate jdbcTemplate = daoConnection.getDynamicDataSource(item.getDbName(),
			item.getDbUserName(),item.getDbPassword());
	
	try {
		
		String sql = "INSERT INTO `prod_price_offer`\r\n" + 
				"(`PPO_ID`,\r\n" + 
				"`PPO_PROD_ID`,\r\n" + 
				"`PPO_NAME`,\r\n" + 
				"`PPO_START_DATE`,\r\n" + 
				"`PPO_END_DATE`,\r\n" + 
				"`PPO_STATUS`,\r\n" + 
				"`CREATED_BY`,\r\n" + 
				"`UPDATED_BY`,\r\n" + 
				"`CREATED_DATE`,\r\n" + 
				"`UPDATED_DATE`)\r\n" + 
				"VALUES\r\n" + 
				"(0,?,?,?,?,?,?,?,now(),now())";
		
		String detailSql = "INSERT INTO `prod_price_offer_details`\r\n" + 
				"(`PPOD_ID`,\r\n" + 
				"`PPOD_PPO_ID`,\r\n" + 
				"`PPOD_PROD_QTY`,\r\n" + 
				"`PPOD_PROD_PRICE`,\r\n" + 
				"`PPOD_STATUS`,\r\n" + 
				"`CREATED_BY`,\r\n" + 
				"`UPDATED_BY`,\r\n" + 
				"`CREATED_DATE`,\r\n" + 
				"`UPDATED_DATE`)\r\n" + 
				"VALUES\r\n" + 
				"(0,?,?,?,?,?,?,now(),now())";
		
		jdbcTemplate.update(sql,item.getProdId(),item.getOfferName(),item.getStartDate(),item.getEndDate(),item.getStatus(),
				item.getUserName(),item.getUserName());
		
		sql="select max(PPO_ID) from prod_price_offer";
	    item.setId(jdbcTemplate.queryForObject(sql, Integer.class));
		
		List<ProductComboDetails> productComboOfferDetailsList = item.getProductComboOfferDetails();
		
		sql="select max(PPOD_ID) from prod_price_offer_details";
		
		for(ProductComboDetails productComboDetails : productComboOfferDetailsList) {
			
			jdbcTemplate.update(detailSql,item.getId(),
					productComboDetails.getPcodProdQty(),productComboDetails.getPcodPrice(),productComboDetails.getStatus(),
					item.getUserName(),item.getUserName());
			
			
			productComboDetails.setId(jdbcTemplate.queryForObject(sql, Integer.class));
			productComboDetails.setPcodPcoId(item.getId());
		    
		}
		
	    return item;
		
	}catch(Exception e) {
		e.printStackTrace();
		return null;	
	}
	
  }



public Coupon createCouponOffer(Coupon item) {
	
	JdbcTemplate jdbcTemplate = daoConnection.getDynamicDataSource(item.getDbName(),
			item.getDbUserName(),item.getDbPassword());
	
	try {
		
		String sql = "INSERT INTO `coupon_offer`\r\n" + 
				"(`COP_ID`,\r\n" + 
				"`COP_NAME`,\r\n" + 
				"`COP_PER`,\r\n" + 
				"`COP_AMOUNT`,\r\n" + 
				"`COP_STATUS`,\r\n" + 
				"`COP_START_DATE`,\r\n" + 
				"`COP_END_DATE`,\r\n" + 
				"`CREATED_BY`,\r\n" + 
				"`UPDATED_BY`,\r\n" + 
				"`CREATED_DATE`,\r\n" + 
				"`UPDATED_DATE`)\r\n" + 
				"VALUES\r\n" + 
				"(0,?,?,?,?,?,?,?,?,now(),now());";
		
		jdbcTemplate.update(sql,item.getName(),item.getPercentage(),item.getAmount(),
				item.getStatus(),item.getStartDate(),item.getEndDate(),item.getUserName(),item.getUserName());
		
		
	    sql="select max(COP_ID) from coupon_offer";
	    item.setId(jdbcTemplate.queryForObject(sql, Integer.class));
	    return item;
		
	}catch(Exception e) {
		e.printStackTrace();
		return null;	
	}
	
}


public ProductDiscountOffer updateProductDiscountOffer(ProductDiscountOffer item) {
	
	JdbcTemplate jdbcTemplate = daoConnection.getDynamicDataSource(item.getDbName(),
			item.getDbUserName(),item.getDbPassword());
	
	try {
		
		String sql = "UPDATE `prod_disc_offer` SET " + 
				"`PDO_NAME` = ?," + 
				"`PDO_PRD_ID_BUY` = ?," + 
				"`PDO_PRD_ID_FREE` = ?," + 
				"`PDO_PRD_BUY_QTY` = ?," + 
				"`PDO_PRD_FREE_QTY` = ?," + 
				"`PDO_START_DATE` = ?," + 
				"`PDO_END_DATE` = ?," + 
				"`PDO_STATUS` = ?," + 
				"`UPDATED_BY` = ?," + 
				"`UPDATED_DATE` = now() WHERE PDO_ID = ?"; 
				
		
		jdbcTemplate.update(sql,item.getOfferName(),item.getProdBuyId(),item.getProdFreeId(),item.getProdBuyQty(),
				item.getProdFreeQty(),item.getStartDate(),item.getEndDate(),item.getStatus(),item.getUserName(),item.getId());
		
		
	    sql="select max(PDO_ID) from prod_disc_offer";
	    item.setId(jdbcTemplate.queryForObject(sql, Integer.class));
	    return item;
		
	}catch(Exception e) {
		e.printStackTrace();
		return null;	
	}
	
}

}
