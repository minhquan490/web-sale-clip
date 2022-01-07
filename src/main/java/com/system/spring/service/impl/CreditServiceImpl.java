package com.system.spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.system.spring.entity.Credit;
import com.system.spring.repository.CreditRepository;
import com.system.spring.service.CreditService;

@Service
public class CreditServiceImpl implements CreditService {

	@Autowired
	private CreditRepository creditRepository;

	@Override
	public boolean save(Credit credit) {
		if (credit != null) {
			creditRepository.save(credit);
			return true;
		}
		return false;
	}

	@Override
	public boolean delete(Credit credit) {
		if (credit != null) {
			creditRepository.delete(credit);
			return true;
		}
		return false;
	}
}