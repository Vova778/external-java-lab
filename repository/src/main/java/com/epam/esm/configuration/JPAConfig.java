package com.epam.esm.configuration;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManager;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;


@Slf4j
@Configuration
@EnableJpaRepositories("com.epam.esm")
@EnableTransactionManagement
@Data
public class JPAConfig {
    @Value("${spring.jpa.show-sql}")
    private String showSql;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String hibernateDDLAuto;
    @Value("${spring.jpa.properties.hibernate.format_sql}")
    private String formatSql;
    @Value("${spring.jpa.properties.hibernate.highlight_sql}")
    private String highlightSql;
    @Value("${spring.jpa.properties.hibernate.dialect}")
    private String dialect;
    @Value("${spring.jpa.properties.hibernate.default_schema}")
    private String defaultSchema;
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.hikari.minimumIdle}")
    private String minimumIdle;
    @Value("${spring.datasource.hikari.maximumPoolSize}")
    private String maxPoolSize;
    @Value("${spring.datasource.hikari.idleTimeout}")
    private String idleTimeout;
    @Value("${spring.datasource.hikari.poolName}")
    private String poolName;
    @Value("${spring.datasource.hikari.maxLifetime}")
    private String maxLifeTime;
    @Value("${spring.datasource.hikari.connectionTimeout}")
    private String connectionTimeout;

    @Bean
    @Profile("default")
    public DataSource defaultDataSource() {
        log.debug("H2 DataSource created");
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:schema-h2.sql")
                .addScript("classpath:data-h2.sql")
                .ignoreFailedDrops(true)
                .setName("test")
                .build();
    }

    @Bean
    @Profile({"prod", "aws"})
    public DataSource prodDataSource() {
        HikariDataSource dataSource;
        HikariConfig config = new HikariConfig();


        config.setDriverClassName(driverClassName);
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(Integer.parseInt(maxPoolSize));
        config.setMinimumIdle(Integer.parseInt(minimumIdle));
        config.setIdleTimeout(Integer.parseInt(idleTimeout));
        config.setPoolName(poolName);
        config.setMaxLifetime(Integer.parseInt(maxLifeTime));
        config.setConnectionTimeout(Integer.parseInt(connectionTimeout));

        dataSource = new HikariDataSource(config);
        log.debug("MySQL DataSource with HikariCP created");

        Resource createSchema = new ClassPathResource("schema-mysql.sql");
        DatabasePopulator databaseCreator = new ResourceDatabasePopulator(createSchema);
        DatabasePopulatorUtils.execute(databaseCreator, dataSource);
        log.debug("Schema creation script executed");

        Resource initSchema = new ClassPathResource("data-mysql.sql");
        DatabasePopulator databasePopulator = new ResourceDatabasePopulator(initSchema);
        DatabasePopulatorUtils.execute(databasePopulator, dataSource);
        log.debug("Schema initialization script executed");

        return dataSource;
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        Properties properties = new Properties();
        properties.setProperty("hibernate.show_sql", showSql);
        properties.setProperty("hibernate.ddl-auto", hibernateDDLAuto);
        properties.setProperty("hibernate.format_sql", formatSql);
        properties.setProperty("hibernate.highlight_sql", highlightSql);
        properties.setProperty("hibernate.dialect", dialect);
        properties.setProperty("hibernate.default_schema", defaultSchema);
        LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
        emfBean.setJpaProperties(properties);
        emfBean.setDataSource(dataSource);
        emfBean.setPackagesToScan("com.epam.esm");
        emfBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        log.debug("EntityManagerFactory created");
        return emfBean;
    }

    @Bean
    public EntityManager entityManager(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return Objects.requireNonNull(entityManagerFactory.getObject()).createEntityManager();
    }

    @Bean
    public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        log.debug("JpaTransactionManager created");
        return jpaTransactionManager;
    }

}