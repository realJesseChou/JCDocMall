package com.hmall.trade.listener;

import com.hmall.trade.domain.po.SeckillOrder;
import com.hmall.trade.service.ISecKillOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeckillPayStatusListener {

    private final ISecKillOrderService secKillOrderService;

    @RabbitListener(bindings= @QueueBinding(
            value = @Queue(name="trade.pay.success.queue", durable = "true"),
            exchange = @Exchange(name="pay.direct"),
            key= "pay.success"
        )
    )
    public void listenPaySuccess(Long orderId){
        SeckillOrder order = secKillOrderService.getById(orderId);
        if(order == null || order.getStatus() == 1){
            return;
        }
        secKillOrderService.markOrderPaySuccess(orderId);
    }
}
