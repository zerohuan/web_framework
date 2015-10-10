package com.yjh.base.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.yjh.base.exception.CostumeExceptionResolver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.persistence.ValidationMode;
import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * The parent of all of other configuration
 * Holding some resources which could be shared
 *
 * Created by yjh on 2015/9/19.
 */
@Configuration
@EnableTransactionManagement(
        mode = AdviceMode.PROXY, proxyTargetClass = false,
        order = Ordered.LOWEST_PRECEDENCE
)
@ComponentScan(
        basePackages = "com",
        excludeFilters = @ComponentScan.Filter(Controller.class)
)
@EnableJpaRepositories(
        basePackages = {"com.yjh.base.site.repository", "com.yjh.cg.site.repository"},
        entityManagerFactoryRef = "entityManagerFactoryBean",
        transactionManagerRef = "jpaTransactionManager"
)
public class RootContextConfiguration {
    private static Logger logger = LogManager.getLogger();

    @Bean
    public ObjectMapper objectMapper()
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE,
                false);
        return mapper;
    }

    @Bean
    public Jaxb2Marshaller jaxb2Marshaller()
    {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("com.yjh.base.site","com.yjh.cg.site");
        return marshaller;
    }

    /**
     * route to user view by name
     *
     */
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

//    public PersistenceExceptionTranslator persistenceExceptionTranslator() {
//        return new Hibernate
//    }

    //JPA
    /**
     * look up JPA dataSource
     *
     */
    @Bean
    public DataSource springJpaDataSource() {
        JndiDataSourceLookup lookup = new JndiDataSourceLookup();
        return lookup.getDataSource("java:comp/env/jdbc/cg");
    }

    /**
     *  create persistence unit
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
        Map<String, Object> properties = new HashMap<>();
        //do not use schema generation in production
        properties.put("javax.persistence.schema-generation.database.action", "none");
//        properties.put("hibernate.hbm2ddl.auto", "create");

        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabasePlatform("org.hibernate.dialect.MySQL5InnoDBDialect");

        LocalContainerEntityManagerFactoryBean factoryBean =
                new LocalContainerEntityManagerFactoryBean();
        factoryBean.setJpaVendorAdapter(adapter);
        factoryBean.setDataSource(this.springJpaDataSource());
        factoryBean.setPackagesToScan("com.yjh.base.site", "com.yjh.cg.site");
        factoryBean.setValidationMode(ValidationMode.NONE);
        factoryBean.setJpaPropertyMap(properties);

        return factoryBean;
    }

    /**
     * Whenever using @{@link org.springframework.transaction.annotation.EnableTransactionManagement},
     * you must supply a implement of PlatformTransactionManager
     *
     * @see org.springframework.transaction.annotation.EnableTransactionManagement
     */
    @Bean
    public PlatformTransactionManager jpaTransactionManager() {
        return new JpaTransactionManager(
                this.entityManagerFactoryBean().getObject()
        );
    }

    /**
     * Spring always use {@code annotationDrivenTransactionManager} to be
     * default @{@link org.springframework.transaction.annotation.Transactional} manager.
     */
    @Bean
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return this.jpaTransactionManager();
    }

    //message international
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource =
                new ReloadableResourceBundleMessageSource();

        messageSource.setCacheSeconds(-1);
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        messageSource.setBasenames(
                "/WEB-INF/i18n/messages", "/WEB-INF/i18n/errors",
                "/WEB-INF/i18n/validation");

        return messageSource;
    }

    //Bean Validation
    /**
     * Set a specific provider by dynamic class loading.
     * When using Java EE container, it's necessary.
     * When using servlet container, like Tomcat, it's optional.
     * localValidatorFactoryBean will find a provider on classpath automatically.
     *
     * @throws ClassNotFoundException
     */
    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean() throws ClassNotFoundException {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setProviderClass(Class.forName("org.hibernate.validator.HibernateValidator"));
        validator.setValidationMessageSource(this.messageSource());
        return validator;
    }

    /**
     * Default methodValidationPostProcessor will use localValidatorFactoryBean on classpath,
     * so if having used some other configuration like i18n, you should create methodValidationPostProcessor with
     * localValidatorFactoryBean you defined.
     *
     * @throws ClassNotFoundException
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() throws ClassNotFoundException{
        MethodValidationPostProcessor processor =
                new MethodValidationPostProcessor();
        processor.setValidator(this.localValidatorFactoryBean());
        return processor;
    }

    //TODO exceptionResolver
    public HandlerExceptionResolver exceptionResolver() {
        SimpleMappingExceptionResolver resolver = new CostumeExceptionResolver();

        return resolver;
    }
}
