package id.ac.ui.cs.advprog.b10.petdaycare.auth.config;

import id.ac.ui.cs.advprog.b10.petdaycare.auth.core.AuthManager;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private static final String JWT_HEADER = "Authorization";
    private static final String JWT_TOKEN_PREFIX = "Bearer";

    private final TokenRepository tokenRepository;

    private final AuthManager authManager = AuthManager.getInstance();

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader(JWT_HEADER);
        final String jwtToken;

        if (authHeader == null || !authHeader.startsWith(JWT_TOKEN_PREFIX)) {
            return;
        }

        jwtToken = authHeader.substring(7);
        var storedToken = tokenRepository.findByTokenString(jwtToken).orElse(null);
        if(storedToken != null){
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
        }

        authManager.removeToken(jwtToken);

    }
}
