package com.shoppurs.shop.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.shoppurs.connection.DaoConnection;
import com.shoppurs.shop.dao.CategoryDao;
import com.shoppurs.shop.dao.OfferDao;
import com.shoppurs.shop.dao.OrderDao;
import com.shoppurs.shop.dao.ProductDao;
import com.shoppurs.shop.dao.ProfileDao;
import com.shoppurs.shop.dao.RetailerDao;
import com.shoppurs.shop.dao.ShopCustomerDao;
import com.shoppurs.shop.dao.ShoppursDao;
import com.shoppurs.shop.dao.ShoppursDeviceOrderDao;
import com.shoppurs.shop.dao.TransactionDao;
import com.shoppurs.shop.model.MyBank;
import com.shoppurs.shop.model.MyDataSource;
import com.shoppurs.shop.model.MyResponse;
import com.shoppurs.response.GenerateResponse;

@Configuration
public class ShoppursConfiguration {
	
	@Autowired
    private Environment env;

	/*@Bean
	@Qualifier("first")
    MyResponse first() {
        return new MyResponse();
    }*/
	
	@Bean
	@Qualifier("firstDao")
	ShoppursDao dao() {
        return new ShoppursDao();
    }
	
	@Bean
	@Qualifier("productDao")
	ProductDao productDao() {
        return new ProductDao();
    }
	
	@Bean
	@Qualifier("retailerDao")
	RetailerDao retailerDao() {
        return new RetailerDao();
    }
    
    @Bean
	@Qualifier("categoryDao")
    CategoryDao categoryDao() {
        return new CategoryDao();
    }
	
	@Bean(name = "dynamic-datasource")
	MyDataSource myDynamicDB() {
        return new MyDataSource();
    }
	
	
	@Bean
	@Qualifier("offerDao")
    OfferDao offerDao() {
        return new OfferDao();
    }
	
	@Bean
	@Qualifier("shopCustomerDao")
	ShopCustomerDao shopCustomerDao() {
        return new ShopCustomerDao();
    }
	
	@Bean
	@Qualifier("shopOrderDao")
	OrderDao orderDao() {
        return new OrderDao();
    }
	
	@Bean
	@Qualifier("shopProfileDao")
	ProfileDao profileDao() {
        return new ProfileDao();
    }
	
	@Bean
	@Qualifier("daoConnection")
	DaoConnection daoConnection() {
        return new DaoConnection();
    }
	
	@Bean
	@Qualifier("generateResponse")
	GenerateResponse generateResponse() {
        return new GenerateResponse();
    }
	
	@Bean
	@Qualifier("transactionDao")
	TransactionDao transactionDao() {
        return new TransactionDao();
    }
	
	@Bean
	@Qualifier("shoppursDeviceOrderDao")
	ShoppursDeviceOrderDao shoppursDeviceOrderDao() {
        return new ShoppursDeviceOrderDao();
    }
	
	/*@Bean(name = "dynamic-database")
	@Autowired
	public JdbcTemplate dynamicJdbcTemplate(@Qualifier("dynamic-datasource") DataSource dsSlave) {
	    return new JdbcTemplate(dsSlave);
	}
	
	/*@Bean(name = "master-database")
	@Primary
	@ConfigurationProperties(prefix="spring.datasource")
	public DataSource primaryDataSource() {
	    return DataSourceBuilder.create().build();
	}*/
	
	@Bean(name = "master-datasource")
//	@ConfigurationProperties(prefix="spring.remote-datasource")
	public DataSource masterDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));
	    dataSource.setUrl(env.getProperty("spring.datasource.url"));
	    dataSource.setUsername(env.getProperty("spring.datasource.username"));
	    dataSource.setPassword(env.getProperty("spring.datasource.password"));
	    return dataSource;
	   // return DataSourceBuilder.create().build();
	}
	
	@Bean(name = "master-database")
	@Autowired
	public JdbcTemplate masterJdbcTemplate(@Qualifier("master-datasource") DataSource dsSlave) {
	    return new JdbcTemplate(dsSlave);
	}

	@Bean(name = "auth-datasource")
//	@ConfigurationProperties(prefix="spring.remote-datasource")
	public DataSource authDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName(env.getProperty("spring.authdatasource.driverClassName"));
	    dataSource.setUrl(env.getProperty("spring.authdatasource.url"));
	    dataSource.setUsername(env.getProperty("spring.authdatasource.username"));
	    dataSource.setPassword(env.getProperty("spring.authdatasource.password"));
	    return dataSource;
	   // return DataSourceBuilder.create().build();
	}
	
	@Bean(name = "auth-database")
	@Autowired
	public JdbcTemplate authJdbcTemplate(@Qualifier("auth-datasource") DataSource dsSlave) {
	    return new JdbcTemplate(dsSlave);
	}
	
	@Bean(name = "shop-datasource")
//	@ConfigurationProperties(prefix="spring.remote-datasource")
	public DataSource shopDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName(env.getProperty("spring.shopdatasource.driverClassName"));
	    dataSource.setUrl(env.getProperty("spring.shopdatasource.url"));
	    dataSource.setUsername(env.getProperty("spring.shopdatasource.username"));
	    dataSource.setPassword(env.getProperty("spring.shopdatasource.password"));
	    return dataSource;
	   // return DataSourceBuilder.create().build();
	}
	
	@Bean(name = "shop-database")
	@Autowired
	public JdbcTemplate shopJdbcTemplate(@Qualifier("shop-datasource") DataSource dsSlave) {
	    return new JdbcTemplate(dsSlave);
	}
	
	@Bean(name = "product-datasource")
//	@ConfigurationProperties(prefix="spring.remote-datasource")
	public DataSource productDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName(env.getProperty("spring.productdatasource.driverClassName"));
	    dataSource.setUrl(env.getProperty("spring.productdatasource.url"));
	    dataSource.setUsername(env.getProperty("spring.productdatasource.username"));
	    dataSource.setPassword(env.getProperty("spring.productdatasource.password"));
	    return dataSource;
	   // return DataSourceBuilder.create().build();
	}
	
	@Bean(name = "product-database")
	@Autowired
	public JdbcTemplate productJdbcTemplate(@Qualifier("product-datasource") DataSource dsSlave) {
	    return new JdbcTemplate(dsSlave);
	}
	
	@Bean(name = "offer-datasource")
//	@ConfigurationProperties(prefix="spring.remote-datasource")
	public DataSource offerDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName(env.getProperty("spring.offerdatasource.driverClassName"));
	    dataSource.setUrl(env.getProperty("spring.offerdatasource.url"));
	    dataSource.setUsername(env.getProperty("spring.offerdatasource.username"));
	    dataSource.setPassword(env.getProperty("spring.offerdatasource.password"));
	    return dataSource;
	   // return DataSourceBuilder.create().build();
	}
	
	@Bean(name = "offer-database")
	@Autowired
	public JdbcTemplate offerJdbcTemplate(@Qualifier("offer-datasource") DataSource dsSlave) {
	    return new JdbcTemplate(dsSlave);
	}

	
}
