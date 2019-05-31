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
	
	

}
