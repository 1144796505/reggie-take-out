package com.hwkedu.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hwkedu.reggie.dto.SetmealDto;
import com.hwkedu.reggie.entity.Setmeal;

import java.util.List;

/**
 * @author 侯文柯
 * @version 1.0
 */
public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐
     * @param setmealDto
     */
    void saveWithDish(SetmealDto setmealDto);

    /**
     * 根据id删除套餐和包含的菜品
     * @param ids
     */
    void deleteWithDish(List<Long> ids);
}
