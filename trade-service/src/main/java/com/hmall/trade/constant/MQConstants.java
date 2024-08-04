package com.hmall.trade.constant;

public interface MQConstants {
    String DELAY_EEXCHANGE_NAME = "trade.delay.direct";
    String DELAY_QUEUE_NAME = "trade.delay.order.queue";
    String DELAY_ORDER_KEY = "delay.order.key";

    String SECKILL_EXCHANGE_NAME = "seckill.delay.direct";
    String SECKILL_QUEUE_NAME = "seckill.delay.order.queue";
    String SECKILL_ORDER_KEY = "delay.order.key";
}
