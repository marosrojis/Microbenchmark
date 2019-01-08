package cz.rojik.backend.service.impl;

import cz.rojik.backend.dto.user.UserDTO;
import cz.rojik.backend.dto.user.UserRegistrationForm;
import cz.rojik.backend.entity.RoleEntity;
import cz.rojik.backend.enums.RoleTypeEnum;
import cz.rojik.backend.entity.UserEntity;
import cz.rojik.backend.exception.EntityNotFoundException;
import cz.rojik.backend.exception.UserException;
import cz.rojik.backend.repository.RoleRepository;
import cz.rojik.backend.repository.UserRepository;
import cz.rojik.backend.service.EmailService;
import cz.rojik.backend.service.UserService;
import cz.rojik.backend.util.SecurityHelper;
import cz.rojik.backend.util.converter.UserConverter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Service
public class UserServiceImpl implements UserService {

	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
    private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserConverter userConverter;

	@Autowired
	private EmailService emailService;

    /**
     * Creating user with data from {@link UserRegistrationForm}
     * @param user {@link UserRegistrationForm} with data to create new user
     * @return true if the user was successfully created
     */
	@Transactional
    @Override
    public UserDTO create(UserRegistrationForm user) {
		logger.trace("Create new user {}", user);
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
	 * @return successfully updated user
	 */
	@Transactional
	@Override
	public UserDTO update(Long userId, UserDTO user) {
		logger.trace("Update existed user {} in DB with data {}", userId, user);
		Optional<UserEntity> userEntity = findById(userId);
		if (!userEntity.isPresent()) {
			throw new EntityNotFoundException(String.format("User with ID %s was not found.", userId));
		}

		UserEntity entity = userEntity.get();
		boolean isEnabled = entity.isEnabled();
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
			user.getRoles().forEach(role -> roles.add(roleRepository.findFirstByType(RoleTypeEnum.getRoleById(role.getId()))));
			boolean isRolesValidate = validateRoles(roles);
			if (!isRolesValidate) {
				logger.error("User's roles is not validate: " + roles);
				throw new UserException("User's roles is not validate. You cannot set USER and DEMO roles to one user.");
			}

			entity.setRoles(roles);
		}

		entity = userRepository.save(entity);
		logger.debug("User with email was successfully updated: {}", userId, entity);

		if (!isEnabled && entity.isEnabled()) {
			emailService.sendAfterUpdateUser(entity.getEmail());
		}

		return userConverter.entityToDTO(entity, true);
	}

    /**
     * Method that returns user by its email address
     * @param email the email address to find from
     * @return the {@link UserDTO} found
     */
    @Override
    public UserDTO getByEmail(String email) {
    	logger.trace("Get user by email {}", email);
		UserEntity entity = userRepository.findByEmail(email);
		if (entity == null) {
			throw new EntityNotFoundException(String.format("User with email %s was not found", email));
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
    	logger.trace("Get user with ID {} (requested user {})", id, SecurityHelper.getCurrentUser());
        Optional<UserEntity> user = findById(id);

        if (!user.isPresent()) {
			throw new EntityNotFoundException(String.format("User with id %s was not found", id));
		}
        return userConverter.entityToDTO(user.get(), true);
    }

    @Override
    public List<UserDTO> getAll(Optional<Boolean> enabled) {
    	logger.trace("Get all users (requested user: {})", SecurityHelper.getCurrentUser());
        List<UserEntity> users;

        if (enabled.isPresent()) {
        	users = userRepository.findAllEnabled(enabled.get());
		}
		else {
			users = userRepository.findAllWithRole();
		}

		List<UserDTO> output = users.stream().map(user -> userConverter.entityToDTO(user, true)).collect(Collectors.toList());
		return output;
    }

	@Override
	public UserEntity getLoggedUserEntity() {
    	logger.trace("Get logged user entity");
		UserDTO userDTO = SecurityHelper.getCurrentUser();
		if (userDTO != null) {
			Optional<UserEntity> entity = findById(userDTO.getId());
			return entity.get();
		}
		return null;
	}

	@Override
	public void delete(Long id) {
    	logger.debug("Delete user with ID {} (requested user {})", id, SecurityHelper.getCurrentUser());
		Optional<UserEntity> entity = userRepository.findById(id);
		if (!entity.isPresent()) {
			throw new EntityNotFoundException(String.format("User with ID %s was not found.", id));
		}

		userRepository.delete(entity.get());
		logger.debug("User with ID {} was successfully deleted", id);
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
		logger.trace("Get user by username {}", username);
		String email = username;
		UserDTO user = getByEmail(email);

		if(user == null) {
			throw new UsernameNotFoundException("Username not found");
		}

		return user;
	}

	/**
	 * Create user and assign him roles based on registration form
	 * @param user registration user form
	 * @return new user entity
	 */
	private UserEntity createAndSaveRegisteredUser(UserRegistrationForm user) {
    	logger.trace("Create and save new user {}", user);
        UserEntity entity = new UserEntity(user.getFirstname(), user.getLastname(), user.getEmail(), passwordEncoder.encode(user.getPassword()));

		Set<RoleEntity> roles = new HashSet<>();
        user.getRoles().forEach(role -> roles.add(roleRepository.findFirstByType(RoleTypeEnum.getRoleById(role.getId()))));
		boolean isRolesValidate = validateRoles(roles);
		if (!isRolesValidate) {
			logger.error("User's roles is not validate: " + roles);
			throw new UserException("User's roles is not validate. You cannot set USER and DEMO roles to one user.");
		}

        entity.setRoles(roles);

		// if user account is created by admin, automatic enable account
        if (SecurityHelper.isLoggedUserAdmin()) {
            entity.setEnabled(true);
        }

		entity = userRepository.save(entity);
        logger.debug("User {} was successfully saved", entity);

        emailService.sendAfterRegistrationUser(entity.getEmail(), entity.isEnabled());

        return entity;
    }

	/**
	 * Verify if email in parameter has existed in DB
	 * @param email email of new user
	 * @return true if email has existed
	 */
	private boolean verifyExistEmail(String email) {
		logger.trace("Verify if email {} is exists", email);
		UserEntity entity = userRepository.findByEmail(email);
		return !(entity == null);
	}

	/**
	 * Find user by ID. If logged user has not 'Admin' role, check if user ID is equals with logged user ID
	 * @param id user id
	 * @return user entity object
	 */
	private Optional<UserEntity> findById(Long id) {
    	if (!SecurityHelper.isLoggedUserAdmin() && !id.equals(SecurityHelper.getCurrentUserId())) {
			throw new EntityNotFoundException(String.format("User with ID %s was not found.", id));
		}
    	Optional<UserEntity> entity = userRepository.findById(id);
    	return entity;
	}

	/**
	 * Method for validate role for created/updated user.
	 * User must not have role 'User' and 'Demo' at the same time.
	 * @param roles role to set
	 * @return ok/fail validation
	 */
	private boolean validateRoles(Set<RoleEntity> roles) {
    	logger.trace("Validate roles {}", roles);
		List<RoleEntity> roleTypes = roles.stream().filter(r -> r.getType().equals(RoleTypeEnum.USER.getRoleType()) || r.getType().equals(RoleTypeEnum.DEMO.getRoleType())).collect(Collectors.toList());
		return roleTypes.size() != 2;
	}
}
