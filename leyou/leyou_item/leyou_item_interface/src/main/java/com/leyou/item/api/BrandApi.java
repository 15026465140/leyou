package com.leyou.item.api;


import com.leyou.item.pojo.Brand;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("brand")
@CrossOrigin
public interface BrandApi {

    //根据brand_id查询品牌名称
    @GetMapping("{id}")
    public Brand queryBrandById(@PathVariable("id") Long id);
}