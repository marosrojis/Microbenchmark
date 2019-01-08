package cz.rojik.backend.service;

import cz.rojik.backend.dto.user.UserDTO;
import cz.rojik.backend.dto.user.UserRegistrationForm;
import cz.rojik.backend.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public interface UserService extends UserDetailsService {

	/**
	 * Create user based on filled registration form.
	 * @param user user registration form
	 * @return created user
	 */
	UserDTO create(UserRegistrationForm user);

	/**
	 * Update user based on user ID
	 * @param userId user ID
	 * @param user user data to update
	 * @return updated user
	 */
	UserDTO update(Long userId, UserDTO user);

	/**
	 * Get user by email
	 * @param email user's email
	 * @return user object
	 */
	UserDTO getByEmail(String email);

	/**
	 * Get user by ID
	 * @param id user ID
	 * @return user object
	 */
    UserDTO getUser(Long id);

	/**
	 * Get all users from database. Result depends on optional parameter 'enabled' and logged user role.
	 * If logged user has 'Admin' role then return all users.
	 * If logged user has 'Demo' or 'User' role then return list with only logged user.
	 * @param enabled Optional parameter for get only enable/disable users
	 * @return list of users
	 */
	List<UserDTO> getAll(Optional<Boolean> enabled);

	/**
	 * Get logged user entity. Logged user is load from spring application context.
	 * @return entity of logged user
	 */
	UserEntity getLoggedUserEntity();

	/**
	 * Delete user from database by ID
	 * @param id user ID
	 */
	void delete(Long id);
}
