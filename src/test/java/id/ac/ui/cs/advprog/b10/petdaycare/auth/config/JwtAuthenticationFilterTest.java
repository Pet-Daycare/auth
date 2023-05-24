package id.ac.ui.cs.advprog.b10.petdaycare.auth.config;

import id.ac.ui.cs.advprog.b10.petdaycare.auth.repository.TokenRepository;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.*;

 class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JwtAuthenticationFilter authenticationFilter;

    @Mock
    private TokenRepository tokenRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        authenticationFilter = new JwtAuthenticationFilter(jwtService, userDetailsService, tokenRepository);
    }

    @Test
    void testDoFilterInternal_WithoutAuthorizationHeader_ShouldContinueFilterChain() throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        authenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_WithInvalidAuthorizationHeader_ShouldContinueFilterChain() throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("InvalidToken");

        // Act
        authenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);
    }

    // Add more test cases to cover other scenarios of doFilterInternal() method

    @Test
    void testDoFilterInternal_WithValidAuthorizationHeader_ShouldAuthenticateUser() throws Exception {
        // Arrange
        String validJwtToken = "ValidJwtToken";
        String userEmail = "test@example.com";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + validJwtToken);
        when(jwtService.extractUsername(validJwtToken)).thenReturn(userEmail);
        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(null);

        // Act
        authenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(userDetailsService, times(1)).loadUserByUsername(userEmail);
        verify(jwtService, times(1)).isTokenValid(validJwtToken, null);
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
