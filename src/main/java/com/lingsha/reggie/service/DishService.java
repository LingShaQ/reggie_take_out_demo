package com.lingsha.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lingsha.reggie.dto.DishDto;
import com.lingsha.reggie.entity.Dish;

/**
 * @auther Ling.Sha
 * @date 2022/8/7 - 11:27
 */
public interface DishService extends IService<Dish>{

    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品、口味信息
    public DishDto getByIdWithFlavor(Long id);

    //更新菜品信息，同时更新对应口味信息
   public void updateWithFlavor(DishDto dishDto);
}
