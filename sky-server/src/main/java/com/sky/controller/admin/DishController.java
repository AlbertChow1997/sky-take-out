package com.sky.controller.admin;


import com.github.pagehelper.Page;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "Dish management interface")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;
    @PostMapping
    @ApiOperation("Dish add")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("Dish add：{}",dishDTO);
        dishService.saveWithFlavor(dishDTO);


        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("Dish Page query")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("Dish query：{}",dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation("Dish Batch delete")
    public Result delete(@RequestParam List<Long> ids){
        log.info("Dish Batch delete：{}",ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("Dish query by id")
    public Result<DishVO> getById(@PathVariable Long id){
        log.info("Dish query by id：{}",id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }
    @PutMapping
    @ApiOperation("Dish update")
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("Dish update：{}",dishDTO);
        dishService.updateWithFlavor(dishDTO);
        return Result.success();
    }
}
