package cz.rojik.backend.service.impl;

import cz.rojik.backend.dto.user.UserDTO;
import cz.rojik.backend.dto.user.UserRegistrationForm;
import cz.rojik.backend.entity.RoleEntity;
import cz.rojik.backend.entity.RoleType;
import cz.rojik.backend.entity.UserEntity;
import cz.rojik.backend.exception.UserException;
import cz.rojik.backend.repository.RoleRepository;
import cz.rojik.backend.repository.UserRepository;
import cz.rojik.backend.service.BenchmarkService;
import cz.rojik.backend.service.UserService;
import cz.rojik.backend.util.SecurityHelper;
import cz.rojik.backend.util.converter.UserConverter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.BadRequestException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
    private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserConverter userConverter;

	@Autowired
	private BenchmarkService benchmarkService;

    /**
     * Creating user with data from {@link UserRegistrationForm}
     * @param user {@link UserRegistrationForm} with data to create new user
     * @return true if the user was successfully created
     */
	@Transactional
    @Override
    public UserDTO create(UserRegistrationForm user) {
		boolean verifyExistEmail = verifyExistEmail(user.getEmail());
		if (!verifyExistEmail) {
            UserEntity entity = createAndSaveRegisteredUser(user);
            return userConverter.entityToDTO(entity, true);
		}
		throw new UserException(String.format("User with email %s has already existed.", user.getEmail()));
	}

	/**
	 * Update user with data from {@link UserDTO}
	 * @param user {@link UserDTO} with data to update new user
	 * @return true if the user was successfully updated
	 */
	@Transactional
	@Override
	public UserDTO update(Long userId, UserDTO user) {
		UserEntity entity = userRepository.findOne(userId);

		entity = userConverter.mapToEntityUpdate(user, entity);

		if (!StringUtils.equals(user.getEmail(), entity.getEmail())) {
			boolean verifyExistEmail = verifyExistEmail(user.getEmail());
			if (verifyExistEmail) {
				throw new UserException(String.format("User with email %s has already existed.", user.getEmail()));
			}
			entity.setEmail(user.getEmail());
		}

		if (user.getRoles() != null && user.getRoles().size() != 0) {
			entity.setRoles(Collections.emptySet());
			Set<RoleEntity> roles = new HashSet<>();
			user.getRoles().forEach(role -> roles.add(roleRepository.findFirstByType(RoleType.getRoleById(role.getId()))));
			entity.setRoles(roles);
		}

		entity = userRepository.save(entity);
		return userConverter.entityToDTO(entity, true);
	}

	/**
	 * Method that returns list of {@link UserEntity} that are enabled
	 * @return a list of {@link UserEntity} that are enabled
	 */
    @Override
    public List<UserDTO> getAllEnabled() {
//        return userRepository.getAllEnabled();
        return null;
    }

    /**
     * Method that returns list of {@link UserEntity} that are not enabled
     * @return a list of {@link UserEntity} that are enabled
     */
    @Override
    public List<UserDTO> getAllNonEnabled() {
        List<UserEntity> users = userRepository.findAllNonEnabled();

        List<UserDTO> output = users.stream().map(user -> userConverter.entityToDTO(user, true)).collect(Collectors.toList());
        return output;
    }

    /**
     * Method that returns user by its email address
     * @param email the email address to find from
     * @return the {@link UserEntity} found
     */
    @Override
    public UserDTO getByEmail(String email) {
		UserEntity entity = userRepository.findByEmail(email);
		if (entity == null) {
			throw new UserException(String.format("User with email %s was not found", email));
		}
        return userConverter.entityToDTO(entity, true);
    }

    /**
     * Getting {@link UserDTO} from user id
     * @param id the id to find from
     * @return {@link UserDTO} from found user
     */
    @Override
    public UserDTO getUser(Long id) {
        UserEntity user = userRepository.findOne(id);

        if (user == null) {
			throw new UserException(String.format("User with id %s was not found", id));
		}
        return userConverter.entityToDTO(user, true);
    }

    /**
     * Creating and returning list of {@link UserDTO}  from user with Admin role
     * @return list of {@link UserDTO} from user with admin role
     */
    @Override
    public List<UserDTO> getAll() {
        List<UserEntity> users = userRepository.findAllWithRole();

		List<UserDTO> output = users.stream().map(user -> userConverter.entityToDTO(user, true)).collect(Collectors.toList());
		return output;
    }

	@Override
	public UserEntity getLoggedUserEntity() {
		UserDTO userDTO = SecurityHelper.getCurrentUser();
		if (userDTO != null) {
			UserEntity entity = userRepository.findOne(userDTO.getId());
			return entity;
		}
		return null;
	}

	// TODO: promyslet jestli smazat uzivatele pokud jiz pustil nejake testy
	@Override
	public void delete(Long id) {
		UserEntity entity = userRepository.findOne(id);
		if (entity == null) {
			throw new UserException(String.format("User with ID %s was not found.", id));
		}

		userRepository.delete(entity);
	}

	private UserEntity createAndSaveRegisteredUser(UserRegistrationForm user) {
        UserEntity entity = new UserEntity(user.getFirstname(), user.getLastname(), user.getEmail(), passwordEncoder.encode(user.getPassword()));

		Set<RoleEntity> roles = new HashSet<>();
		RoleEntity userRole = roleRepository.findFirstByType(RoleType.DEMO.getRoleType());
		roles.add(userRole);

        user.getRoles().forEach(role -> roles.add(roleRepository.findFirstByType(RoleType.getRoleById(role.getId()))));

        entity.setRoles(roles);

        if (SecurityHelper.isLoggedUserAdmin()) {
            entity.setEnabled(true);
        }

		entity = userRepository.save(entity);
        return entity;
    }

	/**
	 * Verify if email in parameter has existed in DB
	 * @param email email of new user
	 * @return true if email has existed
	 */
	private boolean verifyExistEmail(String email) {
		UserEntity entity = userRepository.findByEmail(email);
		return !(entity == null);
	}

	/**
	 * Getting {@link UserDetails} from username.
	 * @param username to find from
	 * @exception UsernameNotFoundException when the user was not found
	 * @return the {@link UserDetails}
	 */
    @Transactional(readOnly=true)
	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		String email = username;
		UserDTO user = getByEmail(email);

		if(user == null) {
			throw new UsernameNotFoundException("Username not found");
		}

		return user;
	}
}
