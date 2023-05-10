package com.epam.esm.configuration;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
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
@EnableTransactionManagement
@Profile("default")
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

    @Bean
    public DataSource dataSource() {
        log.debug("DataSource created");
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:schema-h2.sql")
                .addScript("classpath:data-h2.sql")
                .ignoreFailedDrops(true)
                .setName("test")
                .build();
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.show_sql", showSql);
        properties.setProperty("hibernate.hbm2ddl.auto", hibernateDDLAuto);
        properties.setProperty("hibernate.format_sql", formatSql);
        properties.setProperty("hibernate.highlight_sql", highlightSql);
        properties.setProperty("hibernate.dialect", dialect);
        properties.setProperty("hibernate.default_schema", defaultSchema);
        LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
        emfBean.setJpaProperties(properties);
        emfBean.setDataSource(dataSource());
        emfBean.setPackagesToScan("com.epam.esm");
        emfBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        log.debug("EntityManagerFactory created");
        return emfBean;
    }
    @Primary
    @Bean
    public EntityManager entityManager() {
        return Objects.requireNonNull(entityManagerFactory().getObject()).createEntityManager();
    }
    @Primary
    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        log.debug("JpaTransactionManager created");
        return jpaTransactionManager;
    }

}