package com.system.spring.service.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.system.spring.entity.Role;
import com.system.spring.entity.User;
import com.system.spring.exception.ResourceNotFoundException;
import com.system.spring.repository.UserRepository;
import com.system.spring.request.UserVo;
import com.system.spring.service.RoleService;
import com.system.spring.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleService roleService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.getUserFromUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User '" + username + "' not found.");
		}
		return new com.system.spring.details.UserDetails(user);
	}

	@Override
	public boolean save(UserVo userRequest) {
		if (userRequest != null) {
			Set<Role> roles = new HashSet<Role>();
			for (String role : userRequest.getRoles()) {
				roles.add(roleService.getRoleByName(role));
			}
			userRepository.save(new User(userRequest.getEmail(), userRequest.getUsername(),
					new BCryptPasswordEncoder().encode(userRequest.getPassword()), roles));
			return true;
		}

		return false;
	}

	@Override
	public boolean edit(User user) {
		User existingUser = userRepository.getById(user.getId());
		if (existingUser == null) {
			throw new ResourceNotFoundException("User is not found");
		}
		existingUser.setFirstName(user.getFirstName());
		existingUser.setLastName(user.getLastName());
		existingUser.setGender(user.getGender());
		existingUser.setBirthDate(user.getBirthDate());
		existingUser.setPremium(user.isPremium());
		existingUser.setEnabled(user.isEnabled());
		userRepository.save(existingUser);
		return true;
	}

	@Override
	public boolean delete(User user) {
		if (user == null) {
			return false;
		} else {
			userRepository.delete(user);
			return true;
		}
	}

	@Override
	public UserVo getUserFromUsername(String username) {
		User user = userRepository.getUserFromUsername(username);
		if (user != null) {
			Set<Role> roles = user.getRoles();
			Set<String> roleNames = roles.stream().map(r -> r.getRoleName()).collect(Collectors.toSet());
			UserVo userVo = new UserVo(user.getUsername(), user.getEmail(), roleNames);
			return userVo;
		}
		return null;
	}

	@Override
	public User getUserInformation(long userId) {
		return userRepository.getUserInformation(userId);
	}

	@Override
	public User getUserClip(long userId) {
		return userRepository.getUserClip(userId);
	}

	@Override
	public User getUserHasClipsPurchased(long id) {
		return userRepository.getUserHasClipsPurchased(id);
	}
}
