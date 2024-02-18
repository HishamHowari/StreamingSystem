package com.stream.uploadvideo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;

@Repository
public class DAO {
    protected JdbcTemplate jdbcTemplate;

    public DAO() {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        this.jdbcTemplate = new JdbcTemplate(dataSourceConfig.getDataSource());
    }

}
