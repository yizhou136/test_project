package com.zy.nut.services.configuration.datasource;


import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/30.
 */
@Configuration
@EnableConfigurationProperties(NutDataSourceProperties.class)
public class NutDataSourceConfiguration {
    public static final String ReadDataSourceKey = "ReadDataSourceKey";
    public static final String RWDataSourceKey = "RWDataSourceKey";

    /*public static final String ReadTransactionManagerKey = "ReadTransactionManagerKey";
    public static final String RWTransactionManagerKey = "RWTransactionManagerKey";*/

    @Autowired
    private NutDataSourceProperties nutDataSourceProperties;



    /*@Bean
    @Qualifier(RWTransactionManagerKey)
    public DataSourceTransactionManager roTransactionManager(DataSource dataSource){
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        //dataSourceTransactionManager.setDataSource((DataSource) applicationContext.getBean(ReadDataSourceKey));
        dataSourceTransactionManager.setDataSource(dataSource);
        return dataSourceTransactionManager;
    }

    @Bean
    @Qualifier(ReadTransactionManagerKey)
    public DataSourceTransactionManager rwTransactionManager(){

        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        //dataSourceTransactionManager.setDataSource((DataSource) applicationContext.getBean(RWDataSourceKey));
        dataSourceTransactionManager.setDataSource(generateRoDataSource());
        return dataSourceTransactionManager;
    }

    //@Bean(name = ReadDataSourceKey)
    public DataSource generateRoDataSource(){
        DataSource roDataSource = new NutDataSource(ReadDataSourceKey,nutDataSourceProperties.getRoUrl(),
                nutDataSourceProperties.getRoUsername(),
                nutDataSourceProperties.getRoPassword());
        return roDataSource;
    }

    @Bean(name = RWDataSourceKey)
    public DataSource generateRwDataSource(){
        DataSource rwDataSource = new NutDataSource(RWDataSourceKey,nutDataSourceProperties.getRwUrl(),
                nutDataSourceProperties.getRwUsername(),
                nutDataSourceProperties.getRwPassword());
        return rwDataSource;
    }*/

    @Bean
    public DataSource generateRoutingDataSource(){
        NutRoutingDataSource nutRoutingDataSource = new NutRoutingDataSource();
        DataSource roDataSource = new NutDataSource(ReadDataSourceKey,nutDataSourceProperties.getRoUrl(),
                nutDataSourceProperties.getRoUsername(),
                nutDataSourceProperties.getRoPassword());
        DataSource rwDataSource = new NutDataSource(RWDataSourceKey,nutDataSourceProperties.getRwUrl(),
                nutDataSourceProperties.getRwUsername(),
                nutDataSourceProperties.getRwPassword());

        Map<Object, Object> map = new HashMap<Object,Object>();
        map.put(ReadDataSourceKey, roDataSource);
        map.put(RWDataSourceKey, rwDataSource);
        nutRoutingDataSource.setDefaultTargetDataSource(rwDataSource);
        nutRoutingDataSource.setTargetDataSources(map);
        return nutRoutingDataSource;
    }

    public static class NutDataSource extends DriverManagerDataSource {
        private String type;

        NutDataSource(String type, String url,String un,String pw){
            super(url, un, pw);
            this.type = type;
        }

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            return sb.append("thed datasource type is :").append(type).toString();
        }
    }
}