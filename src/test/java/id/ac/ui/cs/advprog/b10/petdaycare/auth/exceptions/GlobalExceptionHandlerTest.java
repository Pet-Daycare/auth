package id.ac.ui.cs.advprog.b10.petdaycare.auth.exceptions;

import id.ac.ui.cs.advprog.b10.petdaycare.auth.exceptions.ErrorTemplate;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.exceptions.advice.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

 class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
     void testUsernameExist() {
        ResponseEntity<Object> response = globalExceptionHandler.usernameExist();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorTemplate error = (ErrorTemplate) response.getBody();
        assertEquals("User with the same username already exist", error.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, error.getStatus());
    }

    @Test
     void testUsernameAlreadyLoggedIn() {
        ResponseEntity<Object> response = globalExceptionHandler.usernameAlreadyLoggedIn();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorTemplate error = (ErrorTemplate) response.getBody();
        assertEquals("User already logged in", error.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, error.getStatus());
    }

    @Test
     void testInvalidToken() {
        ResponseEntity<Object> response = globalExceptionHandler.invalidToken();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorTemplate error = (ErrorTemplate) response.getBody();
        assertEquals("User token is invalid", error.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, error.getStatus());
    }

    @Test
     void testCredentialsError() {
        String errorMessage = "Invalid credentials";
        ResponseEntity<Object> response = globalExceptionHandler.credentialsError(new RuntimeException(errorMessage));

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        ErrorTemplate error = (ErrorTemplate) response.getBody();
        assertEquals(errorMessage, error.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, error.getStatus());
    }

    @Test
     void testGeneralError() {
        String errorMessage = "Something went wrong";
        ResponseEntity<Object> response = globalExceptionHandler.generalError(new RuntimeException(errorMessage));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        ErrorTemplate error = (ErrorTemplate) response.getBody();
        assertEquals(errorMessage, error.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, error.getStatus());
    }
}
