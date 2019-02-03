package cz.rojik.mock;

import cz.rojik.backend.dto.user.RoleDTO;
import cz.rojik.backend.dto.user.UserDTO;
import cz.rojik.backend.enums.RoleTypeEnum;
import cz.rojik.backend.util.SecurityHelper;

import java.util.Arrays;
import java.util.Collections;

import static cz.rojik.mock.MockConst.*;
import static org.mockito.BDDMockito.given;

/**
 * Created by Marek Rojik (marek.rojik@inventi.cz) on 02. 02. 2019
 */
public class SecurityHelperMock {

    private SecurityHelper securityHelper;

    public SecurityHelperMock(SecurityHelper securityHelper) {
        this.securityHelper = securityHelper;
    }

    public UserDTO mockAdmin() {
        RoleDTO adminRole = new RoleDTO().setType(RoleTypeEnum.ADMIN.getRoleType());
        adminRole.setId(ROLE_ID_1);
        RoleDTO userRole = new RoleDTO().setType(RoleTypeEnum.USER.getRoleType());
        userRole.setId(ROLE_ID_2);

        UserDTO user = new UserDTO()
                .setFirstname(USER_FIRSTNAME_1)
                .setLastname(USER_LASTNAME_1)
                .setEmail(USER_EMAIL_1)
                .setPassword(USER_PASSWORD_1)
                .setEnabled(true);
        user.setId(USER_ID_1);

        user.setRoles(Arrays.asList(adminRole, userRole));
        given(securityHelper.getCurrentUser()).willReturn(user);
        given(securityHelper.getCurrentUserId()).willReturn(user.getId());
        given(securityHelper.getCurrentUserName()).willReturn(user.getUsername());
        given(securityHelper.isLoggedUserAdmin()).willReturn(true);
        return user;
    }

    public UserDTO mockUser() {
        RoleDTO userRole = new RoleDTO().setType(RoleTypeEnum.USER.getRoleType());
        userRole.setId(ROLE_ID_2);

        UserDTO user = new UserDTO()
                .setFirstname(USER_FIRSTNAME_2)
                .setLastname(USER_LASTNAME_2)
                .setEmail(USER_EMAIL_2)
                .setPassword(USER_PASSWORD_2)
                .setEnabled(true);
        user.setId(USER_ID_2);

        user.setRoles(Collections.singletonList(userRole));
        given(securityHelper.getCurrentUser()).willReturn(user);
        given(securityHelper.getCurrentUserId()).willReturn(user.getId());
        given(securityHelper.getCurrentUserName()).willReturn(user.getUsername());
        given(securityHelper.isLoggedUserAdmin()).willReturn(false);
        return user;
    }

    public UserDTO mockDemo() {
        RoleDTO demoRole = new RoleDTO().setType(RoleTypeEnum.DEMO.getRoleType());
        demoRole.setId(ROLE_ID_3);

        UserDTO user = new UserDTO()
                .setFirstname(USER_FIRSTNAME_3)
                .setLastname(USER_LASTNAME_3)
                .setEmail(USER_EMAIL_3)
                .setPassword(USER_PASSWORD_3)
                .setEnabled(true);
        user.setId(USER_ID_3);

        user.setRoles(Collections.singletonList(demoRole));
        given(securityHelper.getCurrentUser()).willReturn(user);
        given(securityHelper.getCurrentUserId()).willReturn(user.getId());
        given(securityHelper.getCurrentUserName()).willReturn(user.getUsername());
        given(securityHelper.isLoggedUserAdmin()).willReturn(false);
        return user;
    }
}
