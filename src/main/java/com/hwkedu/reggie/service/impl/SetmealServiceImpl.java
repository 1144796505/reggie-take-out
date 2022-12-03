package com.hwkedu.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hwkedu.reggie.common.CustomException;
import com.hwkedu.reggie.dto.SetmealDto;
import com.hwkedu.reggie.entity.Setmeal;
import com.hwkedu.reggie.entity.SetmealDish;
import com.hwkedu.reggie.mapper.SetmealMapper;
import com.hwkedu.reggie.service.SetmealDishService;
import com.hwkedu.reggie.service.SetmealService;
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
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    SetmealDishService setmealDishService;

    /**
     * 新增套餐实现
     * @param setmealDto
     */
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息到setmeal表中,通过insert的方式
        this.save(setmealDto);

        //保存套餐和菜品的信息到setmealDish表中,通过insert的方式
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
     setmealDishes =   setmealDishes.stream().map(item ->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 根据id删除套餐和菜品
     * @Transactional 开启事务
     * @param ids
     */

    @Override
    @Transactional
    public void deleteWithDish(List<Long> ids) {
            //先判断状态是否是停售
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids)
                .eq(Setmeal::getStatus,1);
        int count = this.count(queryWrapper);
        if (count>0){
            //有正在起售的套餐不能删除,抛出自定义异常
            throw new CustomException("套餐正在起售,无法删除");
        }
        //根据id删除套餐
        this.removeByIds(ids);

        //根据id删除套餐关联表中的菜品
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(lambdaQueryWrapper);
    }
}
