package com.onlinestore.shopping_carts_service.feign;

import com.onlinestore.shopping_carts_service.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="users-service")
public interface IUserAPI {

    @PostMapping("/user/find/{id}")
    public UserDTO findByUserId(@PathVariable Long id);

    @PutMapping("/user/update")
    public String updateUser(@RequestBody UserDTO userDTO);

}
