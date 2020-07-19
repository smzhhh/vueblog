package com.markerhub.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.markerhub.common.lang.Result;
import com.markerhub.entity.Ship;
import com.markerhub.service.ShipService;
import com.markerhub.util.ShiroUtil;
import io.jsonwebtoken.lang.Assert;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
public class ShipController {

    @Autowired
    ShipService shipService;

    @GetMapping("/ships")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage){

        Page page = new Page(currentPage, 10);
        IPage pageData = shipService.page(page, new QueryWrapper<Ship>().orderByDesc("created"));

        return Result.succ(pageData);
    }

    @GetMapping("/ship/{id}")
    public Result detail(@PathVariable(name = "id") Long id){

        Ship ship = shipService.getById(id);
        Assert.notNull(ship, "该船只已被删除");

        return Result.succ(ship);
    }

    @RequiresAuthentication
    @PostMapping("/ship/edit")
    public Result edit(@Validated @RequestBody Ship ship){

        Ship temp = null;
        if(ship.getId() != null){
            temp = shipService.getById(ship.getId());
            // 只能编辑自己的文章
            Assert.isTrue(temp.getUserId().longValue() == ShiroUtil.getProfile().getId().longValue(),"没有权限编辑！");

        } else {

            temp = new Ship();
            temp.setUserId(ShiroUtil.getProfile().getId());
            temp.setCreated(LocalDateTime.now());
            temp.setStatus(0);

        }

        BeanUtil.copyProperties(ship, temp, "id","userId","created","status");
        shipService.saveOrUpdate(temp);

        return Result.succ(null);
    }

    @RequiresAuthentication
    @PostMapping("/ship/delete")
    public Result delete(@Validated @RequestBody Ship ship){

        Ship temp = shipService.getById(ship.getId());
        // 只能删除自己创建的船只
        Assert.isTrue(temp.getUserId().longValue() == ShiroUtil.getProfile().getId().longValue(),"没有权限删除！");



        shipService.removeById(temp);

        return Result.succ(null);
    }
}
