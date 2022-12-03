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

    /**
     * 根据id查询对应的菜品和口味信息
     * @param id
     * @return
     */
    public DishDto getWithFlavor(Long id);

    /**
     * 修改菜品和菜品口味到两张表中
     * @param dishDto
     */
    void updateWithFlavor(DishDto dishDto);
}
