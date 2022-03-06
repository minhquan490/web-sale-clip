package com.system.spring.controller;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.system.spring.config.ApiConfig;
import com.system.spring.details.UserDetails;
import com.system.spring.entity.Cart;
import com.system.spring.entity.Clip;
import com.system.spring.entity.Item;
import com.system.spring.entity.User;
import com.system.spring.exception.UnknowException;
import com.system.spring.request.AddToCartRequest;
import com.system.spring.response.ServerResponse;
import com.system.spring.service.CartService;
import com.system.spring.service.ClipService;
import com.system.spring.service.ItemService;

@Controller
@RequestMapping(ApiConfig.CART_PATH)
public class CartController {

	@Autowired
	private CartService cartService;

	@Autowired
	private ClipService clipService;

	@Autowired
	private ItemService itemService;

	@PreAuthorize("hasAnyAuthority('viewer', 'actor')")
	@PutMapping(ApiConfig.ADD_TO_CART)
	@ResponseBody
	public ResponseEntity<ServerResponse> addToCart(@RequestBody(required = false) AddToCartRequest request)
			throws NullPointerException {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userDetails.getUser();
		Cart cart = cartService.getCartFromUser(user);
		Clip clip = clipService.get(Long.valueOf(request.getClipId()));
		Item item = new Item(1, clip.getPrice(), clip);
		Set<Item> items = cart.getItems() == null ? new HashSet<>() : cart.getItems();
		items.add(item);
		cart.setItems(items);
		if (cartService.edit(cart)) {
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new ServerResponse(LocalDateTime.now(), HttpStatus.CREATED, items));
		} else {
			throw new UnknowException("Error", null);
		}
	}

	@PreAuthorize("hasAnyAuthority('viewer', 'actor', 'admin')")
	@DeleteMapping(ApiConfig.REMOVE_ITEM + "/{id}")
	public ResponseEntity<ServerResponse> deleteItem(@PathVariable(name = "id") String id) {
		Item item = itemService.getItem(Long.valueOf(id));
		if (itemService.delete(item)) {
			return ResponseEntity.ok(new ServerResponse(LocalDateTime.now(), HttpStatus.OK, "Delete success"));
		} else {
			throw new UnknowException("Error", null);
		}
	}
}
