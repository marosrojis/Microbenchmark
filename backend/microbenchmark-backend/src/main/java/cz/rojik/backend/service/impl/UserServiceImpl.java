package cz.rojik.backend.service.impl;

import cz.rojik.backend.dto.user.UserDTO;
import cz.rojik.backend.dto.user.UserRegistrationForm;
import cz.rojik.backend.entity.UserEntity;
import cz.rojik.backend.repository.UserRepository;
import cz.rojik.backend.service.RoleService;
import cz.rojik.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private RoleService roleService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
    private UserRepository userRepository;

    /**
     * Creating user with data from {@link UserRegistrationForm}
     * @param userForm {@link UserRegistrationForm} with data to create new user
     * @return true if the user was successfully created
     */
	@Transactional
    @Override
    public UserDTO createUser(UserRegistrationForm userForm) {
		boolean verifyExistEmail = verifyExistEmail(userForm.getEmail());
		if (!verifyExistEmail) {
            UserEntity user = createAndSaveRegisteredUser(userForm);
            return new UserDTO(user);
		}
		return null;
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
     * Method that returns user by its email address
     * @param email the email address to find from
     * @return the {@link UserEntity} found
     */
    @Override
    public UserDTO getByEmail(String email) {
        return new UserDTO(userRepository.findByEmail(email));
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
            return null;
        }
        return new UserDTO(user);
    }

    /**
     * Creating and returning list of {@link UserDTO}  from user with Admin role
     * @return list of {@link UserDTO} from user with admin role
     */
    @Override
    public List<UserDTO> getAllUserWithRoles() {
//        List<User> users = userRepository.getAllUserWithRoles();
//
//        List<UserDTO> output = new ArrayList<>();
//        for (User user : users) {
//            output.add(new UserDTO(user));
//        }
//        return output;
        return null;
    }

    private UserEntity createAndSaveRegisteredUser(UserRegistrationForm userForm) {
//        User user = new User(userForm.getFirstname(), userForm.getLastname(), userForm.getEmail(), passwordEncoder.encode(userForm.getPassword()));
//        user.getRoles().add(roleService.getByType(RoleType.getRoleById(userForm.getRoleId())));

//	    this.save(user);

//        return user;
    return null;
    }

	/**
	 * Verify if email in parameter has existed in DB
	 * @param email email of new user
	 * @return true if email has existed
	 */
	private boolean verifyExistEmail(String email) {
		UserDTO existUser = this.getByEmail(email);
		return !(existUser == null);
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
