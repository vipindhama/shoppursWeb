package com.shoppurs.shop.dao;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.shoppurs.connection.DaoConnection;
import com.shoppurs.shop.controller.ShoppursApiController;
import com.shoppurs.shop.mapper.DbUserMapper;
import com.shoppurs.shop.mapper.MyUserMapper;
import com.shoppurs.shop.mapper.ProductSaleMapper;
import com.shoppurs.shop.mapper.ShopCustomerSaleMapper;
import com.shoppurs.shop.mapper.UserLicenseMapper;
import com.shoppurs.shop.model.Customer;
import com.shoppurs.shop.model.DbUser;
import com.shoppurs.shop.model.MyBank;
import com.shoppurs.shop.model.MyDataSource;
import com.shoppurs.shop.model.MyUser;
import com.shoppurs.shop.model.ProductSaleObject;
import com.shoppurs.shop.model.ShopCustomerSaleData;
import com.shoppurs.shop.model.UserID;
import com.shoppurs.shop.model.UserLicense;
import com.shoppurs.shop.model.UserLogin;
import com.shoppurs.shop.model.requestModel.ShopSaleReq;
import com.shoppurs.shop.model.requestModel.UserDetailsReq;
import com.shoppurs.utilities.Constants;

public class RetailerDao {
	
	

}
