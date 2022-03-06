package com.system.spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.system.spring.entity.Cart;
import com.system.spring.entity.User;
import com.system.spring.repository.CartRepository;
import com.system.spring.service.CartService;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepository cartRepository;

	@Override
	public boolean save(Cart cart) {
		if (cart != null) {
			cartRepository.save(cart);
			return true;
		}
		return false;
	}

	@Override
	public boolean edit(Cart cart) {
		Cart cartExisting = cartRepository.getReferenceById(cart.getId());
		if (cartExisting != null) {
			cartRepository.save(cartExisting);
			return true;
		}
		return false;
	}

	@Override
	public boolean delete(Cart cart) {
		Cart cartExisting = cartRepository.getReferenceById(cart.getId());
		cartRepository.delete(cartExisting);
		return true;
	}

	@Override
	public Cart getCartFromUser(User user) {
		return cartRepository.getCartFromUser(user);
	}
}