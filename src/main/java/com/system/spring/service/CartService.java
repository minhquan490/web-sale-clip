package com.system.spring.service;

import com.system.spring.entity.Cart;
import com.system.spring.entity.User;

public interface CartService {

	boolean save(Cart cart);

	boolean edit(Cart cart);

	boolean delete(Cart cart);

	Cart getCartFromUser(User user);
}
