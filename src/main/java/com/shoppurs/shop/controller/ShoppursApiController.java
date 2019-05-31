package com.shoppurs.shop.controller;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoppurs.shop.dao.ShoppursDao;
import com.shoppurs.shop.model.Category;
import com.shoppurs.shop.model.Country;
import com.shoppurs.shop.model.Customer;
import com.shoppurs.shop.model.Greeting;
import com.shoppurs.shop.model.MyBank;
import com.shoppurs.shop.model.MyProduct;
import com.shoppurs.shop.model.MyResponse;
import com.shoppurs.shop.model.MyUser;
import com.shoppurs.shop.model.RetailerCategory;
import com.shoppurs.shop.model.SimpleItem;
import com.shoppurs.shop.model.SubCategory;
import com.shoppurs.shop.model.UserID;
import com.shoppurs.shop.model.UserLogin;

@RestController("/api")
public class ShoppursApiController {
    
    
}
