package com.stream.uploadvideo;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://dockerDB:3306/docker-database?autoReconnect=true&failOverReadOnly=false&maxReconnects=10");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        return dataSource;
    }


}
