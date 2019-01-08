package cz.rojik.backend.enums;

import java.io.Serializable;

/**
 * All basic roles
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public enum RoleTypeEnum implements Serializable {

    /**
     * The most powerful role. User with this role can do it everything.
     */
    ADMIN(Values.ROLE_ADMIN_VALUE), // ID - 1

    /**
     * Role for basic user. User with this role can run benchmark and see to all his completed benchmarks and results.
     */
    USER(Values.ROLE_USER_VALUE), // ID - 2

    /**
     * Role only for view completed benchmarks. User with this role can only view completed benchmarks. He cannot run benchmark.
     */
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