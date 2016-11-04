package com.zy.nut.web.configuration.webmvc;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.web.servlet.view.xml.MappingJackson2XmlView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhougb on 2016/11/4.
 */
@Configuration
@EnableWebMvc
public class SpringMVCConfiguration extends WebMvcConfigurationSupport {



    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        super.configureContentNegotiation(configurer);
        configurer.defaultContentType(MediaType.APPLICATION_XHTML_XML);
        configurer.favorParameter(false);
        configurer.favorPathExtension(true);
        configurer.ignoreUnknownPathExtensions(true);
        configurer.ignoreAcceptHeader(true);
    }

    @Override
    protected void configureViewResolvers(ViewResolverRegistry registry) {
        MappingJackson2JsonView mappingJackson2JsonView = new MappingJackson2JsonView();
        MappingJackson2XmlView mappingJackson2XmlView = new MappingJackson2XmlView();
        registry.enableContentNegotiation(mappingJackson2JsonView, mappingJackson2XmlView);
        registry.freeMarker();
        registry.jsp();
    }
}
