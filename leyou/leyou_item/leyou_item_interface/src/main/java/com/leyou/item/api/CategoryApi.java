package com.leyou.item.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("category")
@CrossOrigin
public interface CategoryApi {
    //根据三级分类id查询三级分类名称
    @GetMapping
    public List<String> queryNamesByIds(@RequestParam("ids")List<Long> ids);
}

