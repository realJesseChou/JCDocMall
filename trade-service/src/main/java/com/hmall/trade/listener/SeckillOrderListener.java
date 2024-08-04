package com.hmall.trade.listener;

import com.hmall.trade.constant.MQConstants;
import com.hmall.trade.domain.po.SeckillOrder;
import com.hmall.trade.service.IOrderService;
import com.hmall.trade.service.ISecKillOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SeckillOrderListener {

    private final ISecKillOrderService secKillOrderService;

    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(MQConstants.SECKILL_QUEUE_NAME),
            exchange = @Exchange(value = MQConstants.SECKILL_EXCHANGE_NAME, delayed = "true"),
            key = MQConstants.SECKILL_ORDER_KEY
    ))
    public void listenSeckillOrder(Map<String, Long> seckillOrderMap) {
        // 根据生成的订单id创建订单，这里的id是秒杀订单id，创建一个秒杀订单的数据表
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setId(seckillOrderMap.get("orderId"));
        seckillOrder.setItemId(seckillOrderMap.get("itemId"));
        seckillOrder.setUserId(seckillOrderMap.get("userId"));
        seckillOrder.setStatus(1);
        seckillOrder.setCreateTime(LocalDateTime.now());

        // 创建秒杀订单
        Long orderId = secKillOrderService.createOrder(seckillOrder);
        if(orderId == null){
            // 创建订单失败
            return;
        }

        // 发送延迟消息监听订单支付状态
        rabbitTemplate.convertAndSend(
                MQConstants.DELAY_EEXCHANGE_NAME,
                MQConstants.DELAY_ORDER_KEY,
                orderId,
                message -> {
                    message.getMessageProperties().setDelay(1000 * 60 * 15);
                    return message;
                }
        );

    }
}
