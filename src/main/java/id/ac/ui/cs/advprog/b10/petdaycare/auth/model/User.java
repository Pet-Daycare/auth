package id.ac.ui.cs.advprog.b10.petdaycare.auth.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.model.Pet;
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
    @GeneratedValue
    private Integer id;
    private String firstname;
    private String lastname;

    private String password;

    @Column(unique = true)
    private String username;


    @Column(unique=true)
    private String email;

    private String role;

    private boolean active;

    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<Pet> pets;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "petwallet_id", referencedColumnName = "id")
    private PetWallet petWallet;

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

    public PetWallet getPetWallet() {return this.petWallet;}

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
