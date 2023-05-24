package id.ac.ui.cs.advprog.b10.petdaycare.auth.service;

import id.ac.ui.cs.advprog.b10.petdaycare.auth.core.AuthManager;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.dto.AuthenticationRequest;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.dto.AuthenticationResponse;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.dto.RegisterRequest;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.exceptions.InvalidTokenException;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.exceptions.UserAlreadyExistException;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.exceptions.UsernameAlreadyExistException;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.model.User;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.repository.TokenRepository;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthManager authManager;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenRepository tokenRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationService(userRepository, tokenRepository, passwordEncoder, jwtService, authenticationManager);
    }

    @Test
    void testRegisterSuccess() {
        RegisterRequest request = new RegisterRequest();
        request.setFullName("John Doe");
        request.setUsername("johndoe");
        request.setEmail("johndoe@example.com");
        request.setPassword("password");
        request.setRole("USER");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());

        User savedUser = User.builder()
                .fullName(request.getFullName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password("encodedPassword")
                .role(request.getRole())
                .build();
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User registeredUser = authenticationService.register(request);

        assertNotNull(registeredUser);
        assertEquals(request.getFullName(), registeredUser.getFullName());
        assertEquals(request.getUsername(), registeredUser.getUsername());
        assertEquals(request.getEmail(), registeredUser.getEmail());
        assertEquals(request.getRole(), registeredUser.getRole());

        verify(userRepository).findByEmail(request.getEmail());
        verify(userRepository).findByUsername(request.getUsername());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegisterUserAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("existing@example.com");
        request.setUsername("existingUser");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new User()));
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());

        assertThrows(UserAlreadyExistException.class, () -> authenticationService.register(request));

        verify(userRepository).findByEmail(request.getEmail());
        verify(userRepository, never()).findByUsername(request.getUsername());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegisterUsernameAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("new@example.com");
        request.setUsername("existingUser");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(new User()));

        assertThrows(UsernameAlreadyExistException.class, () -> authenticationService.register(request));

        verify(userRepository).findByEmail(request.getEmail());
        verify(userRepository).findByUsername(request.getUsername());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testAuthenticateSuccess() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("johndoe");
        request.setPassword("password");

        User user = User.builder()
                .username(request.getUsername())
                .password("encodedPassword")
                .id(1)
                .build();
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(user));

        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .token("jwtToken")
                .build();
        when(jwtService.generateToken(user)).thenReturn("jwtToken");

        AuthenticationResponse response = authenticationService.authenticate(request);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());

        verify(userRepository).findByUsername(request.getUsername());
        verify(jwtService).generateToken(user);
    }

    @Test
    void testAuthenticateUserNotFound() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("nonexistentuser");
        request.setPassword("password");

        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> authenticationService.authenticate(request));

        verify(userRepository).findByUsername(request.getUsername());
        verifyNoInteractions(jwtService);
    }

//    @Test
//    void testVerifyValidToken() {
//        String token = "validToken";
//        String username = "johndoe";
//
//        AuthenticationRequest request = new AuthenticationRequest();
//        request.setUsername(username);
//
//
//        User user = User.builder()
//                .id(1)
//                .username(username)
//                .build();
//        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
//        when(authManager.getUsername(token)).thenReturn(username);
//
//        AuthTransactionDto authTransactionDto = authenticationService.verify(token);
//
//        assertNotNull(authTransactionDto);
//        assertEquals(1, authTransactionDto.getIdCustomer());
//        assertEquals(token, authTransactionDto.getToken());
//        assertEquals(username, authTransactionDto.getUsername());
//
//        verify(userRepository).findByUsername(username);
//        verify(authManager).getUsername(token);
//    }

    @Test
    void testVerifyInvalidToken() {
        String token = "invalidToken";

        when(authManager.getUsername(token)).thenThrow(new InvalidTokenException());

        assertThrows(InvalidTokenException.class, () -> authenticationService.verify(token));

//        verify(authManager).getUsername(token);
        verifyNoInteractions(userRepository);
    }

//    @Test
//    void testLogoutValidToken() {
//        String token = "validToken";
//        String username = "johndoe";
//
//
//        User user = User.builder()
//                .id(1)
//                .username(username)
//                .build();
//        authManager.registerNewToken(token, username);
//
//
//        when(authManager.getUsername(token)).thenReturn(username);
//
//        authenticationService.logout(token);
//
//        verify(authManager).getUsername(token);
//        verify(authManager).removeToken(token);
//    }

    @Test
    void testLogoutInvalidToken() {
        String token = "invalidToken";

        when(authManager.getUsername(token)).thenReturn(null);

        assertThrows(InvalidTokenException.class, () -> authenticationService.logout(token));

//        verify(authManager).getUsername(token);
        verifyNoMoreInteractions(authManager);
    }
    // Add more test methods for other service methods

}
