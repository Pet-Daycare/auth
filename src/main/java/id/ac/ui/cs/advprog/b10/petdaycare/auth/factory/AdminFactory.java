package id.ac.ui.cs.advprog.b10.petdaycare.auth.factory;

import id.ac.ui.cs.advprog.b10.petdaycare.auth.dto.RegisterRequest;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.model.User;

public class AdminFactory implements FactoryUser{
    @Override
    public User createUser(RegisterRequest request) {
        return User.builder()
                .fullName(request.getFullName())
                .username(request.getUsername())
                .active(true)
                .email(request.getEmail())
                .password(request.getPassword())
                .role("ADMIN")
                .build();
    }
}
