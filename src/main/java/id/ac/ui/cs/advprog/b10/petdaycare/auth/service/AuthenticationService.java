package id.ac.ui.cs.advprog.b10.petdaycare.auth.service;



import id.ac.ui.cs.advprog.b10.petdaycare.auth.core.AuthManager;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.dto.AuthTransactionDto;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.dto.AuthenticationRequest;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.dto.AuthenticationResponse;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.dto.RegisterRequest;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.exceptions.InvalidTokenException;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.exceptions.UserAlreadyExistException;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.exceptions.UsernameAlreadyExistException;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.exceptions.UsernameAlreadyLoggedIn;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.model.Token;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.model.TokenType;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.model.User;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.repository.TokenRepository;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthManager authManager = AuthManager.getInstance();

    private final AuthenticationManager authenticationManager;

    public User register(RegisterRequest request) {
        var checkUser = userRepository.findByEmail(request.getEmail()).orElse(null);

        if(checkUser != null) {
            throw new UserAlreadyExistException();
        }

        checkUser = userRepository.findByUsername(request.getUsername()).orElse(null);
        if(checkUser != null) {
            throw new UsernameAlreadyExistException();
        }

        var user = User.builder()
                .fullName(request.getFullName())
                .username(request.getUsername())
                .active(true)
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken);

        return user;

    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .tokenString(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            var user = userRepository.findByUsername(request.getUsername()).orElseThrow();

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            var jwtToken = jwtService.generateToken(user);
            authManager.registerNewToken(jwtToken, request.getUsername());

            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);

            return AuthenticationResponse.builder().token(jwtToken).build();
        } catch(UsernameAlreadyLoggedIn e){
            throw new UsernameNotFoundException("Username of this user already login!");
        } catch (Exception e) {
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }

    private void revokeAllUserTokens(User user){
        var validUserToken = tokenRepository.findAllValidTokensByUser(user.getId().toString());
        if (validUserToken.isEmpty()){
            return;
        }
        validUserToken.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validUserToken);
    }

    public AuthTransactionDto verify(String token){
        try{
            var username = authManager.getUsername(token);
            return AuthTransactionDto.builder()
                    .idCustomer(Objects.requireNonNull(userRepository.findByUsername(authManager.getUsername(token)).orElse(null)).getId())
                    .token(token)
                    .username(username)
                    .build();
        } catch (InvalidTokenException e){
            throw new InvalidTokenException();
        }
    }


    public void logout(String token){
        if(authManager.getUsername(token) == null){
            throw new InvalidTokenException();
        } else {
            var storedToken = tokenRepository.findByTokenString(token).orElse(null);
            if(storedToken != null){
                storedToken.setExpired(true);
                storedToken.setRevoked(true);
                tokenRepository.save(storedToken);
            }

            authManager.removeToken(token);
        }
    }

}
