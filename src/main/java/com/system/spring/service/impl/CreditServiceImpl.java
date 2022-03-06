package com.system.spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.system.spring.entity.Credit;
import com.system.spring.entity.User;
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

	@Override
	public Credit getCreditFromUser(User user) {
		return creditRepository.getCreditFromUser(user);
	}

	@Override
	public boolean edit(Credit newCredit) {
		if (newCredit != null) {
			Credit oldCredit = new Credit();
			oldCredit.setCreditNumber(newCredit.getCreditNumber());
			oldCredit.setPassCode(newCredit.getPassCode());
			creditRepository.save(oldCredit);
			return true;
		}
		return false;
	}

	@Override
	public Credit get(long id) {
		return creditRepository.getReferenceById(id);
	}
}