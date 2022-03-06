package com.system.spring.service;

import com.system.spring.entity.Credit;
import com.system.spring.entity.User;

public interface CreditService {

	boolean save(Credit credit);

	boolean delete(Credit credit);

	boolean edit(Credit credit);

	Credit getCreditFromUser(User user);

	Credit get(long id);
}
