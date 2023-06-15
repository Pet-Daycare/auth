package id.ac.ui.cs.advprog.b10.petdaycare.auth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {

    @Id
    @SequenceGenerator(name = "Template_User", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Template_User")
    private Integer id;
    private String fullName;
    private String password;

    @Column(unique = true)
    private String username;


    @Column(unique=true)
    private String email;

    private String role;

    @OneToMany(mappedBy = "user")
    private transient List<Token> tokens;
    private boolean active;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(role.equals("ADMIN")) {
            return ApplicationUserRole.ADMIN.getGrantedAuthority();
        } else {
            return ApplicationUserRole.USER.getGrantedAuthority();
        }
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return username;
    }
    @Override
    public boolean isAccountNonExpired() {
        return this.active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.active;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }
}
