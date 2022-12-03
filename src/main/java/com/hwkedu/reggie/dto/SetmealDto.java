package com.hwkedu.reggie.dto;


import com.hwkedu.reggie.entity.Setmeal;
import com.hwkedu.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
