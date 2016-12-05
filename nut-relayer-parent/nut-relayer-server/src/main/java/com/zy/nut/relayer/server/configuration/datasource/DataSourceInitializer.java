package com.zy.nut.relayer.server.configuration.datasource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceInitializedEvent;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.config.SortedResourcesFactoryBean;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhougb on 2016/12/5.
 */
public class DataSourceInitializer implements ApplicationListener<DataSourceInitializedEvent> {

    private static final Log logger = LogFactory.getLog(DataSourceInitializer.class);

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    private DataSource dataSource;

    @Autowired
    private DataSourceProperties properties;

    private boolean initialized = false;

    @PostConstruct
    public void init() {
        if (!this.properties.isInitialize()) {
            logger.debug("Initialization disabled (not running DDL scripts)");
            return;
        }
        if (this.applicationContext.getBeanNamesForType(DataSource.class, false,
                false).length > 0) {
            this.dataSource = this.applicationContext.getBean(DataSource.class);
        }
        if (this.dataSource == null) {
            logger.debug("No DataSource found so not initializing");
            return;
        }
        runSchemaScripts();
    }

    private void runSchemaScripts() {
        List<Resource> scripts = getScripts(this.properties.getSchema(), "schema");
        if (!scripts.isEmpty()) {
            String username = this.properties.getSchemaUsername();
            String password = this.properties.getSchemaPassword();
            runScripts(scripts, username, password);
            try {
                this.applicationContext
                        .publishEvent(new DataSourceInitializedEvent(this.dataSource));
                // The listener might not be registered yet, so don't rely on it.
                if (!this.initialized) {
                    runDataScripts();
                    this.initialized = true;
                }
            }
            catch (IllegalStateException ex) {
                logger.warn("Could not send event to complete DataSource initialization ("
                        + ex.getMessage() + ")");
            }
        }
    }

    @Override
    public void onApplicationEvent(DataSourceInitializedEvent event) {
        if (!this.properties.isInitialize()) {
            logger.debug("Initialization disabled (not running data scripts)");
            return;
        }
        // NOTE the event can happen more than once and
        // the event datasource is not used here
        if (!this.initialized) {
            runDataScripts();
            this.initialized = true;
        }
    }

    private void runDataScripts() {
        List<Resource> scripts = getScripts(this.properties.getData(), "data");
        String username = this.properties.getDataUsername();
        String password = this.properties.getDataPassword();
        runScripts(scripts, username, password);
    }

    private List<Resource> getScripts(String locations, String fallback) {
        if (locations == null) {
            String platform = this.properties.getPlatform();
            locations = "classpath*:" + fallback + "-" + platform + ".sql,";
            locations += "classpath*:" + fallback + ".sql";
        }
        return getResources(locations);
    }

    private List<Resource> getResources(String locations) {
        return getResources(
                Arrays.asList(StringUtils.commaDelimitedListToStringArray(locations)));
    }

    private List<Resource> getResources(List<String> locations) {
        SortedResourcesFactoryBean factory = new SortedResourcesFactoryBean(
                this.applicationContext, locations);
        try {
            factory.afterPropertiesSet();
            List<Resource> resources = new ArrayList<Resource>();
            for (Resource resource : factory.getObject()) {
                if (resource.exists()) {
                    resources.add(resource);
                }
            }
            return resources;
        }
        catch (Exception ex) {
            throw new IllegalStateException("Unable to load resources from " + locations,
                    ex);
        }
    }

    private void runScripts(List<Resource> resources, String username, String password) {
        if (resources.isEmpty()) {
            return;
        }
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.setContinueOnError(this.properties.isContinueOnError());
        populator.setSeparator(this.properties.getSeparator());
        if (this.properties.getSqlScriptEncoding() != null) {
            populator.setSqlScriptEncoding(this.properties.getSqlScriptEncoding().name());
        }
        for (Resource resource : resources) {
            populator.addScript(resource);
        }
        DataSource dataSource = this.dataSource;
        if (StringUtils.hasText(username) && StringUtils.hasText(password)) {
            dataSource = DataSourceBuilder.create(this.properties.getClassLoader())
                    .driverClassName(this.properties.determineDriverClassName())
                    .url(this.properties.determineUrl()).username(username)
                    .password(password).build();
        }
        DatabasePopulatorUtils.execute(populator, dataSource);
    }
}