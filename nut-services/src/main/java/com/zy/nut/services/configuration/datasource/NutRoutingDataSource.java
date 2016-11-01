package com.zy.nut.services.configuration.datasource;


import com.zy.nut.services.configuration.tx.MyAnnotationTransactionAttributeSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;


import javax.sql.DataSource;

/**
 * Created by Administrator on 2016/10/30.
 */
public class NutRoutingDataSource extends AbstractRoutingDataSource{
    private Logger logger = LoggerFactory.getLogger(NutRoutingDataSource.class);


    @Override
    protected String determineCurrentLookupKey() {
        String re = NutDataSourceConfiguration.RWDataSourceKey;
        Boolean isReadOnly = (Boolean) MyAnnotationTransactionAttributeSource.getValue();
        if (isReadOnly){
            re = NutDataSourceConfiguration.ReadDataSourceKey;
        }

        logger.debug("determineCurrentLookupKey {}", re);
        return re;
    }

    protected DataSource determineTargetDataSource() {
        DataSource dataSource = super.determineTargetDataSource();
        logger.debug("determineTargetDataSource {}",dataSource);
        return dataSource;
    }
}
