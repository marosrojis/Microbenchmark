package cz.rojik.backend.service;

import cz.rojik.backend.dto.user.RoleDTO;
import cz.rojik.backend.entity.RoleType;

import java.util.List;

public interface RoleService {

	RoleDTO getByType(String name);
	
	RoleDTO getByType(RoleType roleType);

	List<RoleDTO> findAll();
}
