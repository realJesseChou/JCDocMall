package com.hmall.seckill.controller;

import com.hmall.seckill.service.ISeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    private ISeckillService seckillService;

    @PostMapping("/{itemId}")
    public boolean seckill(@PathVariable Long itemId) {
        return seckillService.seckill(itemId);
    }
}
