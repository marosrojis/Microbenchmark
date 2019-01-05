package cz.rojik.backend.service;

import cz.rojik.backend.dto.user.RoleDTO;
import cz.rojik.backend.enums.RoleTypeEnum;

import java.util.List;

public interface RoleService {

	RoleDTO getByType(String name);
	
	RoleDTO getByType(RoleTypeEnum roleType);

	List<RoleDTO> findAll();
}
