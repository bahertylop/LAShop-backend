package org.lashop.newback.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lashop.newback.dto.AddressDto;
import org.lashop.newback.models.Account;
import org.lashop.newback.services.AddressService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.lashop.newback.config.security.AccountUserDetails;

import java.security.Principal;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class AddressControllerTest {

    @Mock
    private AddressService addressService;

    @InjectMocks
    private AddressController addressController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(addressController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetUserAddresses_Success() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).build());
        List<AddressDto> addresses = List.of(AddressDto.builder().address("address1").build(), AddressDto.builder().address("address2").build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;
        when(addressService.getAllAddresses(userDetails.getId())).thenReturn(addresses);

        ResponseEntity<?> response = addressController.getUserAddresses(principal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(addresses, response.getBody());

        mockMvc.perform(get("/api/address/get").principal(principal))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].address").value("address1"))
                .andExpect(jsonPath("$[1].address").value("address2"))
        ;
    }


    @Test
    void testGetUserAddresses_NoAuthorized() {
        Principal principal = null;

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ResponseEntity<?> response = addressController.getUserAddresses(principal);
        });
    }

    @Test
    void testGetUserAddressesThrowsException() throws Exception {
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;
        when(addressService.getAllAddresses(userDetails.getId())).thenThrow(new RuntimeException("exception"));

        ResponseEntity<?> response = addressController.getUserAddresses(principal);
        assertEquals("exception", response.getBody());

        mockMvc.perform(get("/api/address/get").principal(principal))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$").value("exception"));
        ;
    }

    @Test
    void testAddAddress_Success() throws Exception {
        String address = "address";
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        ResponseEntity<?> response = addressController.addAddress(address, principal);
        assertEquals("address added", response.getBody());
        verify(addressService, times(1)).addNewAddress(userDetails.getId(), address);

        mockMvc.perform(post("/api/address/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(address))
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(jsonPath("$").value("address added"));
    }

    @Test
    void testAddAddress_NoAuthorize() throws Exception {
        String address = "address";
        Principal principal = null;

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            ResponseEntity<?> response = addressController.addAddress(address, principal);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("user no authorized", response.getBody());
        });
    }

    @Test
    void testAddAddress_NoAddress() throws Exception {
        String address = null;
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;

        ResponseEntity<?> response = addressController.addAddress(address, principal);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("request has empty body", response.getBody());

        ResultActions res = mockMvc.perform(post("/api/address/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(principal))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAddAddressThrowsException() throws Exception {
        String address = "address";
        AccountUserDetails userDetails = new AccountUserDetails(Account.builder().id(1L).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        Principal principal = (Principal) authentication;
        doThrow(new RuntimeException("exception")).when(addressService).addNewAddress(userDetails.getId(), address);

        ResponseEntity<?> response = addressController.addAddress(address, principal);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("exception", response.getBody());

        mockMvc.perform(post("/api/address/add").principal(principal).contentType(MediaType.APPLICATION_JSON)
                        .content(address))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("exception"));
        ;
    }
}