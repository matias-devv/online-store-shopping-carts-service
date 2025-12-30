package com.onlinestore.shopping_carts_service.service;

import com.onlinestore.shopping_carts_service.dto.ProductDTO;
import com.onlinestore.shopping_carts_service.dto.ShoppingCartDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IShoppingCartService {

    public String createShoppingCart(ShoppingCartDTO shoppingCartDTO);

    public String deleteProductFromShoppingCart(Long code_product, Long shopping_cart_id);

    public String addProductToShoppingCart(ProductDTO productDTO, Long shopping_cart_id);

    public ShoppingCartDTO findById(Long id_shopping_cart);

    public List<Long> findAllShopingCartIds();
}
