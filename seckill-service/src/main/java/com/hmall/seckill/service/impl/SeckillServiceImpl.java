package com.hmall.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmall.api.client.TradeClient;
import com.hmall.common.utils.UserContext;
import com.hmall.seckill.constant.MQConstants;
import com.hmall.seckill.domain.dto.SeckillDTO;
import com.hmall.seckill.domain.po.Item;
import com.hmall.seckill.mapper.SeckillMapper;
import com.hmall.seckill.service.ISeckillService;
import com.hmall.seckill.utils.RedisIdWorker;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class SeckillServiceImpl extends ServiceImpl<SeckillMapper, Item> implements ISeckillService {


    private final StringRedisTemplate stringRedisTemplate;
    public final RedisIdWorker redisIdWorker;
    private final TradeClient tradeClient;
    private final RabbitTemplate rabbitTemplate;


    // 静态代码块将lua脚本导入
    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;
    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("seckill.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);
    }

    /**
     * 秒杀商品
     *
     * @param itemId 商品ID
     * @return 是否秒杀成功
     */
    @Override
    public boolean seckill(Long itemId) {
        Long userId = UserContext.getUser();
        Long orderId = redisIdWorker.nextId("order");
        // 执行Lua脚本，判断是否重复下单和库存是否充足，并执行对应的扣减库存操作
        Long result = stringRedisTemplate.execute(
                SECKILL_SCRIPT,
                Collections.emptyList(),
                itemId.toString(), userId.toString(), orderId.toString()
        );
        int r = result.intValue();
        // 下单失败则直接返回
        if(r != 0){
            return false;
        }
        Map<String, Long> seckillOrderMap = new HashMap<>();
        seckillOrderMap.put("itemId", itemId);
        seckillOrderMap.put("orderId", orderId);
        seckillOrderMap.put("userId", userId);
        // 向消息队列发送消息， 由订单服务进行订单生成，OrderFormDTO
        rabbitTemplate.convertAndSend(
                MQConstants.SECKILL_EXCHANGE_NAME,
                MQConstants.SECKILL_ORDER_KEY,
                seckillOrderMap,
                message -> {
                    message.getMessageProperties().setDelay(10000);
                    return message;
                }
        );
        return true;
    }
}
