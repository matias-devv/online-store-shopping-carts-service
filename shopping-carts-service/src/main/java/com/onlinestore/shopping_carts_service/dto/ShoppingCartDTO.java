package com.onlinestore.shopping_carts_service.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartDTO {

    private Long id_shopping_cart;
    private Long id_user;
    private BigDecimal total_price;

    private List<ProductDTO> products;

}
