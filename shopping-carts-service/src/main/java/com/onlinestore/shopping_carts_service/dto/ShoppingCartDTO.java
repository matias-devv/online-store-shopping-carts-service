package com.onlinestore.shopping_carts_service.dto;


import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Shopping cart data")
public class ShoppingCartDTO {

    @Schema(description = "Shopping cart ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id_shopping_cart;

    @Schema(description = "User ID who owns the cart", example = "100")
    private Long id_user;

    @Schema(description = "List of products in the cart. Only 'code' and 'quantity' are required on creation.")
    private List<ProductDTO> products;

    @Schema(description = "Total cart price (sum of all products)", example = "2599.98", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal total_price;
}
