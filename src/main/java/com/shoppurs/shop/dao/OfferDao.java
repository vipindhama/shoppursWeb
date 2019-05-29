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

import com.shoppurs.connection.DaoConnection;
import com.shoppurs.shop.controller.ShoppursApiController;
import com.shoppurs.shop.mapper.CategoryMapper;
import com.shoppurs.shop.mapper.OfferMasterMapper;
import com.shoppurs.shop.model.Category;
import com.shoppurs.shop.model.MyOffer;
import com.shoppurs.shop.model.MyProduct;
import com.shoppurs.shop.model.OfferMaster;
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
		

}
