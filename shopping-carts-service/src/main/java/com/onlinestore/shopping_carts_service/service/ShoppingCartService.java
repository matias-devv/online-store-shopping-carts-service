package com.onlinestore.shopping_carts_service.service;

import com.onlinestore.shopping_carts_service.dto.ProductDTO;
import com.onlinestore.shopping_carts_service.dto.ShoppingCartDTO;
import com.onlinestore.shopping_carts_service.dto.UserDTO;
import com.onlinestore.shopping_carts_service.feign.IProductAPI;
import com.onlinestore.shopping_carts_service.feign.IUserAPI;
import com.onlinestore.shopping_carts_service.model.ShoppingCart;
import com.onlinestore.shopping_carts_service.repository.IShoppingCartRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.ServiceUnavailableException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShoppingCartService implements IShoppingCartService {

    @Autowired
    private IShoppingCartRepository iShoppingCartRepository;

    @Autowired
    private IProductAPI iProductAPI;

    @Autowired
    private IUserAPI iUserAPI;

    @Override
    public String createShoppingCart(ShoppingCartDTO shoppingCartDTO) {

        //save entity
        ShoppingCart shoppingCart = this.convertDtoToShoppingCartEntity(shoppingCartDTO);

        //set single price and name to the list of products inside the "shoppingCart"
        shoppingCart = this.setSinglePriceAndNameToProducts(shoppingCart);

        //actualize the total price
        shoppingCart = this.actualizeTotalPrice(shoppingCart);

        //save the entity
        iShoppingCartRepository.save(shoppingCart);

        //setting the id of the shopping cart to the user asigned in the DTO
        this.setThisShoppingCartToUser(shoppingCart);

        return "The shopping cart has been created";
    }

    @CircuitBreaker(name ="users-service", fallbackMethod = "fallbackSetThisShoppingCartToUser")
    @Retry(name = "users-service")
    private void setThisShoppingCartToUser(ShoppingCart shoppingCart) {

        UserDTO userDTO = iUserAPI.findByUserId( shoppingCart.getId_user() );
        if( userDTO != null ) {

            //the user will have a list of shopping cart IDs, they may already have IDs loaded or not
            //I'm adding this one I just created to their list.
            List<Long> shoppingCartsOfUser = userDTO.getIds_shopping_cart();

            if( shoppingCartsOfUser != null ) {
                shoppingCartsOfUser.add( shoppingCart.getId_shopping_cart() );
            }
            else{
                // If the user doesn't have a list of shopping cart IDs,
                // I create a list, add the ID of this shopping cart, and set that for the user.
                List<Long> shoppinCarts = new ArrayList<>();

                shoppinCarts.add( shoppingCart.getId_shopping_cart() );

                userDTO.setIds_shopping_cart( shoppinCarts );
            }
            //update
            iUserAPI.updateUser(userDTO);
        }
    }

    public void fallbackSetThisShoppingCartToUser(Throwable throwable) throws ServiceUnavailableException {
        throw new ServiceUnavailableException("the microservice: users-service is temporarily closed, method: setThisShoppingCartToUser");
    }

    private List<Long> getProductCodesFromShoppingCart(ShoppingCart shoppingCart) {

        List<ProductDTO> products = shoppingCart.getProducts();
        List<Long> product_codes = new ArrayList<>();

        for(ProductDTO pr : products){
            product_codes.add(pr.getCode());
        }
        return product_codes;
    }

    @CircuitBreaker(name="products-service", fallbackMethod = "fallbackSetSinglePriceAndNameToProducts")
    @Retry(name="products-service")
    private ShoppingCart setSinglePriceAndNameToProducts(ShoppingCart shoppingCart) {

        //search for products using the codes received in the DTO
        List<Long> product_codes = this.getProductCodesFromShoppingCart(shoppingCart);
        List<ProductDTO> productsFromAPI = iProductAPI.findProductsByCodes(product_codes);

        //I grab the entity's product list
        List<ProductDTO> listToReturn = shoppingCart.getProducts();

        if( listToReturn == null || productsFromAPI == null ) {
            return null;
        }

        //the "single_price" and "name" attributes are empty in the ShoppingCart(Entity) product list.
        //so I'm iterating and setting these attributes.
        for (ProductDTO product : listToReturn) {

            for (ProductDTO productFromAPI : productsFromAPI) {

                if (productFromAPI.getCode().equals(product.getCode())) {
                    product.setSingle_price(productFromAPI.getSingle_price());
                    product.setName(productFromAPI.getName());
                }
            }
        }
        //set the list with all attributes complete
        shoppingCart.setProducts(listToReturn);
        return shoppingCart;
    }

    public ShoppingCart fallbackSetSinglePriceAndNameToProducts(Throwable throwable){
        return new ShoppingCart( 9999999L, 999999L, null, null);
    }

    @Override
    public String deleteProductFromShoppingCart(Long code_product, Long shopping_cart_id) {

        ShoppingCart shoppingCart = iShoppingCartRepository.findById(shopping_cart_id).orElse(null);

        if( shoppingCart == null ) {
            return "The shopping cart does not exist";
        }

        List<ProductDTO> products = shoppingCart.getProducts();
        if( products == null ) {
            return "The shopping cart does not have any products";
        }

        for(ProductDTO product : products) {

                if( product.getCode().equals(code_product) ) {

                    //remove the product from the list
                    products.remove(product);
                    shoppingCart.setProducts(products);

                    //actualize the total price
                    shoppingCart = this.actualizeTotalPrice(shoppingCart);

                    //save
                    iShoppingCartRepository.save(shoppingCart);
                    return "The product from the shopping cart has been deleted";
                }
                return "That product does not exist in the shopping cart";
        }
        return "The product code was not found in the product list";
    }

    @Override
    @CircuitBreaker(name="products-service", fallbackMethod = "fallbackAddProductToShoppingCart")
    @Retry(name="products-service")
    public String addProductToShoppingCart(ProductDTO productDTO, Long shopping_cart_id) {


        //I'm looking for the shopping cart and the product to add
        ShoppingCart shoppingCart = iShoppingCartRepository.findById(shopping_cart_id).orElse(null);

        //Get the list of products of this shopping cart
        List<ProductDTO> products = shoppingCart.getProducts();

        //The product to add
        ProductDTO productFound = iProductAPI.findProductByCode( productDTO.getCode() );

        if( shoppingCart != null && productFound != null && products != null ) {

            //the productDTO will already have the ID and quantity
            //it's missing the individual price and name, so I'm setting these attributes
            productDTO.setSingle_price( productFound.getSingle_price() );
            productDTO.setName( productFound.getName() );

            //add
            products.add( productDTO );
            shoppingCart.setProducts(products);

            //actualize the total price of the products
            shoppingCart = this.actualizeTotalPrice(shoppingCart); //<-- se rompe aca

            //save
            iShoppingCartRepository.save(shoppingCart);
            return "The product was saved successfully";
        }
        return "The shopping cart does not exist, the id it's wrong or the product does not exist";
    }

    public String fallbackAddProductToShoppingCart(Throwable throwable) {
        return "The microservice: product-service it's temporarily closed";
    }

    private ShoppingCart actualizeTotalPrice(ShoppingCart shoppingCart) {

        List<ProductDTO> products =  shoppingCart.getProducts();

        BigDecimal totalPrice = BigDecimal.ZERO;
        shoppingCart.setTotal_price(BigDecimal.ZERO);

        if(products == null) {
            return null;
        }

        for(ProductDTO product : products) {
                //I take the quantity of the current product
                BigDecimal quantity = BigDecimal.valueOf( product.getQuantity());
                //I multiply the "single price" with the "quantity" and the result add it to the "totalPrice"
                totalPrice = totalPrice.add( product.getSingle_price().multiply(quantity) );
        }

        shoppingCart.setTotal_price(totalPrice);

        return shoppingCart;
    }

    @Override
    public ShoppingCartDTO findById(Long id_shopping_cart) {
        ShoppingCart shoppingCart = iShoppingCartRepository.findById(id_shopping_cart).orElse(null);
        if(shoppingCart != null) {
            return convertEntityToDTO(shoppingCart);
        }
        return null;
    }

    private ShoppingCartDTO convertEntityToDTO(ShoppingCart shoppingCart) {
        ShoppingCartDTO dto = new ShoppingCartDTO();
        dto.setId_shopping_cart(shoppingCart.getId_shopping_cart());
        dto.setId_user(shoppingCart.getId_user());
        dto.setTotal_price(shoppingCart.getTotal_price());
        dto.setProducts(shoppingCart.getProducts());
        return dto;
    }

    @Override
    public List<Long> findAllShopingCartIds() {
        List<ShoppingCart> list = iShoppingCartRepository.findAll();
        List<Long> shoppingCartIds = new ArrayList<>();

        for(ShoppingCart sc : list){
            shoppingCartIds.add( sc.getId_shopping_cart() );
        }
        return shoppingCartIds;
    }

    private ShoppingCart convertDtoToShoppingCartEntity(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId_user(shoppingCartDTO.getId_user());
        shoppingCart.setTotal_price(shoppingCartDTO.getTotal_price());
        shoppingCart.setProducts(shoppingCartDTO.getProducts());
        return shoppingCart;
    }
}
