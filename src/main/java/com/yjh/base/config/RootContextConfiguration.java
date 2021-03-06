package com.yjh.base.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.yjh.base.exception.CostumeExceptionResolver;
import com.yjh.base.site.ClusterEventMulticaster;
import com.yjh.base.site.repository.CustomRepositoryFactoryBean;
import com.yjh.cg.site.server.MessageServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.persistence.ValidationMode;
import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * The parent of all of other configuration
 * Holding some resources which could be shared
 *
 * Created by yjh on 2015/9/19.
 */
@Configuration
@EnableAsync(
        proxyTargetClass = true,
        order =  Ordered.HIGHEST_PRECEDENCE
)
@EnableScheduling
@EnableTransactionManagement(
        mode = AdviceMode.PROXY, proxyTargetClass = false,
        order = Ordered.LOWEST_PRECEDENCE
)
@ComponentScan(
        basePackages = "com",
        excludeFilters = {
                @ComponentScan.Filter(Controller.class),
                @ComponentScan.Filter(ControllerAdvice.class)
        }
)
@Import({SearchEngineConfiguration.class})
@EnableJpaRepositories(
        basePackages = {"com.yjh.base.site.repository", "com.yjh.cg.site.repository", "com.yjh.search.site.repository"},
        entityManagerFactoryRef = "entityManagerFactoryBean",
        transactionManagerRef = "jpaTransactionManager",
        repositoryFactoryBeanClass = CustomRepositoryFactoryBean.class
)
public class RootContextConfiguration implements AsyncConfigurer, SchedulingConfigurer {
    private static Logger logger = LogManager.getLogger();
    private static final Logger schedulingLogger =
            LogManager.getLogger(logger.getName() + ".[scheduling]");

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
        factoryBean.setPackagesToScan("com.yjh.base.site",
                "com.yjh.cg.site", "com.yjh.search.site");
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

//    /**
//     * Spring always use {@code annotationDrivenTransactionManager} to be
//     * default @{@link org.springframework.transaction.annotation.Transactional} manager.
//     */
//    @Bean
//    public PlatformTransactionManager annotationDrivenTransactionManager() {
//        return this.jpaTransactionManager();
//    }

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

    /**
     * TODO Test it, include JSP in transaction
     *
     * @return OpenEntityManagerInViewInterceptor can binds a JPA EntityManager to the
     * thread for the entire processing of the request.
     * EntityManager in View" pattern, i.e. to allow for lazy loading in
     * web views despite the original transactions already being completed.
     */
    @Bean
    public OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor() {
        OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor =
                new OpenEntityManagerInViewInterceptor();
        openEntityManagerInViewInterceptor.setEntityManagerFactory(this.entityManagerFactoryBean().getObject());
        return openEntityManagerInViewInterceptor;
    }

    @Bean
    public RequestMappingHandlerMapping RequestMappingHandlerMapping() {
        RequestMappingHandlerMapping requestMappingHandlerMapping =
                new RequestMappingHandlerMapping();
        Object[] interceptors = new Object[]{this.openEntityManagerInViewInterceptor()};
        requestMappingHandlerMapping.setInterceptors(interceptors);
        return requestMappingHandlerMapping;
    }

    /**
     * Add MessageServer into the register of single Bean:
     * MessageServer has a SpringConfigurator, so you can @Inject or @Autowire in it.
     * But you still cannot inject it to other beans, because it's not in the register of
     * single beans. So, defining a MessageServer in the RootContextConfiguration can
     * add it in.
     *
     */
    @Bean
    public MessageServer messageServer() {
        return new MessageServer();
    }

    @Bean
    public ThreadPoolTaskScheduler taskScheduler()
    {
        logger.info("Setting up thread pool task scheduler with 20 threads.");
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(20);
        scheduler.setThreadNamePrefix("task-");
        scheduler.setAwaitTerminationSeconds(60);
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setErrorHandler(t -> schedulingLogger.error(
                "Unknown error occurred while executing task.", t
        ));
        scheduler.setRejectedExecutionHandler(
                (r, e) -> schedulingLogger.error(
                        "Execution of task {} was rejected for unknown reasons.", r
                )
        );
        return scheduler;
    }

    @Override
    public Executor getAsyncExecutor()
    {
        Executor executor = this.taskScheduler();
        logger.info("Configuring asynchronous method executor {}.", executor);
        return executor;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar registrar)
    {
        TaskScheduler scheduler = this.taskScheduler();
        logger.info("Configuring scheduled method executor {}.", scheduler);
        registrar.setTaskScheduler(scheduler);
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, objects)->schedulingLogger.error(throwable);
    }

    /**
     * Add a ClusterEventMulticaster
     */
    @Bean
    public ClusterEventMulticaster applicationEventMulticaster() {
        return new ClusterEventMulticaster();
    }
}
