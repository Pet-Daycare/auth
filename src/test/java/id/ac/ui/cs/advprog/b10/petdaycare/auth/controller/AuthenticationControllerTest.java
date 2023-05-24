package id.ac.ui.cs.advprog.b10.petdaycare.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.dto.AuthTransactionDto;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.dto.AuthenticationRequest;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.dto.AuthenticationResponse;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.dto.RegisterRequest;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.model.User;
import id.ac.ui.cs.advprog.b10.petdaycare.auth.service.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

 class AuthenticationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthenticationController(authenticationService)).build();
    }

    @Test
     void testRegister() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        // Set up the registerRequest object

        User registeredUser = User.builder()
                .role("ADMIN")
                .build();
        // Set up the registeredUser object

        when(authenticationService.register(any(RegisterRequest.class))).thenReturn(registeredUser);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(registeredUser.getUsername()));
    }

    @Test
     void testLogin() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("username", "password");
        // Set up the authenticationRequest object

        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequest);
        // Set up the authenticationResponse object

        when(authenticationService.authenticate(any(AuthenticationRequest.class))).thenReturn(authenticationResponse);

        if (authenticationResponse == null) {
            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(authenticationRequest)))
                    .andExpect(status().isBadRequest());
        } else {
            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(authenticationRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").value(authenticationResponse.getToken()));
        }
    }

    @Test
     void testVerifyToken() throws Exception {
        String token = "example_token";

        AuthTransactionDto authTransactionDto = new AuthTransactionDto();
        // Set up the authTransactionDto object

        when(authenticationService.verify(token)).thenReturn(authTransactionDto);

        mockMvc.perform(get("/api/v1/auth/verify-token/{token}", token))
                .andExpect(status().isOk());
    }

    @Test
     void testLogout() throws Exception {
        String token = "example_token";

        mockMvc.perform(post("/api/v1/auth/logout/{token}", token))
                .andExpect(status().isOk())
                .andExpect(content().string(token + " logout jalan"));
    }

    private static String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCreateCookie() {
        // Arrange
        String cookieName = "token";
        String value = "jwtToken";

        // Act
        Cookie cookie = AuthenticationController.createCookie(cookieName, value);

        // Assert
        assertEquals(cookieName, cookie.getName());
        assertEquals(value, cookie.getValue());
        assertTrue(cookie.getSecure());
        assertFalse(cookie.isHttpOnly());

        // Validate URL encoding and decoding
        String encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8);
        assertEquals(encodedValue, cookie.getValue());
        String decodedValue = java.net.URLDecoder.decode(encodedValue, StandardCharsets.UTF_8);
        assertEquals(value, decodedValue);
    }

    @Test
    void testClearAllCookies() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Cookie cookie1 = new Cookie("token", "value1");
        Cookie cookie2 = new Cookie("session", "value2");
        Cookie[] cookies = {cookie1, cookie2};

        when(request.getCookies()).thenReturn(cookies);

        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);

        // Act
        AuthenticationController.clearAllCookies(request, response);

        // Assert
        verify(response, times(2)).addCookie(cookieCaptor.capture());

        assertEquals(cookie1, cookieCaptor.getAllValues().get(0));
        assertEquals(cookie2, cookieCaptor.getAllValues().get(1));

        assertEquals(0, cookieCaptor.getValue().getMaxAge());
    }
}



//import com.fasterxml.jackson.databind.ObjectMapper;
//import id.ac.ui.cs.advprog.b10.petdaycare.auth.dto.AuthTransactionDto;
//import id.ac.ui.cs.advprog.b10.petdaycare.auth.dto.AuthenticationRequest;
//import id.ac.ui.cs.advprog.b10.petdaycare.auth.dto.AuthenticationResponse;
//import id.ac.ui.cs.advprog.b10.petdaycare.auth.dto.RegisterRequest;
//import id.ac.ui.cs.advprog.b10.petdaycare.auth.model.User;
//import id.ac.ui.cs.advprog.b10.petdaycare.auth.service.AuthenticationService;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MockMvcBuilder;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.Collections;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//public class AuthenticationControllerTest {
//    private MockMvc mockMvc;
//
//    @Mock
//    private AuthenticationService authenticationService;
//
//    @InjectMocks
//    private AuthenticationController authenticationController;
//
//    @BeforeEach
//    public void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
//    }
//
//    @Test
//    public void testRegister() throws Exception {
//        RegisterRequest registerRequest = new RegisterRequest("John", "Doe", "john@example.com", "password", "johndoe", "ADMIN");
//
//        User user = User.builder()
//                .role("ADMIN")
//                .build();
//        when(authenticationService.register(any(RegisterRequest.class)))
//                .thenReturn(user);
//
//        mockMvc.perform(post("/api/v1/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(registerRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("John Doe"))
//                .andExpect(jsonPath("$.email").value("john@example.com"));
//    }
//
//    @Test
//    public void testLogin() throws Exception {
//        AuthenticationRequest authenticationRequest = new AuthenticationRequest("john@example.com", "password");
//        AuthenticationResponse authenticationResponse = new AuthenticationResponse("token");
//
//        when(authenticationService.authenticate(any(AuthenticationRequest.class)))
//                .thenReturn(authenticationResponse);
//
//        mockMvc.perform(post("/api/v1/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(authenticationRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.token").value("token"))
//                .andExpect(header().exists("Set-Cookie"));
//    }
//
//    @Test
//    public void testVerifyToken() throws Exception {
//        String token = "token";
//        AuthTransactionDto authTransactionDto = new AuthTransactionDto(1, "John Doe", "john@example.com");
//
//        when(authenticationService.verify(token)).thenReturn(authTransactionDto);
//
//        mockMvc.perform(get("/api/v1/auth/verify-token/{token}", token))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(1))
//                .andExpect(jsonPath("$.name").value("John Doe"))
//                .andExpect(jsonPath("$.email").value("john@example.com"));
//    }
//
//    @Test
//    public void testLogout() throws Exception {
//        String token = "token";
//        HttpServletRequest request = new MockHttpServletRequest();
//        HttpServletResponse response = new MockHttpServletResponse();
//
//        mockMvc.perform(post("/api/v1/auth/logout/{token}", token))
//                .andExpect(status().isOk())
//                .andExpect(content().string(token + " logout jalan"));
//    }
//
//    private String asJsonString(Object obj) throws Exception {
//        return new ObjectMapper().writeValueAsString(obj);
//    }
//}
