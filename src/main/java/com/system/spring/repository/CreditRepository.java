package com.system.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.system.spring.entity.Credit;

public interface CreditRepository extends JpaRepository<Credit, Long> {

}
