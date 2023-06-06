package id.ac.ui.cs.advprog.b10.petdaycare.auth.factory;

import id.ac.ui.cs.advprog.b10.petdaycare.auth.dto.RegisterRequest;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminFactoryTest {

    @Test
    void testCreateUser_ValidRegisterRequest_CreatesAdminUser() {
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
        assertNotNull(user);
        assertEquals("John Doe", user.getFullName());
        assertEquals("johndoe", user.getUsername());
        assertTrue(user.isActive());
        assertEquals("johndoe@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("ADMIN", user.getRole());
    }
}
