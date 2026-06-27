package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * Add a new dish and its flavors
     * @param dishDTO
     */
    @Transactional
    @Override
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();

        BeanUtils.copyProperties(dishDTO, dish);

        // add a new dish and its flavors
        dishMapper.insert(dish);
        log.info("Dish added successfully: {}", dishDTO.toString());

        Long dishId = dish.getId();

        // add some favors
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && flavors.size() > 0){
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            // Insert some flavors
            dishFlavorMapper.insertBatch(flavors);
            log.info("Dish and its flavors added successfully: {}", dishDTO.toString());
        }

    }

    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * Batch delete
     * Rules:
     * 1. The dish must not be on sale
     * 2. The dish must not have any setmeal
     * 3. Every delete operation can apply on one or more dishes
     * 4. Dish flavors must be deleted when the dish is deleted
     *
     * @param ids
     */
    @Override
    public void deleteBatch(List<Long> ids) {
        // Check if the dish is being selling now
        for(Long id : ids){
            Dish dish = dishMapper.getById(id);
            if(dish.getStatus() == StatusConstant.ENABLE){
                throw new RuntimeException("Dish is being sold now, please stop selling first");
            }

        }

        // Check if the dish has any setmeal
        List<Long> setmealIdsB = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if(setmealIdsB != null && setmealIdsB.size() > 0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        // Delete the dish
//        for (Long id : ids) {
//            dishMapper.deleteById(id);
//            // Delete the dish flavors
//            dishFlavorMapper.deleteByDishId(id);
//        }

        // Delete the dish by ids
        dishMapper.deleteByIds(ids);
        // Delete flavors related to the dish
        dishFlavorMapper.deleteByDishIds(ids);
    }

    @Override
    public DishVO getByIdWithFlavor(Long id) {
        // Get dish by id
        Dish dish = dishMapper.getById(id);

        // Get flavors
        List<DishFlavor> flavors = dishFlavorMapper.getByDishId(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(flavors);

        return dishVO;
    }

    /**
     * Update dish and its flavors
     * @param dishDTO
     */
    @Transactional
    @Override
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        // Update basic info of dish table
        dishMapper.update(dish);

        // Delete the original flavors
        dishFlavorMapper.deleteByDishId(dishDTO.getId());

        // Add new flavors
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && flavors.size() > 0){
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishDTO.getId());
            });
            // Insert new flavors
            dishFlavorMapper.insertBatch(flavors);
        }

    }

    /**
     * Query dish by category
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> list(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.list(dish);
    }
}
