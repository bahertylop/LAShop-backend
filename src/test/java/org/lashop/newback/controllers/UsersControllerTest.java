package org.lashop.newback.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lashop.newback.config.security.AccountUserDetails;
import org.lashop.newback.dto.AccountDto;
import org.lashop.newback.dto.requests.AccountEmailRequest;
import org.lashop.newback.models.Account;
import org.lashop.newback.services.ShoeTypeService;
import org.lashop.newback.services.UsersService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UsersControllerTest {

    @Mock
    private UsersService usersService;

    @InjectMocks
    private UsersController usersController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(usersController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetAllAccounts() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        List<AccountDto> dtos = List.of(
                AccountDto.builder().id(1).email("email1").build(),
                AccountDto.builder().id(2).email("email2").build()
        );

        when(usersService.getAllAccounts()).thenReturn(dtos);

        mockMvc.perform(get("/api/adm/users/all")
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(dtos.size()))
                .andExpect(content().json(objectMapper.writeValueAsString(dtos)));
    }

    @Test
    public void testMakeAccountDeleted() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        AccountEmailRequest request = AccountEmailRequest.builder().email("email").build();

        doNothing().when(usersService).takeAccountDeleted(request.getEmail());

        mockMvc.perform(post("/api/adm/users/deleteAccount")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("state changed to deleted"));

        verify(usersService).takeAccountDeleted(request.getEmail());
    }

    @Test
    public void testMakeAccountDeletedEmptyBody() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        mockMvc.perform(post("/api/adm/users/deleteAccount")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("request has empty body"));
    }

    @Test
    public void testMakeAccountDeletedThrows() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        AccountEmailRequest request = AccountEmailRequest.builder().email("email").build();

        doThrow(new RuntimeException("exception")).when(usersService).takeAccountDeleted(request.getEmail());

        mockMvc.perform(post("/api/adm/users/deleteAccount")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("exception"));

        verify(usersService).takeAccountDeleted(request.getEmail());
    }

    @Test
    public void testMakeAccountBanned() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        AccountEmailRequest request = AccountEmailRequest.builder().email("email").build();

        doNothing().when(usersService).takeAccountBanned(request.getEmail());

        mockMvc.perform(post("/api/adm/users/banAccount")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("state changed to banned"));

        verify(usersService).takeAccountBanned(request.getEmail());
    }

    @Test
    public void testMakeAccountBannedEmptyBody() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        mockMvc.perform(post("/api/adm/users/banAccount")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("request has empty body"));
    }

    @Test
    public void testMakeAccountBannedThrows() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        AccountEmailRequest request = AccountEmailRequest.builder().email("email").build();

        doThrow(new RuntimeException("exception")).when(usersService).takeAccountBanned(request.getEmail());

        mockMvc.perform(post("/api/adm/users/banAccount")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("exception"));

        verify(usersService).takeAccountBanned(request.getEmail());
    }

    @Test
    public void testMakeAccountConfirmed() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        AccountEmailRequest request = AccountEmailRequest.builder().email("email").build();

        doNothing().when(usersService).takeAccountConfirmed(request.getEmail());

        mockMvc.perform(post("/api/adm/users/confirmAccount")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("state changed to confirmed"));

        verify(usersService).takeAccountConfirmed(request.getEmail());
    }

    @Test
    public void testMakeAccountConfirmedEmptyBody() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        mockMvc.perform(post("/api/adm/users/confirmAccount")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("request has empty body"));
    }

    @Test
    public void testMakeAccountConfirmedThrows() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        AccountEmailRequest request = AccountEmailRequest.builder().email("email").build();

        doThrow(new RuntimeException("exception")).when(usersService).takeAccountConfirmed(request.getEmail());

        mockMvc.perform(post("/api/adm/users/confirmAccount")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("exception"));

        verify(usersService).takeAccountConfirmed(request.getEmail());
    }

    @Test
    public void testChangeToAdmin() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        AccountEmailRequest accountEmailRequest = AccountEmailRequest.builder().email("email").build();

        doNothing().when(usersService).takeRoleAdmin(accountEmailRequest.getEmail());

        mockMvc.perform(post("/api/adm/users/takeAdmin")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountEmailRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("role changed to admin"));

        verify(usersService).takeRoleAdmin(accountEmailRequest.getEmail());
    }

    @Test
    public void testChangeToAdminEmptyBody() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        mockMvc.perform(post("/api/adm/users/takeAdmin")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("request has empty body"));
    }

    @Test
    public void testChangeToAdminThrow() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        AccountEmailRequest accountEmailRequest = AccountEmailRequest.builder().email("email").build();

        doThrow(new RuntimeException("exception")).when(usersService).takeRoleAdmin(accountEmailRequest.getEmail());

        mockMvc.perform(post("/api/adm/users/takeAdmin")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountEmailRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("exception"));

        verify(usersService).takeRoleAdmin(accountEmailRequest.getEmail());
    }

    @Test
    public void testChangeToUser() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        AccountEmailRequest accountEmailRequest = AccountEmailRequest.builder().email("email").build();

        doNothing().when(usersService).takeRoleUser(accountEmailRequest.getEmail());

        mockMvc.perform(post("/api/adm/users/takeUser")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountEmailRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("role changed to user"));

        verify(usersService).takeRoleUser(accountEmailRequest.getEmail());
    }

    @Test
    public void testChangeToUserEmptyBody() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        mockMvc.perform(post("/api/adm/users/takeUser")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("request has empty body"));
    }

    @Test
    public void testChangeToUserThrow() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).role(Account.Role.ADMIN).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        AccountEmailRequest accountEmailRequest = AccountEmailRequest.builder().email("email").build();

        doThrow(new RuntimeException("exception")).when(usersService).takeRoleUser(accountEmailRequest.getEmail());

        mockMvc.perform(post("/api/adm/users/takeUser")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountEmailRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("exception"));

        verify(usersService).takeRoleUser(accountEmailRequest.getEmail());
    }

}