package com.wjjj.springboot.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 *@author WJJJ
 *@createTime 2019/7/16 20:52
 *@description 数据库配置类
 */

@Configuration
public class DataSourceConfig {

    @Bean(name = "oracleDataSource")
    @Qualifier("oracleDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.oracle")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mssqlDataSource")
    @Qualifier("mssqlDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.mssql")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "oracleJdbcTemplate")
    public JdbcTemplate primaryJdbcTemplate(@Qualifier("oracleDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "mssqlJdbcTemplate")
    public JdbcTemplate secondaryJdbcTemplate(@Qualifier("mssqlDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
