package id.ac.ui.cs.advprog.b10.petdaycare.auth.controller;


import id.ac.ui.cs.advprog.b10.petdaycare.auth.dto.AuthTransactionDto;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.exceptions.InvalidTokenException;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.dto.AuthenticationRequest;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.dto.AuthenticationResponse;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.dto.RegisterRequest;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.service.AuthenticationService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    @PostMapping("/register")
    public ResponseEntity<User> register (
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login (
            @RequestBody AuthenticationRequest request, HttpServletResponse response
    ) {

        AuthenticationResponse authResp = authenticationService.authenticate(request);

        if(authResp != null){
            String jwtToken = authResp.getToken();

            var cookie = AuthenticationController.createCookie("token", jwtToken);
            response.addCookie(cookie);
        } else {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(authResp);
    }

    @GetMapping("/verify-token/{token}")
    public ResponseEntity<AuthTransactionDto> verifyToken (
            @PathVariable String token, HttpServletRequest request
    ) {

        return ResponseEntity.ok(authenticationService.verify(token));
    }

    @PostMapping(path = "/logout/{token}")
    public String logout(HttpServletRequest request, HttpServletResponse response,
                         @PathVariable String token)  {
        try {
            authenticationService.logout(token);
        } catch (InvalidTokenException e){
            return "Something happened";
        }

        return token + " logout jalan";
    }

    public static Cookie createCookie(String cookieName, String value){
        var newUrlEncoding = URLEncoder.encode(value, StandardCharsets.UTF_8);
        var oldUrlEncoding = newUrlEncoding.replace("+", "%20");
        var cookie = new Cookie(cookieName, oldUrlEncoding);
        cookie.setSecure(true);
        cookie.setHttpOnly(false);
        return cookie;
    }

    static void clearAllCookies(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }
}
