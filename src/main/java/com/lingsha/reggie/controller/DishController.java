package com.lingsha.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingsha.reggie.common.R;
import com.lingsha.reggie.dto.DishDto;
import com.lingsha.reggie.entity.Category;
import com.lingsha.reggie.entity.Dish;
import com.lingsha.reggie.entity.DishFlavor;
import com.lingsha.reggie.service.CategoryService;
import com.lingsha.reggie.service.DishFlavorService;
import com.lingsha.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜品管理
 * @auther Ling.Sha
 * @date 2022/8/8 - 8:55
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {


    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishService dishService;

    @PostMapping()
    public R<String> save(@RequestBody DishDto dishDto)
    {
        //log.info("DishDto的内容:{}",dishDto);
        dishService.saveWithFlavor(dishDto);
        return R.success("传输成功");
    }

    /**
     * 菜品分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name)
    {
        //构造分页构造器
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Dish::getName,name);
        //排序
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo, queryWrapper);


        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");//这里要处理records集合
        List<Dish> records = pageInfo.getRecords();//获得我们的集合
        List<DishDto> list =  records.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);

            Long id = item.getCategoryId();
            Category category = categoryService.getById(id);
            String categoryName = category.getName();

            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> getDish(@PathVariable("id") Long id)
    {

    return R.success(dishService.getByIdWithFlavor(id));
    }

    /**
     * 更新菜品xinxi
     * @return
     */
    @PutMapping()
    public R<String> updateDish(@RequestBody DishDto dishDto)
    {
        //涉及两张表
        log.info("DishDto的内容:{}",dishDto);
        dishService.updateWithFlavor(dishDto);
        return R.success("更新菜品成功");
    }

    /**
     * 根据条件查询对应菜品
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish)
    {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        //1是正在出售
        queryWrapper.eq(Dish::getStatus,1);//where status =1;
        //排序
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);



        List<DishDto> dishDtos  =  list.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);

            Long id = item.getCategoryId();
            Category category = categoryService.getById(id);

            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);

            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> dishWrapper = new LambdaQueryWrapper();
            dishWrapper.eq(DishFlavor::getDishId,dishId);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(dishWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());


        return R.success(dishDtos);
    }
}
