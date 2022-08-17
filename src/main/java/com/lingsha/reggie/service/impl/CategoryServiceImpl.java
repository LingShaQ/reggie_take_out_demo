package com.lingsha.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingsha.reggie.common.CustomException;
import com.lingsha.reggie.common.R;
import com.lingsha.reggie.entity.Category;
import com.lingsha.reggie.entity.Dish;
import com.lingsha.reggie.entity.Setmeal;
import com.lingsha.reggie.mapper.CategoryMapper;
import com.lingsha.reggie.service.CategoryService;
import com.lingsha.reggie.service.DishService;
import com.lingsha.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @auther Ling.Sha
 * @date 2022/8/7 - 9:48
 */
@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    /**
     * 根据id删除分类，并且进行判断是否关联了菜品
     * @param id
     */
    @Override
    public void remove(Long id) {
        //查询分类是否关联菜品,如果关联，抛出业务异常
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(queryWrapper);
        if(count1>0)
        {
            //不可删除,抛出异常  关联菜品了
            throw new CustomException("存在关联菜品，不可删除");
        }
        //查询分类是否关联 套餐 ,如果关联，抛出业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if(count2>0)
        {
            //不可删除,抛出异常  ,关联套餐了
            throw new CustomException("存在关联套餐，不可删除");
        }

        //正常删除
        super.removeById(id);
    }



}
