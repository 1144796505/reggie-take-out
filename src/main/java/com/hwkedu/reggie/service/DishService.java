package com.hwkedu.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hwkedu.reggie.dto.DishDto;
import com.hwkedu.reggie.entity.Dish;

/**
 * @author 侯文柯
 * @version 1.0
 */
public interface DishService extends IService<Dish> {
    /**
     * 新增菜品和菜品口味到两张表中
     * @param dishDto
     */
    public void saveWithFlavor(DishDto dishDto);
}
