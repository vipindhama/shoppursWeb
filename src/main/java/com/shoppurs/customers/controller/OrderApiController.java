package com.shoppurs.customers.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoppurs.customers.dao.OrderManagerDao;
import com.shoppurs.customers.model.MyOrder;
import com.shoppurs.customers.model.MyProduct;
import com.shoppurs.customers.model.MyResponse;
import com.shoppurs.customers.model.OrderDetail;
import com.shoppurs.customers.model.UserID;
import com.shoppurs.shop.controller.ShoppursApiController;
import com.shoppurs.shop.dao.OfferDao;
import com.shoppurs.shop.model.Category;
import com.shoppurs.shop.model.MyOffer;

@RestController("/api/order")
public class OrderApiController {
	
	

}
