package com.system.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.system.spring.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
