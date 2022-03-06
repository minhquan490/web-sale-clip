package com.system.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.system.spring.entity.Credit;
import com.system.spring.entity.User;

public interface CreditRepository extends JpaRepository<Credit, Long> {

	@Query("SELECT c FROM Credit c WHERE c.user = :user")
	public Credit getCreditFromUser(@Param("user") User user);

}
