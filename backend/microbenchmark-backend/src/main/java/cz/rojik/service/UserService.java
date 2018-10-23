package cz.rojik.service;

import cz.rojik.dto.user.UserDTO;
import cz.rojik.dto.user.UserRegistrationForm;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

	UserDTO createUser(UserRegistrationForm userForm);

	List<UserDTO> getAllEnabled();
	
	UserDTO getByEmail(String email);

    UserDTO getUser(Long id);

	List<UserDTO> getAllUserWithRoles();
}
