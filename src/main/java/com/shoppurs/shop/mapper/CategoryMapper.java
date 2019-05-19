package com.shoppurs.shop.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.shop.model.Category;

public class CategoryMapper implements RowMapper<Category>{

	@Override
	public Category mapRow(ResultSet rs, int arg1) throws SQLException {
		Category item = new Category();
		
		try {
			item.setCatId(rs.getInt("CATEGORY_ID"));
			item.setCatName(rs.getString("CATEGORY_NAME"));
			item.setImageUrl(rs.getString("CAT_IMAGE"));
			item.setDelStatus(rs.getString("DEL_STATUS"));
			item.setCreatedBy(rs.getString("CREATED_BY"));
			item.setUpdatedBy(rs.getString("UPDATED_BY"));
			item.setCreatedDate(rs.getString("CREATED_DATE"));
			item.setUpdatedDate(rs.getString("UPDATED_DATE"));
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return item;
	}

}
