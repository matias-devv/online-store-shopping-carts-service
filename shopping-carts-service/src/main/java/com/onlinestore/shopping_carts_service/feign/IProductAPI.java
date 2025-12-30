package com.onlinestore.shopping_carts_service.feign;

import com.onlinestore.shopping_carts_service.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name="products-service")
public interface IProductAPI {

    //get
    @GetMapping("/product/find/{code}")
    public ProductDTO findProductByCode(@PathVariable Long code);

    @GetMapping("/find")
    public List<ProductDTO> findProductsByCodes(@RequestBody List<Long> codes);


}
