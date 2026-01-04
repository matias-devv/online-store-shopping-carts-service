package com.onlinestore.shopping_carts_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Product information within the shopping cart")
public class ProductDTO {

    @Schema(description = "Unique product code", example = "12345", required = true)
    private Long code;

    @Schema(description = "Product name", example = "Laptop Dell XPS 15", accessMode = Schema.AccessMode.READ_ONLY)
    private String name;

    @Schema(description = "Unit price of the product", example = "1299.99", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal single_price;

    @Schema(description = "Product quantity in the cart", example = "2", required = true, minimum = "1")
    private Integer quantity;
}