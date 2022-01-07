package com.system.spring.service;

import com.system.spring.entity.Role;

public interface RoleService {

	boolean save(Role role);

	boolean edit(Role role);

	boolean delete(Role role);

	Role getRoleByName(String roleName);
}
