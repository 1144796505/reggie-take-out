package com.hwkedu.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hwkedu.reggie.entity.ShoppingCart;
import com.hwkedu.reggie.mapper.ShoppingCartMapper;
import com.hwkedu.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * ClassName: ShoppingCartServiceImpl
 * Package: com.hwkedu.reggie.service.impl
 * Description:
 *
 * @Author: 侯文柯
 * @Create: 2022/12/2 - 19:32
 * @Version: v1.0
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
