package cz.rojik.backend.service;

import cz.rojik.backend.dto.user.RoleDTO;
import cz.rojik.backend.enums.RoleTypeEnum;

import java.util.List;

public interface RoleService {

	/**
	 * Get role based on name
	 * @param name role name
	 * @return role object
	 */
	RoleDTO getByType(String name);

	/**
	 * Get role based on role type
	 * @param roleType role type
	 * @return role object
	 */
	RoleDTO getByType(RoleTypeEnum roleType);

	/**
	 * Get all roles
	 * @return list of roles
	 */
	List<RoleDTO> findAll();
}
