package id.ac.ui.cs.advprog.b10.petdaycare.auth.core;

import id.ac.ui.cs.advprog.b10.petdaycare.auth.exceptions.InvalidTokenException;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.exceptions.UsernameAlreadyLoggedIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

 class AuthManagerTest {

    private AuthManager authManager;

    @BeforeEach
    public void setUp() {
        authManager = AuthManager.getInstance();
    }

    @Test
     void testRegisterNewToken() {
        // Arrange
        String token = "token123";
        String username = "testRegisterNewToken";

        // Act
        authManager.registerNewToken(token, username);

        // Assert
        assertEquals(username, authManager.getUsername(token));
    }

    @Test
    void testRegisterNewToken_ThrowsUsernameAlreadyLoggedIn() {
        // Arrange
        String token1 = "token123";
        String token2 = "token456";
        String username = "ThrowsUsernameAlreadyLoggedIn";

        // Act
        authManager.registerNewToken(token1, username);

        // Assert
        assertThrows(UsernameAlreadyLoggedIn.class, () -> authManager.registerNewToken(token2, username));
    }

    @Test
     void testRemoveToken() {
        // Arrange
        String token = "token123";
        String username = "testRemoveToken";
        authManager.registerNewToken(token, username);

        // Act
        authManager.removeToken(token);

        // Assert
        assertThrows(InvalidTokenException.class, () -> authManager.getUsername(token));
    }

    @Test
     void testGetUsername() {
        // Arrange
        String token = "token123";
        String username = "testGetUsername";
        authManager.registerNewToken(token, username);

        // Act
        String result = authManager.getUsername(token);

        // Assert
        assertEquals(username, result);
    }

    @Test
     void testGetUsername_ThrowsInvalidTokenException() {
        // Arrange
        String token = "invalid_token";

        // Act & Assert
        assertThrows(InvalidTokenException.class, () -> authManager.getUsername(token));
    }
}
