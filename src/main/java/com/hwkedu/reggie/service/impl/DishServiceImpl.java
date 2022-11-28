package com.hwkedu.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hwkedu.reggie.dto.DishDto;
import com.hwkedu.reggie.entity.Dish;
import com.hwkedu.reggie.entity.DishFlavor;
import com.hwkedu.reggie.mapper.DishMapper;
import com.hwkedu.reggie.service.DishFlavorService;
import com.hwkedu.reggie.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 侯文柯
 * @version 1.0
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
   @Autowired
   private DishFlavorService dishFlavorService;

    /**
     * 新增菜品,同时保存相应的口味数据
     * @param dishDto
     * Transactional 在方法上表示该方法开启了事务,在类上表示该类开启了事务
     */
    @Transactional
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        //将菜品添加到dish表中
        super.save(dishDto);

        //菜品id
        Long dishId = dishDto.getId();

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors =   flavors.stream().map((item)->{
                item.setDishId(dishId);
                return item;
            }).collect(Collectors.toList());

        //保存菜品口味到dishFlavor表中
        dishFlavorService.saveBatch(flavors);
    }
}
