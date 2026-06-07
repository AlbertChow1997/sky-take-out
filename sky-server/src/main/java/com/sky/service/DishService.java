package com.sky.service;

import com.sky.dto.DishDTO;

public interface DishService {
    /**
     * Add dishes and its favor
     * @param dishDTO
     */
    public void saveWithFlavor(DishDTO dishDTO);
}
