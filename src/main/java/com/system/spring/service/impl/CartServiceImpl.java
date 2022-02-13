package com.system.spring.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.system.spring.entity.Cart;
import com.system.spring.entity.Item;
import com.system.spring.exception.ResourceNotFoundException;
import com.system.spring.repository.CartRepository;
import com.system.spring.service.CartService;

@Service
@SuppressWarnings("deprecation")
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
		Cart cartExisting = cartRepository.getById(cart.getId());
		if (cartExisting != null) {
			if (cart.getItems() != null) {
				Set<Item> setItems = cartExisting.getItems();
				if (setItems == null) {
					setItems = new HashSet<Item>();
				}
				for (Item item : cart.getItems()) {
					setItems.add(item);
				}
				cartExisting.setItems(setItems);
			}
			cartRepository.save(cartExisting);
		}
		return false;
	}

	@Override
	public boolean delete(Cart cart) {
		Cart cartExisting = cartRepository.getById(cart.getId());
		if (cartExisting == null) {
			throw new ResourceNotFoundException("Cart is not found", null);
		}
		cartRepository.delete(cart);
		return true;
	}
}