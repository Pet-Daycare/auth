package id.ac.ui.cs.advprog.b10.petdaycare.auth.service;



import id.ac.ui.cs.advprog.b10.petdaycare.auth.controller.AuthenticationController;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.core.AuthManager;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.dto.AuthTransactionDto;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.dto.AuthenticationRequest;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.dto.AuthenticationResponse;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.dto.RegisterRequest;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.exceptions.InvalidTokenException;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.exceptions.UserAlreadyExistException;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.exceptions.UsernameAlreadyExistException;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.model.PetWallet;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.model.User;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;


// Do not change this code
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
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
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .username(request.getUsername())
                .active(true)
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .petWallet(new PetWallet())
                .build();
        userRepository.save(user);

        return user;

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var jwtToken = jwtService.generateToken(user);
        authManager.registerNewToken(jwtToken, request.getUsername());

        return AuthenticationResponse.builder().token(jwtToken).build();
//        return null;
    }

//    public String verify(String token) {
//        try{
//            return Objects.requireNonNull(userRepository.findByUsername(authManager.getUsername(token)).orElse(null)).getId().toString();
//        } catch (Exception e){
//            return "invalid";
//        }
//    }

    public AuthTransactionDto verify(String token){
//        if (Objects.equals(token, "")){
//            return new AuthTransactionDto(-1, "", "invalid");
//        }

        try{
            var username = authManager.getUsername(token);
            return AuthTransactionDto.builder()
                    .idCustomer(Objects.requireNonNull(userRepository.findByUsername(authManager.getUsername(token)).orElse(null)).getId())
                    .token(token)
                    .username(username)
                    .build();
//            return Objects.requireNonNull(userRepository.findByUsername(authManager.getUsername(token)).orElse(null)).getId().toString();
        } catch (Exception e){
            throw new InvalidTokenException();
//            return new AuthTransactionDto(-1, "", "invalid");
        }
//        return userRepository.findByUsername(authManager.getUsername(token)).stream().findFirst().orElse(null);
    }


    public void logout(String token){
        if(authManager.getUsername(token) == null){
            throw new InvalidTokenException();
        } else {
            authManager.removeToken(token);
        }
    }

}
