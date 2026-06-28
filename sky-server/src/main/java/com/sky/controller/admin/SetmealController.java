package com.sky.controller.admin;


import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "Setmeal related interface")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @RequestMapping("/page")
    @ApiOperation("Setmeal query")
    public Result page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("Setmeal: {}", setmealPageQueryDTO);
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping
    @ApiOperation("Add setmeal")
    public Result save(@RequestBody SetmealDTO setmealDTO) {
        log.info("Add Setmeal: {}", setmealDTO);
        setmealService.saveWithDishes(setmealDTO);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("Delete setmeals")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("Delete Setmeal: {}", ids);
        setmealService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("Query setmeal by id")
    public Result<SetmealVO> getByIdWithDishes(@PathVariable Long id) {
        log.info("Query setmeal by id: {}", id);
        SetmealVO setmealVO = setmealService.getByIdWithDishes(id);
        return Result.success(setmealVO);
    }

    @PutMapping
    @ApiOperation("Edit setmeal")
    public Result update(@RequestBody SetmealDTO setmealDTO) {
        log.info("Edit Setmeal: {}", setmealDTO);
        setmealService.update(setmealDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("Change status")
    public Result startOrStop(@PathVariable Integer status, Long id){
        setmealService.startOrStop(status, id);
        return Result.success();
    }

}
