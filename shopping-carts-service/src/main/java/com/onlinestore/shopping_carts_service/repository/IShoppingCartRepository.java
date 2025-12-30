package com.onlinestore.shopping_carts_service.repository;

import com.onlinestore.shopping_carts_service.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {

}
