package com.hwkedu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hwkedu.reggie.common.BaseContext;
import com.hwkedu.reggie.common.R;
import com.hwkedu.reggie.entity.Orders;
import com.hwkedu.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: OrderController
 * Package: com.hwkedu.reggie.controller
 * Description:
 *
 * @Author: 侯文柯
 * @Create: 2022/12/3 - 17:11
 * @Version: v1.0
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    OrdersService ordersService;

    /**
     * 生成订单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> save(@RequestBody Orders orders){
        ordersService.submit(orders);
        return R.success("下单成功");
    }
    @GetMapping("/userPage")
    public R<Page> orderPage(int page , int pageSize){
       Page<Orders> pages = new Page<>(page,pageSize);
        Long userId = BaseContext.getCurrent();
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId,userId);
        ordersService.page(pages,queryWrapper);
        return R.success(pages);
    }


}
