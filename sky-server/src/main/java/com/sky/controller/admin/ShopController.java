package com.sky.controller.admin;


import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "Api for shop")
@Slf4j
public class ShopController {

    public static final String KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * Set shop status open or close
     * @param status
     * @return
     */

    @PutMapping("/{status}")
    @ApiOperation("Set shop status")
    public Result setStatus(@PathVariable Integer status){
        log.info("Set shop status: {}",status == 1 ? "营业中" : "打烊中");
        redisTemplate.opsForValue().set(KEY,status);
        return Result.success(status);
    }

    /**
     * Get shop status
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("Get shop status")
    public Result<Integer> getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("Get shop status: {}", status == 1 ? "营业中" : "打烊中");
        return Result.success(status);
    }
}
