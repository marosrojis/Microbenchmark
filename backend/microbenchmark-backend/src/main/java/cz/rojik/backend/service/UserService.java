package cz.rojik.backend.service;

import cz.rojik.backend.dto.user.UserDTO;
import cz.rojik.backend.dto.user.UserRegistrationForm;
import cz.rojik.backend.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

	UserDTO create(UserRegistrationForm user);

	UserDTO update(Long userId, UserDTO user);

	List<UserDTO> getAllEnabled();

	List<UserDTO> getAllNonEnabled();

	UserDTO getByEmail(String email);

    UserDTO getUser(Long id);

	List<UserDTO> getAll();

	UserEntity getLoggedUserEntity();

	void delete(Long id);
}
