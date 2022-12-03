package com.hwkedu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hwkedu.reggie.common.BaseContext;
import com.hwkedu.reggie.common.R;
import com.hwkedu.reggie.entity.ShoppingCart;
import com.hwkedu.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName: ShoppingCartController
 * Package: com.hwkedu.reggie.controller
 * Description:
 *
 * @Author: 侯文柯
 * @Create: 2022/12/2 - 19:33
 * @Version: v1.0
 */
@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {
    @Autowired
    ShoppingCartService shoppingCartService;

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean(){
        //获取登录用户ID
        Long userId = BaseContext.getCurrent();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        shoppingCartService.remove(queryWrapper);
        return R.success("清空购物车成功");
    }
    /**
     * 根据传入的菜品id或套餐id让其数量-1
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public R<String> sub(@RequestBody ShoppingCart shoppingCart){
        //判断传来的id是菜品还是套餐执行不同的操作
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        if (shoppingCart.getDishId() != null){
            queryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
            ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);
            Integer number = cartServiceOne.getNumber();

            cartServiceOne.setNumber(number - 1);
            shoppingCartService.updateById(cartServiceOne);
            if (cartServiceOne.getNumber() == 0){
                shoppingCartService.removeById(cartServiceOne);
            }
        }else {
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
            ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number - 1);
            shoppingCartService.updateById(cartServiceOne);
            if (cartServiceOne.getNumber() == 0){
                shoppingCartService.removeById(cartServiceOne);
            }
        }
        return R.success("删除成功");
    }

    /**
     * 根据登录id查询购物车内的东西
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        //根据登录id查询购物车内的东西
        Long userId = BaseContext.getCurrent();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId)
                .orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 根据用户id添加菜品或套餐
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        //获取登录的用户id,看看是谁点的菜
        Long userId = BaseContext.getCurrent();
        shoppingCart.setUserId(userId);
        //条件构造器
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        //查看点的是菜品还是套餐
        Long dishId = shoppingCart.getDishId();
        //如果dishId为空表示点的是套餐,反之则为菜品
        if(dishId !=null){
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        //查询数据
        ShoppingCart shoppingCartServiceOne = shoppingCartService.getOne(queryWrapper);
        //查看点的东西是否已在购物车存在,如存在则数量+1
        if (shoppingCartServiceOne != null){
            Integer number = shoppingCartServiceOne.getNumber();
            shoppingCartServiceOne.setNumber(++number);
            shoppingCartService.updateById(shoppingCartServiceOne);
        }
        //查看点的东西是否已在购物车存在,不存在则执行insert插入
         else {
            shoppingCartService.save(shoppingCart);
            shoppingCartServiceOne = shoppingCart;
        }

        return R.success(shoppingCartServiceOne);
    }
}
