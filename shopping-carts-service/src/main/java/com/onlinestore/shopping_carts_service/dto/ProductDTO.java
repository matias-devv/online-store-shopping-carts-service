package com.onlinestore.shopping_carts_service.dto;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ProductDTO {

    private Long code;
    private String name;
    private BigDecimal single_price;
    private Integer quantity;
}
