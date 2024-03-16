package org.lashop.newback.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lashop.newback.config.security.AccountUserDetails;
import org.lashop.newback.config.security.JwtCore;
import org.lashop.newback.config.security.Token;
import org.lashop.newback.dto.AccountDto;
import org.lashop.newback.dto.requests.SignInRequest;
import org.lashop.newback.dto.requests.SignUpRequest;
import org.lashop.newback.models.Account;
import org.lashop.newback.services.AccountService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import java.security.Principal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationControllerTest {

    @Mock
    private AccountService accountService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtCore jwtCore;

    @InjectMocks
    private AuthorizationController authorizationController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authorizationController).build();
        objectMapper = new ObjectMapper();
    }
    @Test
    void testSignIn_Success() throws Exception {
        // Arrange
        SignInRequest signInRequest = SignInRequest.builder().email("email@email.com").password("qwertyuiop").build();
        Authentication authentication = mock(Authentication.class);
        Token token = new Token("token");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtCore.generateToken(authentication)).thenReturn("token");


        // Act & Assert
        mockMvc.perform(post("/api/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(token)));
    }

    @Test
    void testSignIn_ThrowsException() throws Exception {
        // Arrange
        SignInRequest signInRequest = SignInRequest.builder().email("email@email.com").password("qwertyuiop").build();
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(BadCredentialsException.class);

        // Act & Assert
        mockMvc.perform(post("/api/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").value("not signedIn"));
    }


    @Test
    void testSignUp_Success() throws Exception {
        // Arrange
        SignUpRequest signUpRequest = SignUpRequest.builder().firstName("John").lastName("Doe").email("test@example.com").password("password").phoneNumber("1234567890").build();
        when(accountService.checkEmail(signUpRequest.getEmail())).thenReturn(false);
        doNothing().when(accountService).signUp(any(AccountDto.class));

        // Act & Assert
        mockMvc.perform(post("/api/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("user signedUp"));
        verify(accountService, times(1)).checkEmail(signUpRequest.getEmail());
        verify(accountService, times(1)).signUp(any());
    }

    @Test
    void testSignUp_EmailUsed() throws Exception {
        // Arrange
        SignUpRequest signUpRequest = SignUpRequest.builder().firstName("John").lastName("Doe").email("test@example.com").password("password").phoneNumber("1234567890").build();
        when(accountService.checkEmail(signUpRequest.getEmail())).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/api/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("email used"));
    }
}