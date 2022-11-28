package com.hwkedu.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hwkedu.reggie.entity.Category;

/**
 * @author 侯文柯
 * @version 1.0
 */
public interface CategoryService extends IService<Category> {
    /**
     * 根据条件判断是否删除分类
     * @param id
     */
    void remove(Long id);
}
