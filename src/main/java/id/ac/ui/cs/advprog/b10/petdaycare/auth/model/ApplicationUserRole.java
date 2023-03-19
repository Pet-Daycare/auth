package id.ac.ui.cs.advprog.b10.petdaycare.auth.model;


import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static id.ac.ui.cs.advprog.b10.petdaycare.auth.model.ApplicationUserPermission.*;


public enum ApplicationUserRole {

    ADMIN(Sets.newHashSet(MEDICINE_CREATE, MEDICINE_READ, MEDICINE_UPDATE, MEDICINE_DELETE, ORDER_READ_ALL, ORDER_DELETE)),
    USER(Sets.newHashSet(MEDICINE_READ, ORDER_CREATE, ORDER_READ_SELF, ORDER_UPDATE));


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
