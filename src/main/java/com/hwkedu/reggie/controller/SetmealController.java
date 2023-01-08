package com.hwkedu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hwkedu.reggie.common.R;
import com.hwkedu.reggie.dto.DishDto;
import com.hwkedu.reggie.dto.SetmealDto;
import com.hwkedu.reggie.entity.Dish;
import com.hwkedu.reggie.entity.DishFlavor;
import com.hwkedu.reggie.entity.Setmeal;
import com.hwkedu.reggie.entity.SetmealDish;
import com.hwkedu.reggie.service.SetmealDishService;
import com.hwkedu.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName: SetmealController
 * Package: com.hwkedu.reggie.controller
 * Description:
 *
 * @Author: 侯文柯
 * @Create: 2022/11/30 - 19:34
 * @Version: v1.0
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    SetmealService setmealService;

    @Autowired
    SetmealDishService setmealDishService;

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    @CacheEvict(value = "setMealCache",allEntries = true)
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDish(setmealDto);
        return R.success("添加套餐成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page , int pageSize ,String name){
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        Page<SetmealDto> setmealPageDto = new Page<>(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //根据name模糊查询
        queryWrapper.like(name != null,Setmeal::getName,name)
                .orderByAsc(Setmeal::getUpdateTime);
        setmealService.page(setmealPage,queryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(setmealPage,setmealPageDto,"records");
        List<Setmeal> records = setmealPage.getRecords();
     List<SetmealDto> list =   records.stream().map(item->{
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            Setmeal setmeal = setmealService.getById(item.getId());
            String setmealName = setmeal.getName();
            setmealDto.setCategoryName(setmealName);
            return setmealDto;
        }).collect(Collectors.toList());
        setmealPageDto.setRecords(list);

        return R.success(setmealPageDto);
    }

    /**
     * 根据id删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    @CacheEvict(value = "setMealCache",allEntries = true)
    public R<String> delete(@RequestParam("ids") List<Long> ids){
        log.info("ids:{}",ids);
       setmealService.deleteWithDish(ids);
       return R.success("删除成功");
    }
    @PostMapping("/status/{id}")
    public R<String> post(@PathVariable("id") int id,@RequestParam("ids") List<Long> ids){
        log.info("状态{},ids{}",id,ids);
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Setmeal::getId,ids);
        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(id);
        setmealService.update(setmeal,lambdaQueryWrapper);
//        ids.stream().map((item)->{
//            LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(Setmeal::getId,item);
//            Setmeal setmeal = new Setmeal();
//            setmeal.setStatus(id);
//            setmealService.update(setmeal,queryWrapper);
//            return ids;
//        }).collect(Collectors.toList());

    return R.success("修改成功");
    }

    @GetMapping("/list")
    @Cacheable(value = "setMealCache",key = "#setmeal.categoryId+'_'+#setmeal.status")
    public R<List<Setmeal>> list(Setmeal setmeal){
        //条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId,setmeal.getCategoryId())
                .eq(Setmeal::getStatus,setmeal.getStatus())
                .orderByAsc(Setmeal::getUpdateTime);
        return R.success( setmealService.list(queryWrapper));
    }
}
