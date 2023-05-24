package id.ac.ui.cs.advprog.b10.petdaycare.auth.factory;

import id.ac.ui.cs.advprog.b10.petdaycare.auth.dto.RegisterRequest;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AdminFactoryTest {

    @Test
    void testCreateUser() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setFullName("John Doe");
        request.setUsername("johndoe");
        request.setEmail("johndoe@example.com");
        request.setPassword("password123");

        AdminFactory adminFactory = new AdminFactory();

        // Act
        User user = adminFactory.createUser(request);

        // Assert
        assertEquals("John Doe", user.getFullName());
        assertEquals("johndoe", user.getUsername());
        assertEquals(true, user.isActive());
        assertEquals("johndoe@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("ADMIN", user.getRole());
    }
}