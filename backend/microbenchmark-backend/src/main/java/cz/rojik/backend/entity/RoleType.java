package cz.rojik.backend.entity;

import java.io.Serializable;

/**
 * Výčet možných licencí
 */
public enum RoleType implements Serializable {
    USER(Values.ROLE_USER_VALUE), // ID - 1
    ADMIN(Values.ROLE_ADMIN_VALUE); // ID - 3

    String userRole;

    private RoleType(String userRole){
        this.userRole = userRole;
    }

    public String getRoleType(){
        return userRole;
    }

    public static String getRoleById(int id) {
        if (id == 1) return RoleType.USER.getRoleType();
        if (id == 3) return RoleType.ADMIN.getRoleType();
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
		public static final String ROLE_UNREGISTERED_VALUE = "UNREGISTERED";
	}

}