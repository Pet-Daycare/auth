package id.ac.ui.cs.advprog.b10.petdaycare.auth.model;

public enum ApplicationUserPermission {
    VERIFICATION_CREATE("verification:create"),
    VERIFICATION_READ_ALL("verification:read_all"),
    VERIFICATION_READ_SELF("verification:read_self"),
    VERIFICATION_UPDATE("verification:update"),
    VERIFICATION_DELETE("verification:delete"),

    TOPUP_CREATE("topup:create"),
    TOPUP_READ_ALL("topup:read_all"),
    TOPUP_READ_SELF("topup:read_self"),
    TOPUP_UPDATE("topup:update"),
    TOPUP_DELETE("topup:delete"),

    PENITIPAN_CREATE("penitipan:create"),
    PENITIPAN_READ_ALL("penitipan:read_all"),
    PENITIPAN_READ_SELF("penitipan:read_self"),
    PENITIPAN_UPDATE("penitipan:update"),
    PENITIPAN_DELETE("penitipan:delete"),

    PENGAMBILAN_CREATE("pengambilan:create"),
    PENGAMBILAN_READ_ALL("pengambilan:read_all"),
    PENGAMBILAN_READ_SELF("pengambilan:read_self"),
    PENGAMBILAN_UPDATE("pengambilan:update"),
    PENGAMBILAN_DELETE("pengambilan:delete");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
