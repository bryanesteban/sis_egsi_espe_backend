package com.espe.ListoEgsi.enums;

public enum UserRole {
    ADMIN("ADMIN","Administrator with full system access"),
    USER("USER","Regular user with basic access" ),
    VIEWER("VIEWER", "Viewer only see the documents"),
    APPROVER("APPROVER", "approves the created documents ");


    private final String roleName;
    private final String description;

    UserRole(String roleName, String description) {
        this.roleName = roleName;
        this.description = description;
    }

    /**
     * Get the role name as it appears in Azure AD token
     * @return the role name string
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * Get the description of this role
     * @return the role description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the Spring Security role name with ROLE_ prefix
     * @return the Spring Security role name
     */
    public String getSpringSecurityRole() {
        return "ROLE_" + roleName;
    }

    /**
     * Convert from Azure AD role name to UserRole enum
     * @param roleName the role name from Azure AD
     * @return the corresponding UserRole enum, or null if not found
     */
    public static UserRole fromRoleName(String roleName) {
        if (roleName == null) {
            return null;
        }

        for (UserRole role : UserRole.values()) {
            if (role.getRoleName().equals(roleName)) {
                return role;
            }
        }
        return null;
    }

    /**
     * Get all role names as strings
     * @return array of role name strings
     */
    public static String[] getAllRoleNames() {
        UserRole[] roles = UserRole.values();
        String[] roleNames = new String[roles.length];
        for (int i = 0; i < roles.length; i++) {
            roleNames[i] = roles[i].getRoleName();
        }
        return roleNames;
    }

    /**
     * Check if a role name is valid
     * @param roleName the role name to check
     * @return true if the role name is valid, false otherwise
     */
    public static boolean isValidRole(String roleName) {
        return fromRoleName(roleName) != null;
    }

    @Override
    public String toString() {
        return roleName;
    }
}
