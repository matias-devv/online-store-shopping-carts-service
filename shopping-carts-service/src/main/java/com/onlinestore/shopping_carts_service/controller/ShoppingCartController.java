package com.onlinestore.shopping_carts_service.controller;

import com.onlinestore.shopping_carts_service.dto.ProductDTO;
import com.onlinestore.shopping_carts_service.dto.ShoppingCartDTO;
import com.onlinestore.shopping_carts_service.service.IShoppingCartService;
import jakarta.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shopping-cart")
public class ShoppingCartController {

    @Autowired
    private IShoppingCartService iShoppingCartService;

    @PostMapping("/create")
    public String createShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO){
        return iShoppingCartService.createShoppingCart(shoppingCartDTO);
    }

    @DeleteMapping("/delete/{code_product}/{shopping_cart_id}")
    public String deleteProductFromShoppingCart(@PathVariable Long code_product, @PathVariable Long shopping_cart_id){
        return iShoppingCartService.deleteProductFromShoppingCart(code_product, shopping_cart_id);
    }

    @PostMapping("/add-product/{id_shopping_cart}")
    public String addProductToShoppingCart(@RequestBody ProductDTO productDTO, @PathVariable Long shopping_cart_id){
        return iShoppingCartService.addProductToShoppingCart(productDTO, shopping_cart_id);
    }

    @GetMapping("/find/{id_shopping_cart}")
    public ShoppingCartDTO findById(@PathVariable Long id_shopping_cart){
        return iShoppingCartService.findById(id_shopping_cart);
    }

    @GetMapping("/find-all-ids")
    public List<Long> findAllShopingCartIds(){
        return iShoppingCartService.findAllShopingCartIds();
    }

}
