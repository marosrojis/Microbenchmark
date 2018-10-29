package cz.rojik.service.impl;

import cz.rojik.dto.user.RoleDTO;
import cz.rojik.entity.RoleEntity;
import cz.rojik.entity.RoleType;
import cz.rojik.repository.RoleRepository;
import cz.rojik.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
    private RoleRepository roleRepository;

    /**
	 * Getting {@link RoleEntity} by its type
	 * @param type to find from
	 * @return the {@link RoleEntity} found
	 */
	@Override
	public RoleDTO getByType(String type) {
		return new RoleDTO(roleRepository.findFirstByType(type));
	}

	/**
	 * Getting {@link RoleEntity} by its type (enum)
	 * @param roleType to find from
	 * @return the {@link RoleEntity} found
	 */
	@Override
	public RoleDTO getByType(RoleType roleType) {
		return new RoleDTO(roleRepository.findFirstByType(roleType.name()));
	}

	/**
	 * Return list of all {@link RoleDTO} 
	 * @return list of {@link RoleDTO} created
	 */
	@Override
	public List<RoleDTO> findAll() {
		List<RoleEntity> roles = roleRepository.findAll();

		List<RoleDTO> output = new ArrayList<>();
		for (RoleEntity role : roles) {
			output.add(new RoleDTO(role));
		}
		return output;
	}

}
