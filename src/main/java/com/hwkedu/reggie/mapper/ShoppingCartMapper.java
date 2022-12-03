package com.hwkedu.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hwkedu.reggie.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

/**
 * ClassName: ShoppingCartMapper
 * Package: com.hwkedu.reggie.mapper
 * Description:
 *
 * @Author: 侯文柯
 * @Create: 2022/12/2 - 19:28
 * @Version: v1.0
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
