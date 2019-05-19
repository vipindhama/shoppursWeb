package com.shoppurs.customers.configration;

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
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.shoppurs.customers.dao.CartManagerDao;
import com.shoppurs.customers.dao.OrderManagerDao;
import com.shoppurs.customers.dao.ProductManagerDao;
import com.shoppurs.customers.dao.ProductReviewManagerDao;
import com.shoppurs.customers.dao.SearchDao;
import com.shoppurs.customers.dao.ShopManagerDao;
import com.shoppurs.customers.dao.ShoppursCustomerDao;
import com.shoppurs.customers.model.MyDataSource;
import com.shoppurs.customers.model.MyResponse;

@Configuration
public class ShoppursConfigration {
	
	@Autowired
    private Environment env;

	/*@Bean
	@Qualifier("first")
    MyResponse first() {
        return new MyResponse();
    }*/
	
	@Bean
	@Qualifier("customer_firstDao")
	ShoppursCustomerDao custoemrdao() {
        return new ShoppursCustomerDao();
    }
	
	@Bean
	@Qualifier("order_manager_dao")
	OrderManagerDao orderManagerdao() {
        return new OrderManagerDao();
    }
	
	@Bean
	@Qualifier("cart_manager_dao")
	CartManagerDao cartManagerDao() {
        return new CartManagerDao();
    }
    
    @Bean
	@Qualifier("product_review_manager_dao")
    ProductReviewManagerDao productReviewManagerDao() {
        return new ProductReviewManagerDao();
    }
    
    @Bean
	@Qualifier("shop_manager_dao")
    ShopManagerDao shopManagerDao() {
        return new ShopManagerDao();
    }
    
    @Bean
	@Qualifier("product_manager_dao")
    ProductManagerDao productManagerDao() {
        return new ProductManagerDao();
    }
    
    @Bean
	@Qualifier("search_manager_dao")
    SearchDao searchDao() {
        return new SearchDao();
    }

	
	/*
	 * @Bean(name = "customer_localDatabase")
	 * 
	 * @Primary
	 * 
	 * @ConfigurationProperties(prefix="spring.datasource") public DataSource
	 * primaryDataSource() { return DataSourceBuilder.create().build(); }
	 * 
	 * @Bean(name = "master-datasource")
	 * // @ConfigurationProperties(prefix="spring.remote-datasource") public
	 * DataSource masterDataSource() { DriverManagerDataSource dataSource = new
	 * DriverManagerDataSource(); dataSource.setDriverClassName(env.getProperty(
	 * "spring.remote-datasource.driverClassName"));
	 * dataSource.setUrl(env.getProperty("spring.master-datasource.url"));
	 * dataSource.setUsername(env.getProperty("spring.remote-datasource.username"));
	 * dataSource.setPassword(env.getProperty("spring.remote-datasource.password"));
	 * return dataSource; // return DataSourceBuilder.create().build(); }
	 * 
	 * @Bean(name = "master-database")
	 * 
	 * @Autowired public JdbcTemplate
	 * masterJdbcTemplate(@Qualifier("master-datasource") DataSource dsSlave) {
	 * return new JdbcTemplate(dsSlave); }
	 * 
	 * @Bean(name = "auth-datasource")
	 * // @ConfigurationProperties(prefix="spring.remote-datasource") public
	 * DataSource authDataSource() { DriverManagerDataSource dataSource = new
	 * DriverManagerDataSource(); dataSource.setDriverClassName(env.getProperty(
	 * "spring.remote-datasource.driverClassName"));
	 * dataSource.setUrl(env.getProperty("spring.auth-datasource.url"));
	 * dataSource.setUsername(env.getProperty("spring.remote-datasource.username"));
	 * dataSource.setPassword(env.getProperty("spring.remote-datasource.password"));
	 * return dataSource; // return DataSourceBuilder.create().build(); }
	 * 
	 * @Bean(name = "auth-database")
	 * 
	 * @Autowired public JdbcTemplate authJdbcTemplate(@Qualifier("auth-datasource")
	 * DataSource dsSlave) { return new JdbcTemplate(dsSlave); }
	 * 
	 * 
	 * @Bean(name = "product-datasource")
	 * // @ConfigurationProperties(prefix="spring.remote-datasource") public
	 * DataSource productDataSource() { DriverManagerDataSource dataSource = new
	 * DriverManagerDataSource(); dataSource.setDriverClassName(env.getProperty(
	 * "spring.remote-datasource.driverClassName"));
	 * dataSource.setUrl(env.getProperty("spring.product-datasource.url"));
	 * dataSource.setUsername(env.getProperty("spring.remote-datasource.username"));
	 * dataSource.setPassword(env.getProperty("spring.remote-datasource.password"));
	 * return dataSource; // return DataSourceBuilder.create().build(); }
	 * 
	 * @Bean(name = "product-database")
	 * 
	 * @Autowired public JdbcTemplate
	 * productJdbcTemplate(@Qualifier("product-datasource") DataSource dsSlave) {
	 * return new JdbcTemplate(dsSlave); }
	 */
	
	
	@Bean(name = "customer-datasource")
//	@ConfigurationProperties(prefix="spring.remote-datasource")
	public DataSource customerDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName(env.getProperty("spring.remote-datasource.driverClassName"));
	    dataSource.setUrl(env.getProperty("spring.customer-datasource.url"));
	    dataSource.setUsername(env.getProperty("spring.remote-datasource.username"));
	    dataSource.setPassword(env.getProperty("spring.remote-datasource.password"));
	    return dataSource;	           						
	   // return DataSourceBuilder.create().build();
	}	
	
	@Bean(name = "customer-database")
	@Autowired
	public JdbcTemplate customerJdbcTemplate(@Qualifier("customer-datasource") DataSource dsSlave) {
	    return new JdbcTemplate(dsSlave);
	}
	
	@Bean(name = "transaction-datasource")
//	@ConfigurationProperties(prefix="spring.remote-datasource")
	public DataSource transactionDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName(env.getProperty("spring.remote-datasource.driverClassName"));
	    dataSource.setUrl(env.getProperty("spring.transaction-datasource.url"));
	    dataSource.setUsername(env.getProperty("spring.remote-datasource.username"));
	    dataSource.setPassword(env.getProperty("spring.remote-datasource.password"));
	    return dataSource;	           						
	   // return DataSourceBuilder.create().build();
	}	
	
	@Bean(name = "transaction-database")
	@Autowired
	public JdbcTemplate transactionJdbcTemplate(@Qualifier("transaction-datasource") DataSource dsSlave) {
	    return new JdbcTemplate(dsSlave);
	}
	
	@Bean
	MyDataSource dynamicdatasource() {
        return new MyDataSource();
    }
	
}