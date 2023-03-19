package id.ac.ui.cs.advprog.b10.petdaycare.auth.model;

public enum ApplicationUserPermission {
    MEDICINE_READ("medicine:read"),
    MEDICINE_CREATE("medicine:create"),
    MEDICINE_UPDATE("medicine:update"),
    MEDICINE_DELETE("medicine:delete"),
    ORDER_READ_ALL("order:read_all"),
    ORDER_READ_SELF("order:read_self"),
    ORDER_CREATE("order:create"),
    ORDER_UPDATE("order:update"),
    ORDER_DELETE("order:delete");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
