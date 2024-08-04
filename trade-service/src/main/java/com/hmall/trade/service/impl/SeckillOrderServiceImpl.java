package com.hmall.trade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmall.trade.domain.po.SeckillOrder;
import com.hmall.trade.mapper.SeckillOrderMapper;
import com.hmall.trade.service.ISecKillOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2023-05-05
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements ISecKillOrderService {

    @Override
    public Long createOrder(SeckillOrder seckillOrder) {
        Long orderId = seckillOrder.getId();
        // 1.查询订单是否存在
        SeckillOrder order = getById(orderId);
        if (order != null) {
            log.warn("订单已存在，订单号：{}", orderId);
            return null;
        }
        // 2.创建订单
        save(seckillOrder);
        return orderId;
    }
}
