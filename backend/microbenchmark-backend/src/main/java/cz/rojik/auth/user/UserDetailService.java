package cz.rojik.auth.user;

import cz.rojik.dto.user.UserDTO;
import cz.rojik.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(noRollbackFor = Exception.class)
public class UserDetailService implements UserDetailsService {

		private static Logger log = LoggerFactory.getLogger(UserDetailService.class);

		@Autowired
		private UserService userService;

		/**
		 * Load user by email address.
		 *
		 * @param username user's email address.
		 * @return User details
		 * @throws UsernameNotFoundException If user not found.
		 */
		@Override
		public UserDTO loadUserByUsername(String username) throws UsernameNotFoundException {
			UserDTO user = userService.getByEmail(username);
			if (user == null) {
				throw new UsernameNotFoundException("User not found");
			}
			return user;
		}
}
