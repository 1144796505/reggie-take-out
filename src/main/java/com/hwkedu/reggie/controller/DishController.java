package com.hwkedu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hwkedu.reggie.common.R;
import com.hwkedu.reggie.dto.DishDto;
import com.hwkedu.reggie.entity.Category;
import com.hwkedu.reggie.entity.Dish;
import com.hwkedu.reggie.service.CategoryService;
import com.hwkedu.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 侯文柯
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    DishService dishService;

    @Autowired
    CategoryService categoryService;
    @PostMapping()
    public R<String> save(@RequestBody DishDto dishDto){
        log.info("接收到的数据{}",dishDto.toString());

        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page , int pageSize ,String name){
        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //like模糊查询条件
        queryWrapper.like(name != null , Dish::getName , name)
                .orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        //获取原records数据
        List<Dish> records  = pageInfo.getRecords();

       List<DishDto> list = records.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();//获取分类id
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();//获取分类名称
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());
       dishDtoPage.setRecords(list);
        return   R.success(dishDtoPage);
    }
}
