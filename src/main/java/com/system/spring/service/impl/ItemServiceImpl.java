package com.system.spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.system.spring.entity.Item;
import com.system.spring.repository.ItemRepository;
import com.system.spring.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemRepository itemRepository;

	@Override
	public boolean save(Item item) {
		if (item != null) {
			itemRepository.save(item);
			return true;
		}
		return false;
	}

	@Override
	public boolean delete(Item item) {
		if (item != null) {
			itemRepository.delete(item);
			return true;
		}
		return false;
	}

	@Override
	public Item getItem(long id) {
		return itemRepository.getReferenceById(id);
	}
}