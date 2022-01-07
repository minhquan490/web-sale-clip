package com.system.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.system.spring.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
