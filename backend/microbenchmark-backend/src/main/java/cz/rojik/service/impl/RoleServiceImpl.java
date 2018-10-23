package cz.rojik.service.impl;

import cz.rojik.dto.user.RoleDTO;
import cz.rojik.entity.Role;
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
	 * Getting {@link Role} by its type
	 * @param type to find from
	 * @return the {@link Role} found
	 */
	@Override
	public RoleDTO getByType(String type) {
		return new RoleDTO(roleRepository.findFirstByType(type));
	}

	/**
	 * Getting {@link Role} by its type (enum)
	 * @param roleType to find from
	 * @return the {@link Role} found
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
		List<Role> roles = roleRepository.findAll();

		List<RoleDTO> output = new ArrayList<>();
		for (Role role : roles) {
			output.add(new RoleDTO(role));
		}
		return output;
	}

}
