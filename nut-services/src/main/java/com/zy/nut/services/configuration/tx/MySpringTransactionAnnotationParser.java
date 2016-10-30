package com.zy.nut.services.configuration.tx;

import com.zy.nut.services.configuration.datasource.NutDataSourceConfiguration;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.SpringTransactionAnnotationParser;
import org.springframework.transaction.interceptor.NoRollbackRuleAttribute;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/30.
 */
public class MySpringTransactionAnnotationParser extends SpringTransactionAnnotationParser {

    protected TransactionAttribute parseTransactionAnnotation(AnnotationAttributes attributes) {
        TransactionAttribute transactionAttribute = super.parseTransactionAnnotation(attributes);
        if (transactionAttribute.isReadOnly()){
            ((RuleBasedTransactionAttribute)transactionAttribute).setQualifier(NutDataSourceConfiguration.ReadTransactionManagerKey);
        }else {
            ((RuleBasedTransactionAttribute)transactionAttribute).setQualifier(NutDataSourceConfiguration.RWTransactionManagerKey);
        }
        return transactionAttribute;
    }
}
