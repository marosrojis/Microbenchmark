package cz.rojik.backend.service;

import cz.rojik.backend.dto.user.UserDTO;
import cz.rojik.backend.dto.user.UserRegistrationForm;
import cz.rojik.backend.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

	UserDTO create(UserRegistrationForm user);

	UserDTO update(Long userId, UserDTO user);

	UserDTO getByEmail(String email);

    UserDTO getUser(Long id);

	List<UserDTO> getAll(Optional<Boolean> enabled);

	UserEntity getLoggedUserEntity();

	void delete(Long id);
}
