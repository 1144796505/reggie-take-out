package com.hwkedu.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hwkedu.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * ClassName: OrdersMapper
 * Package: com.hwkedu.reggie.mapper
 * Description:
 *
 * @Author: 侯文柯
 * @Create: 2022/12/2 - 21:44
 * @Version: v1.0
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
