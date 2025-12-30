package com.onlinestore.shopping_carts_service.model;

import com.onlinestore.shopping_carts_service.dto.ProductDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ShoppingCart {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_shopping_cart;

    private Long id_user;
    private BigDecimal total_price;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<ProductDTO> products;
}
