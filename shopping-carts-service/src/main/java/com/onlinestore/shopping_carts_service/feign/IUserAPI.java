package com.onlinestore.shopping_carts_service.feign;

import com.onlinestore.shopping_carts_service.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="users-service")
public interface IUserAPI {

    //get
    @GetMapping("/user/find/{id}")
    public UserDTO findByUserId(@PathVariable Long id);
    //update
    @PutMapping("/user/update")
    public String updateUser(@RequestBody UserDTO userDTO);

}
