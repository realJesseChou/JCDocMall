package com.hmall.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hmall.seckill.domain.po.Item;

public interface ISeckillService extends IService<Item> {
    boolean seckill(Long itemId);
}
