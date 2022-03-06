package com.system.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.system.spring.entity.Cart;
import com.system.spring.entity.User;

public interface CartRepository extends JpaRepository<Cart, Long> {

	@Query("SELECT c FROM Cart c INNER JOIN FETCH c.items WHERE c.user = :user")
	public Cart getCartFromUser(@Param("user") User user);
}
