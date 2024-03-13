package org.lashop.newback.services.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.lashop.newback.dto.AccountDto;
import org.lashop.newback.models.Account;
import org.lashop.newback.models.Address;
import org.lashop.newback.models.Card;
import org.lashop.newback.models.Orders;
import org.lashop.newback.repositories.AccountRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignUp() {
        AccountDto accountDto = AccountDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .phoneNumber("1234567890")
                .password("password")
                .role("USER")
                .accountState("DELETED")
                .personalSale(10)
                .build();
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        accountService.signUp(accountDto);

        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testGetAccount() {
        Long accountId = 1L;
        Account account = Account.builder()
                .id(accountId)
                .firstName("name")
                .lastName("surName")
                .email("email@email")
                .phoneNumber("1234567890")
                .password("qwertyuiop")
                .personalSale(10)
                .role(Account.Role.USER)
                .accountState(Account.State.CONFIRMED)
                .addresses(List.of(Address.builder().id(1).build(), Address.builder().id(2).build()))
                .cards(List.of(Card.builder().id(1).build()))
                .orders(List.of(Orders.builder().id(1).build()))
                .build();
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        AccountDto accountDto = accountService.getAccount(accountId);

        Assertions.assertEquals(accountId, accountDto.getId());
        Assertions.assertEquals(AccountDto.from(account), accountDto);
    }

    @Test
    void testGetAccountThrowsException() {
        Long accountId = 1L;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class, () -> {
            accountService.getAccount(accountId);
        });
    }

    @Test
    void testValidEmailAndPassword() {
        String email = "john@example.com";
        String password = "password";

        Account account = Account.builder()
                .id(1L)
                .firstName("name")
                .lastName("surName")
                .email(email)
                .phoneNumber("1234567890")
                .password(password)
                .personalSale(10)
                .role(Account.Role.USER)
                .accountState(Account.State.CONFIRMED)
                .addresses(List.of(Address.builder().id(1).build(), Address.builder().id(2).build()))
                .cards(List.of(Card.builder().id(1).build()))
                .orders(List.of(Orders.builder().id(1).build()))
                .build();

        when(accountRepository.findByEmail(email)).thenReturn(Optional.of(account));
        when(passwordEncoder.matches(password, account.getPassword())).thenReturn(true);

        AccountDto accountDto = accountService.validEmailAndPassword(email, password);

        Assertions.assertEquals(email, accountDto.getEmail());
    }

    @Test
    void testValidEmailAndPasswordThrowsException() {
        String email = "john@example.com";
        String password = "password";

        Account account = Account.builder()
                .id(1L)
                .firstName("name")
                .lastName("surName")
                .email(email)
                .phoneNumber("1234567890")
                .password(password)
                .personalSale(10)
                .role(Account.Role.USER)
                .accountState(Account.State.CONFIRMED)
                .addresses(List.of(Address.builder().id(1).build(), Address.builder().id(2).build()))
                .cards(List.of(Card.builder().id(1).build()))
                .orders(List.of(Orders.builder().id(1).build()))
                .build();

        when(accountRepository.findByEmail(email)).thenReturn(Optional.of(account));
        when(passwordEncoder.matches(password, account.getPassword())).thenReturn(false);

        Assertions.assertThrows(RuntimeException.class, () -> {
            accountService.validEmailAndPassword(email, password);
        });
    }

    @Test
    void testChangePassword() {
        Long accountId = 1L;
        String newPassword = "newPassword";
        accountService.changePassword(accountId, newPassword);
        verify(accountRepository, times(1)).changePassword(accountId, newPassword);
    }

    @Test
    void testChangeEmail() {
        Long accountId = 1L;
        String newEmail = "newEmail@example.com";
        accountService.changeEmail(accountId, newEmail);
        verify(accountRepository, times(1)).changeEmail(accountId, newEmail);
    }

    @Test
    void testChangePersonalSale() {
        Long accountId = 1L;
        int newSale = 20;
        accountService.changePersonalSale(accountId, newSale);
        verify(accountRepository, times(1)).changePersonalSale(accountId, newSale);
    }

    @Test
    void testChangePhone() {
        Long accountId = 1L;
        String newPhone = "1234567890";
        accountService.changePhone(accountId, newPhone);
        verify(accountRepository, times(1)).changePhone(accountId, newPhone);
    }

    @Test
    void testDeleteAccount() {
        Long accountId = 1L;
        accountService.deleteAccount(accountId);
        verify(accountRepository, times(1)).deleteById(accountId);
    }

    @Test
    void testCheckEmail() {
        String email = "test@example.com";
        when(accountRepository.findByEmail(email)).thenReturn(Optional.of(Account.builder().build()));

        boolean emailExists = accountService.checkEmail(email);

        Assertions.assertTrue(emailExists);
    }
}