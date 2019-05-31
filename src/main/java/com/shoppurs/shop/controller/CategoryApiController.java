package com.shoppurs.shop.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoppurs.connection.DaoConnection;
import com.shoppurs.shop.dao.CategoryDao;
import com.shoppurs.shop.dao.ShoppursDao;
import com.shoppurs.shop.model.Category;
import com.shoppurs.shop.model.MyResponse;
import com.shoppurs.shop.model.RetailerCategory;
import com.shoppurs.shop.model.SubCategory;
import com.shoppurs.shop.model.UserID;
import com.shoppurs.shop.model.requestModel.CategoryDetailReq;
import com.shoppurs.shop.model.requestModel.DelCategory;

@RestController("/api/categories")
public class CategoryApiController {
	
private static final Logger log = LoggerFactory.getLogger(ShoppursApiController.class);
    
  

}
