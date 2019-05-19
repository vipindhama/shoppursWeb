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

import com.shoppurs.shop.controller.ShoppursApiController;
import com.shoppurs.shop.model.Category;
import com.shoppurs.shop.model.MyOffer;
import com.shoppurs.shop.model.MyProduct;

public class OfferDao {
	private static final Logger log = LoggerFactory.getLogger(ShoppursApiController.class);

	
	@Autowired
   	@Qualifier("offer-database")
    private JdbcTemplate offerJdbcTemplate;
	
	//Manage Shoppurs Offers action 1 = create offer, action 3 = get all offer, action 2 = get offer product id wise 
	public Object manageShoppursOffer(MyOffer myoffer) {
		List<MyOffer> myofferList = new ArrayList<MyOffer>();
		List<Category> itemList = new ArrayList<Category>();
		int status = 1;
	    String message = "";
	    String query = "{ call manage_shoppurs_offers(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }";
	    Connection conn = null;
		try {
			conn = offerJdbcTemplate.getDataSource().getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(conn !=null) {
			ResultSet rs;
			
			    try {
			        CallableStatement stmt = conn.prepareCall(query);
			        stmt.setInt(1, myoffer.getOfferId());
			    	stmt.setString(2, myoffer.getOfferNumber());
			    	stmt.setString(3, myoffer.getOfferName());
			    	stmt.setFloat(4, myoffer.getPercentage());
			    	stmt.setFloat(5, myoffer.getAmount());
			    	stmt.setString(6, myoffer.getStartDate());
			    	stmt.setString(7, myoffer.getEndDate());
			    	stmt.setString(8, myoffer.getStatus());
			    	stmt.setString(9, myoffer.getType());
			    	stmt.setString(10, myoffer.getCreateBy());
			    	stmt.setString(11, myoffer.getUpdatedBy());
			    	stmt.setInt(12, myoffer.getAction());
			    	stmt.setInt(13, myoffer.getProductId());
			    	stmt.setString(14, myoffer.getImage());
			    	stmt.setString(15, myoffer.getDisc());
			    	stmt.setInt(16, myoffer.getCatId());
			    	//log.info("productId "+myoffer.getProductId());
			    	stmt.registerOutParameter(17, java.sql.Types.INTEGER);
			    	stmt.registerOutParameter(18, java.sql.Types.VARCHAR);
			        stmt.execute();		     
			        status = stmt.getInt(17);
			    	message = stmt.getString(18);
			    	if(myoffer.getAction()==3 || myoffer.getAction()==2) {
			    		rs = stmt.getResultSet();
				    	log.info("rs "+rs);
				    	if(rs!=null) {
				    	while(rs.next()) {
				    		MyOffer offer = new MyOffer();
				    		offer.setOfferId(rs.getInt(1));
				    		offer.setOfferNumber(rs.getString(2));
				    		offer.setOfferName(rs.getString(3));
				    		offer.setPercentage(rs.getFloat(4));
				    		offer.setAmount(rs.getFloat(5));
				    		offer.setStartDate(rs.getString(6));
				    		offer.setEndDate(rs.getString(7));
				    		offer.setStatus(rs.getString(8));
				    		offer.setImage(rs.getString(9));
				    		offer.setDisc(rs.getString(10));				
				    		offer.setType(rs.getString(11));
				    		offer.setCreateBy(rs.getString(12));
				    		offer.setUpdatedBy(rs.getString(13));
				    		offer.setCreatedDate(rs.getString(14));
				    		offer.setUpdatedDate(rs.getString(15));
				    		
				    		myofferList.add(offer);
				    		}
			    	}return myofferList;
			    }else if(myoffer.getAction()==4) {
			    	rs = stmt.getResultSet();
			    	//log.info("rs "+rs);
			    	if(rs!=null) {
			    		while(rs.next()) {
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
			    	}
			    	
		    		
		    		return itemList;
			     }
			    }catch (SQLException ex) {
			        System.out.println(ex.getMessage());
			}
		}
	    
		return status+"|"+message;
	}
	
	
	//Manage Shop Offers
		public Object manageShopOffer(MyOffer myoffer) {
			List<MyOffer> myofferList = new ArrayList<MyOffer>();
			int status = 1;
		    String message = "";
		    String query = "{ call manage_shop_offers(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }";
		    Connection conn = null;
			try {
				conn = offerJdbcTemplate.getDataSource().getConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(conn !=null) {
				ResultSet rs;
				
				    try {
				        CallableStatement stmt = conn.prepareCall(query);
				        stmt.setInt(1, myoffer.getOfferId());
				    	stmt.setString(2, myoffer.getOfferNumber());
				    	stmt.setString(3, myoffer.getOfferName());
				    	stmt.setFloat(4, myoffer.getPercentage());
				    	stmt.setFloat(5, myoffer.getAmount());
				    	stmt.setString(6, myoffer.getStartDate());
				    	stmt.setString(7, myoffer.getEndDate());
				    	stmt.setString(8, myoffer.getStatus());
				    	stmt.setString(9, myoffer.getType());
				    	stmt.setString(10, myoffer.getCreateBy());
				    	stmt.setString(11, myoffer.getUpdatedBy());
				    	stmt.setInt(12, myoffer.getAction());
				    	stmt.setString(13, myoffer.getDbname());
				    	stmt.setInt(14, myoffer.getProductId());
				    	stmt.setString(15, myoffer.getImage());
				    	stmt.setString(16, myoffer.getDisc());
				    	stmt.setInt(17, myoffer.getCatId());
				    	//log.info("productId "+myoffer.getProductId());
				    	stmt.registerOutParameter(18, java.sql.Types.INTEGER);
				    	stmt.registerOutParameter(19, java.sql.Types.VARCHAR);
				        stmt.execute();		     
				        status = stmt.getInt(18);
				    	message = stmt.getString(19);
				    	if(myoffer.getAction()==3 || myoffer.getAction()==2) {
				    		rs = stmt.getResultSet();
					    	log.info("rs "+rs);
					    	if(rs!=null) {
					    	while(rs.next()) {
					    		MyOffer offer = new MyOffer();
					    		offer.setOfferId(rs.getInt(1));
					    		offer.setOfferNumber(rs.getString(2));
					    		offer.setOfferName(rs.getString(3));
					    		offer.setPercentage(rs.getFloat(4));
					    		offer.setAmount(rs.getFloat(5));
					    		offer.setStartDate(rs.getString(6));
					    		offer.setEndDate(rs.getString(7));
					    		offer.setStatus(rs.getString(8));
					    		offer.setImage(rs.getString(9));
					    		offer.setDisc(rs.getString(10));
					    		offer.setType(rs.getString(11));
					    		offer.setCreateBy(rs.getString(12));
					    		offer.setUpdatedBy(rs.getString(13));
					    		offer.setCreatedDate(rs.getString(14));
					    		offer.setUpdatedDate(rs.getString(15));
					    		
					    		
					    		myofferList.add(offer);
					    		}
				    	}return myofferList;
				    }
				    }catch (SQLException ex) {
				        System.out.println(ex.getMessage());
				}
			}
		    
			return status+"|"+message;
		}
		

}
