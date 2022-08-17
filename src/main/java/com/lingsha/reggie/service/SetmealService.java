package com.lingsha.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lingsha.reggie.dto.SetmealDto;
import com.lingsha.reggie.entity.Setmeal;

import java.util.List;

/**
 * @auther Ling.Sha
 * @date 2022/8/7 - 11:30
 */
public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐，以及套餐和菜品关联数据
     * @param ids
     */
    public void removeWithDish(List<Long> ids);
}
