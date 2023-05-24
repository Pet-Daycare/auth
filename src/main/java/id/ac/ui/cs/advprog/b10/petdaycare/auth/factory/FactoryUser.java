package id.ac.ui.cs.advprog.b10.petdaycare.auth.factory;

import id.ac.ui.cs.advprog.b10.petdaycare.auth.dto.RegisterRequest;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.model.User;

public interface FactoryUser {
    User createUser(RegisterRequest request);
}
