package id.ac.ui.cs.advprog.b10.petdaycare.auth.controller;


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
    public ResponseEntity<AuthenticationResponse> register (
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login (
            @RequestBody AuthenticationRequest request, HttpServletResponse response
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request, response));
    }

    @PostMapping("/verify-token/{token}")
    public ResponseEntity<User> verifyToken (
            @PathVariable String token
    ) {
        return ResponseEntity.ok(authenticationService.verify(token));
    }



    @GetMapping(path = "/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response,
                         @CookieValue(name="token", defaultValue = "") String token)  {

        clearAllCookies(request, response);
        try {
            authenticationService.logout(token);
        } catch (Exception e){

        }

        return "redirect:login";
    }

    public static Cookie createCookie(String cookieName, String value){
        var newUrlEncoding = URLEncoder.encode(value, StandardCharsets.UTF_8);
        var oldUrlEncoding = newUrlEncoding.replace("+", "%20");
        var cookie = new Cookie(cookieName, oldUrlEncoding);
        cookie.setSecure(true);
        cookie.setHttpOnly(false);
        return cookie;
    }

    private static void clearAllCookies(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }
}
