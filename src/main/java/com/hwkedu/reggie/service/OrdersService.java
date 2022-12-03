package com.hwkedu.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hwkedu.reggie.entity.Orders;

/**
 * ClassName: OrdersService
 * Package: com.hwkedu.reggie.service
 * Description:
 *
 * @Author: 侯文柯
 * @Create: 2022/12/2 - 21:45
 * @Version: v1.0
 */
public interface OrdersService extends IService<Orders> {
    /**
     * 用户下单
     * @param orders
     */
    void submit(Orders orders);
}
