package cz.rojik.backend.enums;

import java.io.Serializable;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public enum RoleTypeEnum implements Serializable {
    ADMIN(Values.ROLE_ADMIN_VALUE), // ID - 1
    USER(Values.ROLE_USER_VALUE), // ID - 2
    DEMO(Values.ROLE_DEMO_VALUE); // ID - 3

    String userRole;

    private RoleTypeEnum(String userRole){
        this.userRole = userRole;
    }

    public String getRoleType(){
        return userRole;
    }

    public static String getRoleById(long id) {
        if (id == 1) return RoleTypeEnum.ADMIN.getRoleType();
        if (id == 2) return RoleTypeEnum.USER.getRoleType();
        if (id == 3) return RoleTypeEnum.DEMO.getRoleType();
        return null;
    }

    @Override
    public String toString() {
        return "RoleType{" +
                "userRole='" + userRole + '\'' +
                '}';
    }

    public static class Values {
		public static final String ROLE_ADMIN_VALUE = "ADMIN";
		public static final String ROLE_USER_VALUE = "USER";
		public static final String ROLE_DEMO_VALUE = "DEMO";
	}

}