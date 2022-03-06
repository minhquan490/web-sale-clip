package com.system.spring.service;

import com.system.spring.entity.Item;

public interface ItemService {

	boolean save(Item item);

	boolean delete(Item item);

	Item getItem(long id);
}
