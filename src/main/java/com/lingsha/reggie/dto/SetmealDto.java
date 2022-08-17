package com.lingsha.reggie.dto;

import com.lingsha.reggie.entity.Setmeal;
import com.lingsha.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
