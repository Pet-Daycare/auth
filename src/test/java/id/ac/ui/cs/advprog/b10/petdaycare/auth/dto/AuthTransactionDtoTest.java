package id.ac.ui.cs.advprog.b10.petdaycare.auth.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AuthTransactionDtoTest {

    @Test
    void testNoArgsConstructor_ShouldCreateInstanceWithNullValues() {
        // Act
        AuthTransactionDto authTransactionDto = new AuthTransactionDto();

        // Assert
        assertNull(authTransactionDto.getIdCustomer());
        assertNull(authTransactionDto.getUsername());
        assertNull(authTransactionDto.getToken());
    }

    @Test
    void testAllArgsConstructor_ShouldCreateInstanceWithProvidedValues() {
        // Arrange
        Integer idCustomer = 1;
        String username = "john";
        String token = "token";

        // Act
        AuthTransactionDto authTransactionDto = new AuthTransactionDto(idCustomer, username, token);

        // Assert
        assertEquals(idCustomer, authTransactionDto.getIdCustomer());
        assertEquals(username, authTransactionDto.getUsername());
        assertEquals(token, authTransactionDto.getToken());
    }

    @Test
    void testAuthTransactionDto_IdCustomerNotNull() {
        AuthTransactionDto dto = AuthTransactionDto.builder()
                .idCustomer(1)
                .username("testuser")
                .token("testtoken")
                .build();

        // Assert statements to verify the expected behavior
        assertEquals(1, dto.getIdCustomer());
        assertEquals("testuser", dto.getUsername());
        assertEquals("testtoken", dto.getToken());
    }

    @Test
    void testAuthTransactionDto_IdCustomerNull() {
        AuthTransactionDto dto = AuthTransactionDto.builder()
                .username("testuser")
                .token("testtoken")
                .build();

        // Assert statements to verify the expected behavior
        assertEquals(null, dto.getIdCustomer());
        assertEquals("testuser", dto.getUsername());
        assertEquals("testtoken", dto.getToken());
    }
    @Test
    void testBuilder_ShouldCreateInstanceWithProvidedValues() {
        // Arrange
        Integer idCustomer = 1;
        String username = "john";
        String token = "token";

        // Act
        AuthTransactionDto authTransactionDto = AuthTransactionDto.builder()
                .idCustomer(idCustomer)
                .username(username)
                .token(token)
                .build();

        // Assert
        assertEquals(idCustomer, authTransactionDto.getIdCustomer());
        assertEquals(username, authTransactionDto.getUsername());
        assertEquals(token, authTransactionDto.getToken());
    }

    @Test
    void testSetterAndGetters_ShouldSetAndReturnValues() {
        // Arrange
        AuthTransactionDto authTransactionDto = new AuthTransactionDto();

        Integer idCustomer = 1;
        String username = "john";
        String token = "token";

        // Act
        authTransactionDto.setIdCustomer(idCustomer);
        authTransactionDto.setUsername(username);
        authTransactionDto.setToken(token);

        // Assert
        assertEquals(idCustomer, authTransactionDto.getIdCustomer());
        assertEquals(username, authTransactionDto.getUsername());
        assertEquals(token, authTransactionDto.getToken());
    }

    @Test
    void testNonNullIdCustomer() {
        // Arrange
        Integer idCustomer = 123;
        String username = "john_doe";
        String token = "abc123";

        // Act
        AuthTransactionDto dto = AuthTransactionDto.builder()
                .idCustomer(idCustomer)
                .username(username)
                .token(token)
                .build();

        // Assert
        Assertions.assertEquals(idCustomer, dto.getIdCustomer());
        Assertions.assertEquals(username, dto.getUsername());
        Assertions.assertEquals(token, dto.getToken());
    }

    @Test
    void testNullIdCustomer() {
        // Arrange
        String username = "john_doe";
        String token = "abc123";

        // Act
        AuthTransactionDto dto = AuthTransactionDto.builder()
                .username(username)
                .token(token)
                .build();

        // Assert
        Assertions.assertNull(dto.getIdCustomer());
        Assertions.assertEquals(username, dto.getUsername());
        Assertions.assertEquals(token, dto.getToken());
    }

    @Test
    void testNullToken() {
        // Arrange
        Integer idCustomer = 123;
        String username = "john_doe";

        // Act
        AuthTransactionDto dto = AuthTransactionDto.builder()
                .idCustomer(idCustomer)
                .username(username)
                .build();

        // Assert
        Assertions.assertEquals(idCustomer, dto.getIdCustomer());
        Assertions.assertEquals(username, dto.getUsername());
        Assertions.assertNull(dto.getToken());
    }
}
