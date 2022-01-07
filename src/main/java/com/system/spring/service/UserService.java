package com.system.spring.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.system.spring.entity.User;
import com.system.spring.request.UserVo;

public interface UserService extends UserDetailsService {

	boolean save(UserVo userRequest);

	boolean edit(User user);

	boolean delete(User user);

	UserVo getUserFromUsername(String username);

	User getUserInformation(long userId);

	User getUserClip(long userId);

	User getUserHasClipsPurchased(long id);
}
