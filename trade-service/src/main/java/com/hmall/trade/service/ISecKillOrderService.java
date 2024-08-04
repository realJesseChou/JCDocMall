package com.hmall.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hmall.trade.domain.po.SeckillOrder;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 虎哥
 * @since 2023-05-05
 */
public interface ISecKillOrderService extends IService<SeckillOrder> {

    Long createOrder(SeckillOrder seckillOrder);

    void markOrderPaySuccess(Long orderId);

    void cancelOrder(Long orderId);
}
