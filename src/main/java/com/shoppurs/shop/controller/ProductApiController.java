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

import com.shoppurs.customers.model.MyCart;
import com.shoppurs.shop.dao.ProductDao;
import com.shoppurs.shop.dao.ShoppursDao;
import com.shoppurs.shop.model.MyProduct;
import com.shoppurs.shop.model.MyResponse;
import com.shoppurs.shop.model.ProductBarcode;
import com.shoppurs.shop.model.ProductRating;
import com.shoppurs.shop.model.ProductSaleObject;
import com.shoppurs.shop.model.UserID;
import com.shoppurs.shop.model.requestModel.CategoryDetailReq;
import com.shoppurs.shop.model.requestModel.DelCategory;
import com.shoppurs.shop.model.requestModel.DelProductReq;
import com.shoppurs.shop.model.requestModel.ProductRatingReq;

@RestController("/api/products")
public class ProductApiController {
	
	

}
