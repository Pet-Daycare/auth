package id.ac.ui.cs.advprog.b10.petdaycare.auth.service;

import id.ac.ui.cs.advprog.b10.petdaycare.auth.core.AuthManager;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.dto.AuthTransactionDto;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.dto.AuthenticationRequest;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.dto.AuthenticationResponse;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.dto.RegisterRequest;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.exceptions.*;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.model.Token;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.model.TokenType;
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

import java.util.ArrayList;
import java.util.List;
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

        assertEquals("John Doe", request.getFullName());
        assertEquals("johndoe", request.getUsername());
        assertEquals("johndoe@example.com", request.getEmail());
        assertEquals("USER", request.getRole());
        assertEquals("password", request.getPassword());

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
    void testRegisterAdminSuccess() {
        RegisterRequest request = new RegisterRequest();
        request.setFullName("John Doe 12");
        request.setUsername("johndoe12");
        request.setEmail("johndoe12@example.com");
        request.setPassword("password");
        request.setRole("ADMIN");

        assertEquals("John Doe 12", request.getFullName());
        assertEquals("johndoe12", request.getUsername());
        assertEquals("johndoe12@example.com", request.getEmail());
        assertEquals("ADMIN", request.getRole());
        assertEquals("password", request.getPassword());

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
        request.setFullName("John Doe 12");
        request.setPassword("password");
        request.setRole("ADMIN");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(new User()));

        assertThrows(UsernameAlreadyExistException.class, () -> authenticationService.register(request));

        verify(userRepository).findByEmail(request.getEmail());
        verify(userRepository).findByUsername(request.getUsername());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegisterRoleUnknown() {
        RegisterRequest request = new RegisterRequest();
        request.setFullName("John Doe 13");
        request.setUsername("johndoe14");
        request.setEmail("johndoe13@example.com");
        request.setPassword("password");
        request.setRole("unknown");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        assertThrows(UserRoleRegisterException.class, () -> authenticationService.register(request));

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
        List<Token> tokens = new ArrayList<>();
        var token = Token.builder()
                .user(user)
                .tokenString("jwt1")
                .tokenType(TokenType.BEARER)
                .expired(true)
                .revoked(true)
                .build();
//        tokens.add(token);

        when(jwtService.generateToken(user)).thenReturn("jwtToken");
        when(tokenRepository.findAllValidTokensByUser(user.getId().toString())).thenReturn(tokens);

        AuthenticationResponse response = authenticationService.authenticate(request);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());

        verify(userRepository).findByUsername(request.getUsername());
        verify(jwtService).generateToken(user);
        verify(tokenRepository).findAllValidTokensByUser(user.getId().toString());

//        verify(authManager).revokeAllUserTokens(user);
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void testVerifyValidToken() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("johndoe");
        request.setPassword("password");
        String token = "jwtToken";

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

        AuthTransactionDto authTransactionDto = authenticationService.verify(token);

        assertNotNull(authTransactionDto);
        assertEquals(1, authTransactionDto.getIdCustomer());
        assertEquals(token, authTransactionDto.getToken());
    }

    @Test
    void testSameUserLoginTwice() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("johndoe");
        request.setPassword("password");

        User user = User.builder()
                .username(request.getUsername())
                .password("encodedPassword")
                .id(1)
                .build();
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(user));

        assertThrows(UsernameAlreadyLoggedIn.class, () -> authenticationService.authenticate(request));
    }

    @Test
    void testLogoutWithInvalidToken() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("johndoe");
        request.setPassword("password");

        String token = "invalidToken";
        AuthManager authManagerMock = mock(AuthManager.class);
        when(authManagerMock.getUsername(token)).thenReturn(null);

        assertThrows(InvalidTokenException.class, () -> authenticationService.logout(token));

        verifyNoInteractions(userRepository);

        authManagerMock.getUsername(token);
        verify(authManagerMock).getUsername(token);
    }

    @Test
    void testLogoutWithValidToken() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("johndoe18");
        request.setPassword("password");

        User user = User.builder()
                .username(request.getUsername())
                .password("encodedPassword12")
                .id(7)
                .build();
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(user));

        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .token("jwtToken12")
                .build();

        List<Token> tokens = new ArrayList<>();
        var token = Token.builder()
                .user(user)
                .tokenString("jwt1")
                .tokenType(TokenType.BEARER)
                .expired(true)
                .revoked(true)
                .build();
        tokens.add(token);

        when(jwtService.generateToken(user)).thenReturn("jwtToken12");
        when(tokenRepository.findAllValidTokensByUser(user.getId().toString())).thenReturn(tokens);


        AuthenticationResponse response = authenticationService.authenticate(request);

        assertNotNull(response);
        assertEquals("jwtToken12", response.getToken());

        verify(tokenRepository).findAllValidTokensByUser(user.getId().toString());
        verify(userRepository).findByUsername(request.getUsername());
        verify(jwtService).generateToken(user);


        Token tokenLogout = Token.builder()
                .id(4)
                .user(user)
                .tokenString("jwtToken12")
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();


        when(tokenRepository.findByTokenString(tokenLogout.getTokenString())).thenReturn(Optional.of(tokenLogout));
        when(authManager.getUsername(tokenLogout.getTokenString())).thenReturn("johndoe18");


        authenticationService.logout(tokenLogout.getTokenString());
        verify(tokenRepository).findByTokenString(tokenLogout.getTokenString());
        verify(tokenRepository).save(tokenLogout);

        authManager.removeToken(tokenLogout.getTokenString());
        verify(authManager).removeToken(tokenLogout.getTokenString());


        assertEquals(4, tokenLogout.getId());
        assertEquals(user, tokenLogout.getUser());
        assertEquals(TokenType.BEARER, tokenLogout.getTokenType());
        assertEquals("jwtToken12", tokenLogout.getTokenString());
        assertEquals(true, tokenLogout.isExpired());
        assertEquals(true, tokenLogout.isRevoked());

 }


    @Test
    void testAuthenticateUserNotFound() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("nonexistentuser");
        request.setPassword("password");

        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());

        assertThrows(UsernameDoesNotExist.class, () -> authenticationService.authenticate(request));

        verify(userRepository).findByUsername(request.getUsername());
        verifyNoInteractions(jwtService);
    }


    @Test
    void testVerifyInvalidToken() {
        String token = "invalidToken";

        when(authManager.getUsername(token)).thenThrow(new InvalidTokenException());

        assertThrows(InvalidTokenException.class, () -> authenticationService.verify(token));

        verifyNoInteractions(userRepository);
    }

}
