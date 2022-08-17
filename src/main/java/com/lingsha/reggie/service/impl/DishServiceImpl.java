package com.lingsha.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingsha.reggie.dto.DishDto;
import com.lingsha.reggie.entity.Dish;
import com.lingsha.reggie.entity.DishFlavor;
import com.lingsha.reggie.mapper.DishMapper;
import com.lingsha.reggie.service.DishFlavorService;
import com.lingsha.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @auther Ling.Sha
 * @date 2022/8/7 - 11:27
 */
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService{
    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     * 新增菜品，同时保存口味数据
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
    //保存基本信息到菜品表 dish
    this.save(dishDto);
        Long dishId = dishDto.getId();
       List<DishFlavor> flavors = dishDto.getFlavors();
        flavors =  flavors.stream().map((item)->{item.setDishId(dishId);
            return item;
            }).collect(Collectors.toList());
        //保存基本信息到菜品表 dish_flavor
    dishFlavorService.saveBatch(flavors);
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {

        //查询基本表
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        //从口味表查询口味
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(list);

        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
    //更新dish表
    this.updateById(dishDto);
        //删除口味表
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        //更新口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors =  flavors.stream().map((item)->
        {
            item.setDishId(dishDto.getId());

            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }
}
