package com.zy.nut.services.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionSynchronizationUtils;

/**
 * Created by Administrator on 2016/10/30.
 */
@Aspect
@Component
public class TransactionAopIntercepter {
    private Logger logger = LoggerFactory.getLogger(TransactionAopIntercepter.class);

    @Before("execution(public * com.zy.nut.common.service..*.*(..))")
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public void beforeDo(){
        boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();

        logger.debug("beforeDo readOnly:{}", readOnly);
    }
}
