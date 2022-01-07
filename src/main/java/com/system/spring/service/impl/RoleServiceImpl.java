package com.system.spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.system.spring.entity.Role;
import com.system.spring.exception.ResourceNotFoundException;
import com.system.spring.repository.RoleRepository;
import com.system.spring.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public boolean save(Role role) {
		if (role != null) {
			roleRepository.save(role);
			return true;
		}
		return false;
	}

	@Override
	public boolean edit(Role role) {
		Role existingRole = roleRepository.getById(role.getId());
		if (existingRole == null) {
			throw new ResourceNotFoundException("Role is not exist");
		} else {
			existingRole.setRoleName(role.getRoleName());
			roleRepository.save(existingRole);
			return true;
		}
	}

	@Override
	public boolean delete(Role role) {
		if (role == null) {
			return false;
		} else {
			roleRepository.delete(role);
			return true;
		}
	}

	@Override
	public Role getRoleByName(String roleName) {
		Role role = roleRepository.getRoleByName(roleName);
		if (role != null) {
			return role;
		}
		return null;
	}

}
