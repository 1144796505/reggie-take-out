package com.hwkedu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hwkedu.reggie.common.BaseContext;
import com.hwkedu.reggie.common.CustomException;
import com.hwkedu.reggie.common.R;
import com.hwkedu.reggie.entity.AddressBook;
import com.hwkedu.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName: AddressBookController
 * Package: com.hwkedu.reggie.controller
 * Description:
 *
 * @Author: 侯文柯
 * @Create: 2022/12/1 - 20:41
 * @Version: v1.0
 */
@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookController {
    @Autowired
    AddressBookService addressBookService;


    @DeleteMapping
    public R<String> delete(@RequestParam("ids") Long ids){
        //先判断当前地址是否是默认的,如果是默认的就提示不能删除
        AddressBook addressBook = addressBookService.getById(ids);
        if (addressBook.getIsDefault() == 1){
            throw new CustomException("这是默认地址,无法删除");
        }
        addressBookService.removeById(ids);
        return R.success("删除成功");
    }

    /**
     * 修改地址信息
     * @param addressBook
     * @return
     */
    @PutMapping
    public R<String> updateSave(@RequestBody AddressBook addressBook){
     addressBookService.updateById(addressBook);

        return R.success("修改地址成功");
    }

    @GetMapping("/{id}")
    public R<AddressBook> getUpdate(@PathVariable("id") Long id){
        AddressBook addressBook = addressBookService.getById(id);
        return R.success(addressBook);
    }

    /**
     * 根据id设置默认地址
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    public R<String> getDefault(@RequestBody AddressBook addressBook){
        //先根据登录id把所有保存地址的默认值改成0
        Long userId = BaseContext.getCurrent();
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,userId);
        addressBook.setIsDefault(0);
        addressBookService.update(addressBook,queryWrapper);

        //再根据id修改当前地址为默认值
        LambdaQueryWrapper<AddressBook> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AddressBook::getId,addressBook.getId());
        addressBook.setIsDefault(1);
        addressBookService.update(addressBook,lambdaQueryWrapper);
        return R.success("设为默认地址成功");

    }

    @GetMapping("/default")
    public R<AddressBook> defult(){
        //获取登录用户的id
        Long userId = BaseContext.getCurrent();
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,userId)
                .eq(AddressBook::getIsDefault,1);
        AddressBook one = addressBookService.getOne(queryWrapper);
        return R.success(one);
    }

    /**
     * 保存地址到表中
     * @param addressBook
     * @return
     */
    @PostMapping

    public R<String> save(@RequestBody AddressBook addressBook){
            //获取登录的用户id赋值给userId
        Long userId = BaseContext.getCurrent();
        addressBook.setUserId(userId);
        addressBookService.save(addressBook);
        return R.success("保存地址成功");
    }

    /**
     * 根据登录id显示数据
     * @return
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list(){
        //获取登录的用户id
        Long userId = BaseContext.getCurrent();
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,userId);
        List<AddressBook> list = addressBookService.list(queryWrapper);
        return R.success(list);
    }
}
