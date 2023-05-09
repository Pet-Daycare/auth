package id.ac.ui.cs.advprog.b10.petdaycare.auth.exceptions.advice;


import id.ac.ui.cs.advprog.b10.petdaycare.auth.exceptions.*;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {


//    @ExceptionHandler(value = {UserAlreadyExistException.class})
//    public ResponseEntity<Object> userExist() {
//        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
//        ErrorTemplate baseException = new ErrorTemplate(
//                "User with the same username already exist",
//                badRequest,
//                ZonedDateTime.now(ZoneId.of("Z"))
//        );
//
//        return new ResponseEntity<>(baseException, badRequest);
//    }

    @ExceptionHandler(value = {UsernameAlreadyExistException.class, UserAlreadyExistException.class})
    public ResponseEntity<Object> usernameExist() {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ErrorTemplate baseException = new ErrorTemplate(
                "User with the same username already exist",
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(baseException, badRequest);
    }
    @ExceptionHandler(value = {UsernameAlreadyLoggedIn.class})
    public ResponseEntity<Object> usernameAlreadyLoggedIn() {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ErrorTemplate baseException = new ErrorTemplate(
                "User already logged in",
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(baseException, badRequest);
    }

    @ExceptionHandler(value = {InvalidTokenException.class})
    public ResponseEntity<Object> invalidToken() {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ErrorTemplate baseException = new ErrorTemplate(
                "User token is invalid",
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(baseException, badRequest);
    }

    @ExceptionHandler(value = {JwtException.class, AuthenticationException.class, UsernameNotFoundException.class})
    public ResponseEntity<Object> credentialsError(Exception exception) {
        HttpStatus badRequest = HttpStatus.UNAUTHORIZED;
        ErrorTemplate baseException = new ErrorTemplate(
                exception.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(baseException, badRequest);
    }


    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> generalError(Exception exception) {
        HttpStatus badRequest = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorTemplate baseException = new ErrorTemplate(
                exception.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(baseException, badRequest);
    }


}

