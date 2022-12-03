package com.hwkedu.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hwkedu.reggie.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * ClassName: OrderDetailMapper
 * Package: com.hwkedu.reggie.mapper
 * Description:
 *
 * @Author: 侯文柯
 * @Create: 2022/12/2 - 21:58
 * @Version: v1.0
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
