package com.lingsha.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lingsha.reggie.entity.Category;

/**
 * @auther Ling.Sha
 * @date 2022/8/7 - 9:48
 */
public interface CategoryService extends IService<Category> {

    public void remove(Long id);
}
