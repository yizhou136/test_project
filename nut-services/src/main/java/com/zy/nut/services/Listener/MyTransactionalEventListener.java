package com.zy.nut.services.Listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Created by Administrator on 2016/10/30.
 */
@Component
public class MyTransactionalEventListener {
    private Logger logger = LoggerFactory.getLogger(MyTransactionalEventListener.class);

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void beforeCommint(MyTransEvent myTransEvent){
        logger.info("beforeCommint xxx  isReadOnly:{}", myTransEvent);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void afterCommit(MyTransEvent myTransEvent){
        logger.info("afterCommint xxx  isReadOnly:{}", myTransEvent);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void afterCompletion(MyTransEvent myTransEvent){
        logger.info("afterCompletion xxx  isReadOnly:{}", myTransEvent);
    }
}
