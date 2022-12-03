package com.hwkedu.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hwkedu.reggie.dto.DishDto;
import com.hwkedu.reggie.entity.Dish;
import com.hwkedu.reggie.entity.DishFlavor;
import com.hwkedu.reggie.mapper.DishMapper;
import com.hwkedu.reggie.service.DishFlavorService;
import com.hwkedu.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
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
     *
     * @param dishDto Transactional 在方法上表示该方法开启了事务,在类上表示该类开启了事务
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
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味到dishFlavor表中
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据id查询对应的菜品和口味信息
     *
     * @param id
     * @return
     */
    @Override
    public DishDto getWithFlavor(Long id) {
        //通过id查询菜品基本信息
        Dish dish = this.getById(id);
        //创建Dto对象
        DishDto dishDto = new DishDto();
        //完成对象拷贝
        BeanUtils.copyProperties(dish, dishDto);
        //条件查询FLavor
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        //将查询到的flavor赋值到dto对象中
        dishDto.setFlavors(list);
        return dishDto;

    }

    /**
     * 修改菜品和菜品口味并保存
     *
     * @param dishDto
     */
    @Transactional
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //根据id修改菜品的基本信息
        this.updateById(dishDto);

        //通过dishID将DishFlavor表中的口味删除
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        //新增DishFlavor表中的口味数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        //给flavors的dishID赋值
        flavors = flavors.stream().map(item -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        //将数据批量保存到dish_flavor数据库
        dishFlavorService.saveBatch(flavors);
    }
}
