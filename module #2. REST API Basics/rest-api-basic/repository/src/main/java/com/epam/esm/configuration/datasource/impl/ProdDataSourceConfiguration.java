package com.epam.esm.configuration.datasource.impl;

import com.epam.esm.configuration.datasource.DataSourceConfiguration;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Objects;

@Slf4j
@Configuration
@Profile("prod")
@ComponentScan("com.epam.esm")
@PropertySource("classpath:application-prod.properties")
public class ProdDataSourceConfiguration implements DataSourceConfiguration {

    private static final String DRIVER = "spring.datasource.mysql.driver";
    private static final String URL = "spring.datasource.mysql.url";
    private static final String USER = "spring.datasource.mysql.user";
    private static final String PASSWORD = "spring.datasource.mysql.password";
    private final Environment environment;

    public ProdDataSourceConfiguration(Environment environment) {
        this.environment = environment;
    }

    @Override
    @Bean
    @Primary
    public HikariDataSource getDataSource() {
        HikariDataSource dataSource;
        HikariConfig config = new HikariConfig();
        log.debug("HikariConfig for PROD Profile created");

        config.setDriverClassName(Objects.requireNonNull(environment.getProperty(DRIVER)));
        config.setJdbcUrl(Objects.requireNonNull(environment.getProperty(URL)));
        config.setUsername(Objects.requireNonNull(environment.getProperty(USER)));
        config.setPassword(Objects.requireNonNull(environment.getProperty(PASSWORD)));
        config.setMaximumPoolSize(100);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);
        log.debug("HikariDataSource for PROD Profile created");

        return dataSource;
    }

    @Bean
    @Primary
    public JdbcTemplate getJDBCTemplate() {
        return new JdbcTemplate(getDataSource());
    }
}
