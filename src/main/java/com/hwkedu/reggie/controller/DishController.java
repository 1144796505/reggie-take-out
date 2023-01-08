package com.hwkedu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hwkedu.reggie.common.R;
import com.hwkedu.reggie.dto.DishDto;
import com.hwkedu.reggie.entity.Category;
import com.hwkedu.reggie.entity.Dish;
import com.hwkedu.reggie.entity.DishFlavor;
import com.hwkedu.reggie.service.CategoryService;
import com.hwkedu.reggie.service.DishFlavorService;
import com.hwkedu.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
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

    @Autowired
    DishFlavorService dishFlavorService;

    @Autowired
    RedisTemplate redisTemplate;
    @PostMapping()
    public R<String> save(@RequestBody DishDto dishDto){
        log.info("接收到的数据{}",dishDto.toString());

        dishService.saveWithFlavor(dishDto);

        //更新后清理缓存,让其重新查询数据库更新信息
        String key = "dish_"+dishDto.getCategoryId()+"_1";

        redisTemplate.delete(key);
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
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable("id") Long id){
        DishDto dishDto = dishService.getWithFlavor(id);
        return R.success(dishDto);
    }
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);

        //更新后清理缓存,让其重新查询数据库更新信息
        String key = "dish_"+dishDto.getCategoryId()+"_1";

        redisTemplate.delete(key);

        return R.success("修改菜品成功");
    }

    /**
     * 根据条件查询对应的菜品信息
     * @param
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        List<DishDto> dishDtoList = null;

        //拼接key
        String key = "dish_"+dish.getCategoryId()+"_"+dish.getStatus();

       dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);

       //如果redis有数据直接返回
        if (dishDtoList != null){
            return R.success(dishDtoList);
        }


        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        //添加条件，查询状态为1（1为起售，0为停售）的菜品
        queryWrapper.eq(Dish::getStatus,1);

        List<Dish> list = dishService.list(queryWrapper);

       dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            //对象拷贝（每一个list数据）
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();  //分类id
            //通过categoryId查询到category内容
            Category category = categoryService.getById(categoryId);
            //判空
            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //获取当前菜品id
            Long dishId = item.getId();

            //构造条件构造器
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper= new LambdaQueryWrapper<>();
            //添加查询条件
            dishFlavorLambdaQueryWrapper.eq(dishId != null,DishFlavor::getDishId,dishId);
            //select * from dish_flavors where dish_id = ?
            List<DishFlavor> dishFlavors = dishFlavorService.list(dishFlavorLambdaQueryWrapper);

            dishDto.setFlavors(dishFlavors);

            return dishDto;
        }).collect(Collectors.toList());

       //如果redis没有数据,查询数据库后,把数据放入到redis
        redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);

        return R.success(dishDtoList);
    }


}
