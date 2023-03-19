package id.ac.ui.cs.advprog.b10.petdaycare.auth.repository;

import id.ac.ui.cs.advprog.b10.petdaycare.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
