package com.hmall.trade.listener;

import com.hmall.api.client.PayClient;
import com.hmall.api.dto.PayOrderDTO;
import com.hmall.trade.constant.MQConstants;
import com.hmall.trade.domain.po.Order;
import com.hmall.trade.domain.po.SeckillOrder;
import com.hmall.trade.service.IOrderService;
import com.hmall.trade.service.ISecKillOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeckillOrderDelayMessageListener {

    public final ISecKillOrderService seckillOrderService;
    public final PayClient payClient;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(MQConstants.DELAY_QUEUE_NAME),
            exchange = @Exchange(value = MQConstants.DELAY_EEXCHANGE_NAME, delayed = "true"),
            key = MQConstants.DELAY_ORDER_KEY
    ))
    public void listenOrderDelayMessage(Long orderId) {
        // 1. 查询订单
        SeckillOrder order = seckillOrderService.getById(orderId);
        // 2.检测订单状态，判断是否已支付
        if(order == null || order.getStatus() != 1){
            // 订单不存在或已支付
            return;
        }
        // 3.未支付，需要查询支付流水状态
        PayOrderDTO payOrderDTO = payClient.queryPayOrderByBizOrderNo(orderId);

        // 4.判断是否支付
        if(payOrderDTO != null && payOrderDTO.getStatus() == 3){
            // 4.1 已支付，标记订单状态
            seckillOrderService.markOrderPaySuccess(orderId);
        }else{
            // 4.2 未支付，取消订单，回复库存
            seckillOrderService.cancelOrder(orderId);
        }
    }
}
