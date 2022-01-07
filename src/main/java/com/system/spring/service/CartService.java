package com.system.spring.service;

import com.system.spring.entity.Cart;

public interface CartService {

	boolean save(Cart cart);

	boolean edit(Cart cart);

	boolean delete(Cart cart);
}
