package com.zy.nut.relayer.server.configuration.tx;


import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionSynchronizationUtils;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2016/10/30.
 */
public class MyAnnotationTransactionAttributeSource extends AnnotationTransactionAttributeSource{
    private static final  ThreadLocal<Boolean>  threadLocal = new ThreadLocal<Boolean>();

    public static void setValue(Boolean isReadOnly){
        threadLocal.set(isReadOnly);
    }

    public static Boolean getValue(){
        return threadLocal.get();
    }

    @Override
    public TransactionAttribute getTransactionAttribute(Method method, Class<?> targetClass) {
        TransactionAttribute  transactionAttribute =  super.getTransactionAttribute(method, targetClass);
        if (transactionAttribute == null) return transactionAttribute;
        if (transactionAttribute.isReadOnly()){
            setValue(true);
            //((RuleBasedTransactionAttribute)transactionAttribute).setQualifier(NutDataSourceConfiguration.ReadTransactionManagerKey);
        }else {
            setValue(false);
            //((RuleBasedTransactionAttribute)transactionAttribute).setQualifier(NutDataSourceConfiguration.RWTransactionManagerKey);
        }
        return transactionAttribute;
    }
}
