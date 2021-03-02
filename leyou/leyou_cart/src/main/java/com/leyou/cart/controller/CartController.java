package com.leyou.cart.controller;

import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CartController {
    @Autowired
    private CartService cartService;

    //登录状态下添加购物车
    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart) {

        this.cartService.addCart(cart);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
   //登录状态下查询购物车
    @GetMapping
    public ResponseEntity<List<Cart>> queryCarts() {

       List<Cart> carts= this.cartService.queryCarts();

        if (carts == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

       return ResponseEntity.ok(carts);
    }
    //登录状态下购物车商品数量修改
    @PutMapping
    public ResponseEntity<Void> updateNum(@RequestBody Cart cart){
        this.cartService.updateCarts(cart);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{skuId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId") String skuId) {
        this.cartService.deleteCart(skuId);
        return ResponseEntity.ok().build();
    }
}
