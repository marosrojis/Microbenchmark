package cz.rojik.backend.service.impl;

import cz.rojik.backend.dto.user.RoleDTO;
import cz.rojik.backend.entity.RoleEntity;
import cz.rojik.backend.enums.RoleTypeEnum;
import cz.rojik.backend.repository.RoleRepository;
import cz.rojik.backend.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Service
public class RoleServiceImpl implements RoleService {

	private static Logger LOGGER = LoggerFactory.getLogger(RoleServiceImpl.class);

	@Autowired
    private RoleRepository roleRepository;

    /**
	 * Getting {@link RoleEntity} by its type
	 * @param type to find from
	 * @return the {@link RoleEntity} found
	 */
	@Override
	public RoleDTO getByType(String type) {
		LOGGER.trace("Get role by type {}", type);
		return new RoleDTO(roleRepository.findFirstByType(type));
	}

	/**
	 * Getting {@link RoleEntity} by its type (enum)
	 * @param roleType to find from
	 * @return the {@link RoleEntity} found
	 */
	@Override
	public RoleDTO getByType(RoleTypeEnum roleType) {
		LOGGER.trace("Get role by type {}", roleType);
		return new RoleDTO(roleRepository.findFirstByType(roleType.name()));
	}

	/**
	 * Return list of all {@link RoleDTO} 
	 * @return list of {@link RoleDTO} created
	 */
	@Override
	public List<RoleDTO> findAll() {
		LOGGER.trace("Find all roles");
		List<RoleEntity> roles = roleRepository.findAll();

		List<RoleDTO> output = new ArrayList<>();
		for (RoleEntity role : roles) {
			output.add(new RoleDTO(role));
		}
		return output;
	}

}
