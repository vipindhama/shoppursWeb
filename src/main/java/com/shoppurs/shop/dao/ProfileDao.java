package com.shoppurs.shop.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

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
import com.shoppurs.shop.model.MyDataSource;
import com.shoppurs.shop.model.UserID;
import com.shoppurs.shop.model.requestModel.AddressReq;
import com.shoppurs.shop.model.requestModel.BasicProfileReq;
import com.shoppurs.shop.model.requestModel.DeliveryStatusReq;
import com.shoppurs.utilities.Constants;

public class ProfileDao {
	
	
	

}
