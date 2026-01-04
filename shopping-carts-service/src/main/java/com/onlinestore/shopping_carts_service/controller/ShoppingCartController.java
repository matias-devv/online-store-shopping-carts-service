package com.onlinestore.shopping_carts_service.controller;

import com.onlinestore.shopping_carts_service.dto.ProductDTO;
import com.onlinestore.shopping_carts_service.dto.ShoppingCartDTO;
import com.onlinestore.shopping_carts_service.service.IShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shopping-cart")
@Tag(name = "Shopping Cart", description = "API for shopping cart management")
public class ShoppingCartController {

    @Autowired
    private IShoppingCartService iShoppingCartService;

    @PostMapping("/create")
    @Operation(
            summary = "Create a shopping cart",
            description = "Creates a new shopping cart with products. Validates products against external service, " +
                    "calculates total prices and associates the cart with the specified user."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Shopping cart created successfully",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data"
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Products or users service unavailable"
            )
    })
    public String createShoppingCart(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Shopping cart data to create. Must include user ID and product list with their codes and quantities.",
            required = true)
            @RequestBody ShoppingCartDTO shoppingCartDTO){
        return iShoppingCartService.createShoppingCart(shoppingCartDTO);
    }

    @DeleteMapping("/delete/{code_product}/{shopping_cart_id}")
    @Operation(
            summary = "Remove product from cart",
            description = "Removes a specific product from an existing shopping cart and recalculates the total price. " +
                    "If the product is found and removed, the cart's total price is automatically updated."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operation completed. Check response message for specific result.",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Success",
                                            value = "The product from the shopping cart has been deleted",
                                            description = "Product was found and successfully removed"
                                    ),
                                    @ExampleObject(
                                            name = "Cart not found",
                                            value = "The shopping cart does not exist",
                                            description = "Shopping cart ID doesn't exist in database"
                                    ),
                                    @ExampleObject(
                                            name = "Empty cart",
                                            value = "The shopping cart does not have any products",
                                            description = "Shopping cart exists but contains no products"
                                    ),
                                    @ExampleObject(
                                            name = "Product not found",
                                            value = "The product code was not found in the product list",
                                            description = "Product code doesn't exist in this shopping cart"
                                    )
                            }
                    )
            )
    })
    public String deleteProductFromShoppingCart(
            @Parameter(
                    description = "Product code to remove from the cart",
                    required = true,
                    example = "12"
            )
            @PathVariable Long code_product,

            @Parameter(
                    description = "ID of the shopping cart containing the product",
                    required = true,
                    example = "1"
            )
            @PathVariable Long shopping_cart_id
    ){
        return iShoppingCartService.deleteProductFromShoppingCart(code_product, shopping_cart_id);
    }


    @PutMapping("/add/{id_shopping_cart}")
    @Operation(
            summary = "Add product to cart",
            description = "Adds a product to an existing shopping cart. If the product already exists in the cart, " +
                    "the quantity is accumulated. Product details (name, price) are fetched from the products service."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operation completed. Check response message for specific result.",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Success",
                                            value = "The product was saved successfully",
                                            description = "Product added or quantity updated successfully"
                                    ),
                                    @ExampleObject(
                                            name = "Not found",
                                            value = "The shopping cart does not exist, the id it's wrong or the product does not exist",
                                            description = "Shopping cart or product not found"
                                    ),
                                    @ExampleObject(
                                            name = "Service unavailable",
                                            value = "The microservice: product-service it's temporarily closed",
                                            description = "Products service is currently unavailable (circuit breaker activated)"
                                    )
                            }
                    )
            )
    })
    public String addProductToShoppingCart(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Product to add. Only 'code' and 'quantity' are required. Name and price are fetched automatically.",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ProductDTO.class),
                            examples = @ExampleObject(
                                    name = "Add product example",
                                    value = "{\"code\": 12345, \"quantity\": 3}"
                            )
                    )
            )
            @RequestBody ProductDTO productDTO,

            @Parameter(
                    description = "Shopping cart ID to add the product to",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id_shopping_cart
    ){
        return iShoppingCartService.addProductToShoppingCart(productDTO, id_shopping_cart);
    }


    @GetMapping("/find/{id_shopping_cart}")
    @Operation(summary = "Find shopping cart by ID")
    @ApiResponse(
            responseCode = "200",
            description = "Shopping cart found",
            content = @Content(schema = @Schema(implementation = ShoppingCartDTO.class))
    )
    public ShoppingCartDTO findById(
            @Parameter(description = "Shopping cart ID", required = true, example = "1")
            @PathVariable Long id_shopping_cart
    ){
        return iShoppingCartService.findById(id_shopping_cart);
    }

    @GetMapping("/find-all-ids")
    @Operation(summary = "Get all shopping cart IDs")
    @ApiResponse(
            responseCode = "200",
            description = "List of all shopping cart IDs",
            content = @Content(schema = @Schema(implementation = Long.class, type = "array"))
    )
    public List<Long> findAllShopingCartIds(){
        return iShoppingCartService.findAllShopingCartIds();
    }
}
