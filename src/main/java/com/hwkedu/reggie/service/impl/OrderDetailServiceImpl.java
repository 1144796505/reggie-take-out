package com.hwkedu.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hwkedu.reggie.entity.OrderDetail;
import com.hwkedu.reggie.mapper.OrderDetailMapper;
import com.hwkedu.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * ClassName: OrderDetailServiceImpl
 * Package: com.hwkedu.reggie.service.impl
 * Description:
 *
 * @Author: 侯文柯
 * @Create: 2022/12/2 - 21:59
 * @Version: v1.0
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
