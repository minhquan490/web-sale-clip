package com.system.spring.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.system.spring.entity.User;
import com.system.spring.request.UserVo;

public interface UserService extends UserDetailsService {

	boolean save(UserVo userRequest);

	boolean edit(User user);

	boolean delete(User user);

	List<User> getAllUser();

	UserVo getUserFromUsername(String username);

	User getUserInformation(long userId);

	User getUserClip(long userId);

	User getUserHasClipsPurchased(long id);
}
