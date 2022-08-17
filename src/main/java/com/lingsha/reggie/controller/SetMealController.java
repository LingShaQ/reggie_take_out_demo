package com.lingsha.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingsha.reggie.common.R;
import com.lingsha.reggie.dto.SetmealDto;
import com.lingsha.reggie.entity.Category;
import com.lingsha.reggie.entity.Setmeal;
import com.lingsha.reggie.service.CategoryService;
import com.lingsha.reggie.service.SetMealDishService;
import com.lingsha.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @auther Ling.Sha
 * @date 2022/8/8 - 19:15
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetMealController {

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetMealDishService setMealDishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping()
    public R<String> save(@RequestBody SetmealDto setmealDto)
    {
        //log.info("套餐A:{}",setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name)
    {

        Page<Setmeal> pageInfo = new Page<>(page,pageSize);

        Page<SetmealDto> pageDto = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Setmeal::getName,name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo);

        //拷贝
        BeanUtils.copyProperties(pageInfo,pageDto,"records");

        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> setmealDtoList =  records.stream().map((item)->{
            SetmealDto setmealDto = new SetmealDto();
            Long categoryId = item.getCategoryId();
            //根据id查名称
            Category category = categoryService.getById(categoryId);
            if(category!=null)
            {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            //这时候dto里面有name，但是没有数据
            //所以我们把数据放到dto里面，dto继承了父类属性
            BeanUtils.copyProperties(item,setmealDto);
            return setmealDto;
        }).collect(Collectors.toList());

        pageDto.setRecords(setmealDtoList);
        return R.success(pageDto);
    }
    
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids)
    {
        log.info("ids:{}",ids);
        setmealService.removeWithDish(ids);
        return R.success("删除成功");
    }
}
