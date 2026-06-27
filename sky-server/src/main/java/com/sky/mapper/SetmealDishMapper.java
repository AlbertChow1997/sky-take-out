package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * get setmeal ids by dish ids
     * @param dishIds
     * @return
     */

    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);

    // Batch insert the relationship between setmeales and dishes
    void insertBatch(List<SetmealDish> setmealDishes);
}
