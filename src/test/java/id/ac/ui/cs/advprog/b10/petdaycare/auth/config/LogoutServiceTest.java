package id.ac.ui.cs.advprog.b10.petdaycare.auth.config;

import static org.mockito.Mockito.*;

import id.ac.ui.cs.advprog.b10.petdaycare.auth.core.AuthManager;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.model.Token;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.repository.TokenRepository;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

class LogoutServiceTest {

    private LogoutService logoutService;
    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private AuthManager authManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        logoutService = new LogoutService(tokenRepository);
    }

    @Test
    void testLogout_ValidToken_ShouldExpireTokenAndRemoveFromAuthManager() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Authentication authentication = mock(Authentication.class);
        String authHeader = "Bearer token";
        String jwtToken = "token";

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(tokenRepository.findByTokenString(jwtToken)).thenReturn(Optional.of(mock(Token.class)));


        // Act
        logoutService.logout(request, response, authentication);

        // Assert
        verify(tokenRepository).findByTokenString(jwtToken);
        verify(tokenRepository).save(any(Token.class));

        authManager.removeToken(jwtToken);
        verify(authManager).removeToken(jwtToken);
    }

    @Test
    void testLogout_InvalidToken_ShouldNotExpireTokenOrRemoveFromAuthManager() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Authentication authentication = mock(Authentication.class);
        String authHeader = "InvalidToken";

        when(request.getHeader("Authorization")).thenReturn(authHeader);

        // Act
        logoutService.logout(request, response, authentication);

        // Assert
        verify(tokenRepository, never()).findByTokenString(anyString());
        verify(tokenRepository, never()).save(any(Token.class));
        verify(authManager, never()).removeToken(anyString());
    }
}
