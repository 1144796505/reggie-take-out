package com.hwkedu.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hwkedu.reggie.common.CustomException;
import com.hwkedu.reggie.entity.Category;
import com.hwkedu.reggie.entity.Dish;
import com.hwkedu.reggie.entity.Setmeal;
import com.hwkedu.reggie.mapper.CategoryMapper;
import com.hwkedu.reggie.service.CategoryService;
import com.hwkedu.reggie.service.DishService;
import com.hwkedu.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 侯文柯
 * @version 1.0
 * //交给spring容器进行管理
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
   @Autowired
    DishService dishService;
   @Autowired
     SetmealService setmealService;
    /**
     * 根据id判断是否删除分类
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加dish查询条件，根据分类id进行查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
      //查看当前分类是否关联了菜品，如果已经关联，则抛出异常
        if (count1>0){
            //已关联菜品，抛出一个业务异常
            throw new CustomException("当前分类已经关联菜品,不能删除");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加setmeal查询条件，根据分类id进行查询
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        //查看当前分类是否关联了菜品，如果已经关联，则抛出异常
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2>0){
            //已关联套餐，抛出一个业务异常
            throw new CustomException("当前分类已经关联套餐,不能删除");
        }
        //正常删除
        super.removeById(id);
    }
}
