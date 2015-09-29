package com.yjh.cg.config;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Created by yjh on 2015/9/19.
 */
@Configuration
@EnableWebMvc
@ComponentScan(
        basePackages = "com.yjh.cg",
        useDefaultFilters = false,
        includeFilters = @ComponentScan.Filter(Controller.class)
)
@Profile("development")
@PropertySource("classpath:setting.properties")
public class ManagerControllerContextConfiguration {
//    @Value("jdbc.url")
//    private String jdbcUrl;
//    @Value("jdbc.driverClassName")
//    private String driverClassName;
//    @Value("jdbc.username")
//    private String username;
//    @Value("jdbc.password")
//    private String password;
//    @Value("dataSource.initialSize")
//    private int initialSize;
//    @Value("dataSource.maxTotal")
//    private int maxTotal;
//    @Value("dataSource.maxIdle")
//    private int maxIdle;
//    @Value("dataSource.minIdle")
//    private int minIdle;
//    @Value("dataSource.logAbandoned")
//    private boolean logAbandoned;
//    @Value("dataSource.timeBetweenEvictionRunsMillis")
//    private long timeBetweenEvictionRunsMillis;
//    @Value("dataSource.minEvictableIdleTimeMillis")
//    private long minEvictableIdleTimeMillis;
//    @Value("dataSource.removeAbandoned")
//    private boolean removeAbandoned;
//    @Value("dataSource.removeAbandonedTimeout")
//    private int removeAbandonedTimeout;
//    @Value("dataSource.maxWait")
//    private int maxWait;
//
//    //config business dataSource
//    @Bean(name = "dataSourceJDBC", destroyMethod = "close")
//    public BasicDataSource dataSource() {
//        BasicDataSource dataSource = new BasicDataSource();
//
//        dataSource.setDriverClassName(driverClassName);
//        dataSource.setUrl(jdbcUrl);
//        dataSource.setUsername(username);
//        dataSource.setPassword(password);
//        dataSource.setInitialSize(initialSize);
//        dataSource.setMaxTotal(maxTotal);
//        dataSource.setMaxIdle(maxIdle);
//        dataSource.setMinIdle(minIdle);
//        dataSource.setLogAbandoned(logAbandoned);
//        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
//        dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
//        dataSource.setRemoveAbandonedOnBorrow(removeAbandoned);
//        dataSource.setRemoveAbandonedTimeout(removeAbandonedTimeout);
//        dataSource.setMaxWaitMillis(maxWait);
//
//        return dataSource;
//    }

    @Bean
    public DataSource dataSource() throws NamingException {
        Context context = new InitialContext();
        return (DataSource)context.lookup("java:comp/env/jdbc/cg");
    }


    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer =
                new MapperScannerConfigurer();

        mapperScannerConfigurer.setBasePackage("com.*.dao,com.*.daoEx");

        return mapperScannerConfigurer;
    }
}
