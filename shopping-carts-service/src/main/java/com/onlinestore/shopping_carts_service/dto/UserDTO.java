package com.onlinestore.shopping_carts_service.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User information")
public class UserDTO {

    @Schema(description = "Unique user ID", example = "100")
    private Long id;

    @Schema(description = "Username", example = "john_doe")
    private String username;

    @Schema(description = "National Identity Document", example = "12345678")
    private String dni;

    @Schema(description = "User email address", example = "john.doe@example.com")
    private String gmail;

    @Schema(description = "List of shopping cart IDs associated with the user")
    private List<Long> ids_shopping_cart;
}