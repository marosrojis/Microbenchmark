package cz.rojik.service;

import cz.rojik.dto.user.RoleDTO;
import cz.rojik.entity.RoleType;

import java.util.List;

public interface RoleService {

	RoleDTO getByType(String name);
	
	RoleDTO getByType(RoleType roleType);

	List<RoleDTO> findAll();
}
