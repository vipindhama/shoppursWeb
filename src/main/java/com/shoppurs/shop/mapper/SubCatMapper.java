package com.shoppurs.shop.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.shoppurs.shop.model.Category;
import com.shoppurs.shop.model.SubCategory;

public class SubCatMapper implements RowMapper<SubCategory>{

	@Override
	public SubCategory mapRow(ResultSet rs, int arg1) throws SQLException {
		SubCategory item = new SubCategory();
		item.setSubCatId(rs.getInt("SUB_CATEGORY_ID"));
		item.setCatId(rs.getInt("SUB_CATEGORY_CAT_ID"));
		item.setSubCatName(rs.getString("SUB_CATEGORY_NAME"));
		item.setImageUrl(rs.getString("SUB_CAT_IMAGE"));
		item.setDelStatus(rs.getString("DEL_STATUS"));
		item.setCreatedBy(rs.getString("CREATED_BY"));
		item.setUpdatedBy(rs.getString("UPDATED_BY"));
		item.setCreatedDate(rs.getString("CREATED_DATE"));
		item.setUpdatedDate(rs.getString("UPDATED_DATE"));
		return item;
	}

}
