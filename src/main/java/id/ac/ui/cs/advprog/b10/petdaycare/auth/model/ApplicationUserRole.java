package id.ac.ui.cs.advprog.b10.petdaycare.auth.model;


import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static id.ac.ui.cs.advprog.b10.petdaycare.auth.model.ApplicationUserPermission.*;


public enum ApplicationUserRole {

    ADMIN(Sets.newHashSet(VERIFICATION_CREATE, VERIFICATION_READ_ALL, VERIFICATION_UPDATE,
            VERIFICATION_DELETE, TOPUP_READ_ALL, TOPUP_UPDATE, TOPUP_DELETE, PENITIPAN_READ_ALL,
            PENITIPAN_UPDATE, PENITIPAN_DELETE, PENGAMBILAN_READ_ALL, PENGAMBILAN_UPDATE,
            PENGAMBILAN_DELETE)),
    USER(Sets.newHashSet(VERIFICATION_READ_SELF,TOPUP_CREATE, TOPUP_READ_SELF, PENITIPAN_CREATE,
            PENITIPAN_READ_SELF, PENITIPAN_UPDATE, PENGAMBILAN_CREATE, PENGAMBILAN_READ_SELF,
            PENGAMBILAN_UPDATE));


    private final Set<ApplicationUserPermission> permissions;


    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthority() {
        Set<SimpleGrantedAuthority> authorities = getPermissions().stream().map(permission -> new SimpleGrantedAuthority(permission.getPermission())).collect(Collectors.toSet());
        authorities.add(new SimpleGrantedAuthority("ROLE_"+ this.name()));
        return authorities;
    }
}
