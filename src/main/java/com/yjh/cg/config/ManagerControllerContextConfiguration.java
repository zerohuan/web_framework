package com.yjh.cg.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.RequestToViewNameTranslator;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.DefaultRequestToViewNameTranslator;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.inject.Inject;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * CG project's configuration
 * For DispatcherServlet Context, it's parent is {@link com.yjh.base.config.RootContextConfiguration}
 *
 * Created by yjh on 2015/9/19.
 */
@Configuration
@EnableWebMvc
@ComponentScan(
        basePackages = "com.yjh.cg.site",
        useDefaultFilters = false,
        includeFilters = @ComponentScan.Filter(Controller.class)
)
@Profile("development")
@PropertySource("classpath:setting.properties")
public class ManagerControllerContextConfiguration extends WebMvcConfigurerAdapter {
    private static Logger logger = LogManager.getLogger();

    @Inject
    ApplicationContext applicationContext;
    @Inject
    SpringValidatorAdapter validatorAdapter;

    @Inject
    ObjectMapper objectMapper;
    @Inject
    Marshaller marshaller;
    @Inject
    Unmarshaller unmarshaller;

    @Override
    public void configureMessageConverters(
            List<HttpMessageConverter<?>> converters
    ) {
        converters.add(new ByteArrayHttpMessageConverter());
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        stringHttpMessageConverter.setSupportedMediaTypes(Arrays.asList(
                new MediaType("text", "plain", Charset.forName("utf-8")),
                new MediaType("text", "html", Charset.forName("utf-8"))
        ));
        converters.add(stringHttpMessageConverter);
        converters.add(new FormHttpMessageConverter());
        converters.add(new SourceHttpMessageConverter<>());

        //add json converter
        MappingJackson2HttpMessageConverter jsonConverter =
                new MappingJackson2HttpMessageConverter();
        jsonConverter.setSupportedMediaTypes(Arrays.asList(
                new MediaType("application", "json", Charset.forName("utf-8")),
                new MediaType("text", "json", Charset.forName("utf-8")),
                new MediaType("application", "x-www-form-urlencoded", Charset.forName("utf-8"))
        ));
        jsonConverter.setObjectMapper(this.objectMapper);
        converters.add(jsonConverter);

        //add xml converter
        MarshallingHttpMessageConverter xmlConverter =
                new MarshallingHttpMessageConverter();
        xmlConverter.setSupportedMediaTypes(Arrays.asList(
                new MediaType("application", "xml", Charset.forName("utf-8")),
                new MediaType("text", "xml", Charset.forName("utf-8"))
        ));
        xmlConverter.setMarshaller(this.marshaller);
        xmlConverter.setUnmarshaller(this.unmarshaller);
        converters.add(xmlConverter);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(true).favorParameter(false)
                .parameterName("mediaType").ignoreAcceptHeader(false)
                .useJaf(false).defaultContentType(MediaType.APPLICATION_XML)
                .mediaType("xml", MediaType.APPLICATION_XML)
                .mediaType("json", MediaType.APPLICATION_JSON);
    }

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();

        logger.debug("project resolver initial.");

        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("/WEB-INF/m/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    /**
     * if you want to return entities and entities attribute, you need it translate entities to name of view.
     *
     */
    @Bean
    public RequestToViewNameTranslator viewNameTranslator()
    {
        return new DefaultRequestToViewNameTranslator();
    }


    /**
     * add argumentResolvers
     * include resolver for pageable parameter, sort parameter
     *
     * just when defecting pageable and sort parameter, this configuration will work
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        Sort defaultSort = new Sort(
                new Sort.Order(Sort.Direction.ASC, "createDate"),
                new Sort.Order(Sort.Direction.ASC, "id")
        );
        Pageable defaultPageable = new PageRequest(0, 20, defaultSort);

        SortHandlerMethodArgumentResolver sortResolver =
                new SortHandlerMethodArgumentResolver();

        //default parameter's name is "sort"
        sortResolver.setSortParameter("$paging.sort");
        sortResolver.setFallbackSort(defaultSort);

        PageableHandlerMethodArgumentResolver pageableResolver =
                new PageableHandlerMethodArgumentResolver(sortResolver);

        pageableResolver.setMaxPageSize(20);
        //page starts at 1, not 0
        pageableResolver.setOneIndexedParameters(true);
        //use default name: "page" and "size"
        pageableResolver.setPrefix("$paging.");
        pageableResolver.setFallbackPageable(defaultPageable);

        argumentResolvers.add(sortResolver);
        argumentResolvers.add(pageableResolver);
    }

    /**
     * Add a DomainClassConverter, it will convert request parameters and path parameters to Entities.
     * It belongs to Spring DATA's support for Spring MVC.
     *
     * @param registry must be instance of FormattingConversionService, otherwise DATA JPA converter
     *                 won't work normally.
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        if(!(registry instanceof FormattingConversionService)) {
            logger.warn("Unable to register Spring DATA JPA converter.");
            return;
        }

        DomainClassConverter<FormattingConversionService> converter =
                new DomainClassConverter<>((FormattingConversionService)registry);

        converter.setApplicationContext(this.applicationContext);
    }

    /**
     * Spring MVC will cover the Validator in root context,
     * so you must add getValidator method
     *
     */
    @Bean
    public Validator getValidator() {
        return this.validatorAdapter;
    }
}
