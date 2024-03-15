package org.lashop.newback.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
import org.springframework.boot.test.context.SpringBootTest;
import java.util.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


public class UsersServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private UsersServiceImpl usersService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAccounts() {
        List<Account> accounts = List.of(
                Account.builder()
                        .id(1)
                        .firstName("name1")
                        .lastName("surname1")
                        .email("email1")
                        .phoneNumber("phone1")
                        .password("password1")
                        .personalSale(0)
                        .role(Account.Role.USER)
                        .accountState(Account.State.CONFIRMED)
                        .addresses(List.of(Address.builder().id(1).build()))
                        .cards(List.of(Card.builder().id(1).build()))
                        .orders(List.of(Orders.builder().id(1).build()))
                .build(),
                Account.builder()
                        .id(2)
                        .firstName("name2")
                        .lastName("surname2")
                        .email("email2")
                        .phoneNumber("phone2")
                        .password("password2")
                        .personalSale(0)
                        .role(Account.Role.USER)
                        .accountState(Account.State.CONFIRMED)
                        .addresses(List.of(Address.builder().id(2).build()))
                        .cards(List.of(Card.builder().id(2).build()))
                        .orders(List.of(Orders.builder().id(2).build()))
                        .build());

        when(accountRepository.findAll()).thenReturn(accounts);

        List<AccountDto> result = usersService.getAllAccounts();

        assertEquals(AccountDto.from(accounts), result);
    }

    @Test
    void testTakeAccountDeleted() {
        String email = "test@example.com";
        Account account = Account.builder()
                .id(2)
                .firstName("name2")
                .lastName("surname2")
                .email("test@example.com")
                .phoneNumber("phone2")
                .password("password2")
                .personalSale(0)
                .role(Account.Role.USER)
                .accountState(Account.State.CONFIRMED)
                .addresses(List.of(Address.builder().id(2).build()))
                .cards(List.of(Card.builder().id(2).build()))
                .orders(List.of(Orders.builder().id(2).build()))
                .build();

        when(accountRepository.findByEmail(email)).thenReturn(Optional.of(account));

        usersService.takeAccountDeleted(email);

        verify(accountRepository, times(1)).changeAccountState(account.getId(), Account.State.DELETED);
    }

    @Test
    void testTakeAccountDeleted_ShouldThrowException_WhenAccountNotFound() {
        String email = "test@example.com";

        when(accountRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usersService.takeAccountDeleted(email));
    }

    @Test
    void testTakeAccountBanned() {
        String email = "test@example.com";
        Account account = Account.builder()
                .id(2)
                .firstName("name2")
                .lastName("surname2")
                .email("test@example.com")
                .phoneNumber("phone2")
                .password("password2")
                .personalSale(0)
                .role(Account.Role.USER)
                .accountState(Account.State.CONFIRMED)
                .addresses(List.of(Address.builder().id(2).build()))
                .cards(List.of(Card.builder().id(2).build()))
                .orders(List.of(Orders.builder().id(2).build()))
                .build();

        when(accountRepository.findByEmail(email)).thenReturn(Optional.of(account));

        usersService.takeAccountBanned(email);

        verify(accountRepository, times(1)).changeAccountState(account.getId(), Account.State.BANNED);
    }

    @Test
    void testTakeAccountBanned_ShouldThrowException_WhenAccountNotFound() {
        String email = "test@example.com";

        when(accountRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usersService.takeAccountBanned(email));
    }

    @Test
    void testTakeAccountConfirmed() {
        String email = "test@example.com";
        Account account = Account.builder()
                .id(2)
                .firstName("name2")
                .lastName("surname2")
                .email("test@example.com")
                .phoneNumber("phone2")
                .password("password2")
                .personalSale(0)
                .role(Account.Role.USER)
                .accountState(Account.State.NOT_CONFIRMED)
                .addresses(List.of(Address.builder().id(2).build()))
                .cards(List.of(Card.builder().id(2).build()))
                .orders(List.of(Orders.builder().id(2).build()))
                .build();

        when(accountRepository.findByEmail(email)).thenReturn(Optional.of(account));

        usersService.takeAccountConfirmed(email);

        verify(accountRepository, times(1)).changeAccountState(account.getId(), Account.State.CONFIRMED);
    }

    @Test
    void testTakeAccountConfirmed_ShouldThrowException_WhenAccountNotFound() {
        String email = "test@example.com";

        when(accountRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usersService.takeAccountConfirmed(email));
    }

    @Test
    void testTakeRoleUser() {
        String email = "test@example.com";
        Account account = Account.builder()
                .id(2)
                .firstName("name2")
                .lastName("surname2")
                .email("test@example.com")
                .phoneNumber("phone2")
                .password("password2")
                .personalSale(0)
                .role(Account.Role.ADMIN)
                .accountState(Account.State.CONFIRMED)
                .addresses(List.of(Address.builder().id(2).build()))
                .cards(List.of(Card.builder().id(2).build()))
                .orders(List.of(Orders.builder().id(2).build()))
                .build();

        when(accountRepository.findByEmail(email)).thenReturn(Optional.of(account));

        usersService.takeRoleUser(email);

        verify(accountRepository, times(1)).changeAccountRole(account.getId(), Account.Role.USER);
    }

    @Test
    void testTakeRoleUser_ShouldThrowException_WhenAccountNotFound() {
        String email = "test@example.com";

        when(accountRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usersService.takeRoleUser(email));
    }

    @Test
    void testTakeRoleAdmin() {
        String email = "test@example.com";
        Account account = Account.builder()
                .id(2)
                .firstName("name2")
                .lastName("surname2")
                .email("test@example.com")
                .phoneNumber("phone2")
                .password("password2")
                .personalSale(0)
                .role(Account.Role.USER)
                .accountState(Account.State.CONFIRMED)
                .addresses(List.of(Address.builder().id(2).build()))
                .cards(List.of(Card.builder().id(2).build()))
                .orders(List.of(Orders.builder().id(2).build()))
                .build();

        when(accountRepository.findByEmail(email)).thenReturn(Optional.of(account));

        usersService.takeRoleAdmin(email);

        verify(accountRepository, times(1)).changeAccountRole(account.getId(), Account.Role.ADMIN);
    }

    @Test
    void testTakeRoleAdmin_ShouldThrowException_WhenAccountNotFound() {
        String email = "test@example.com";

        when(accountRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usersService.takeRoleAdmin(email));
    }

    // Similarly, write tests for other methods like takeAccountBanned, takeAccountConfirmed, takeRoleUser, takeRoleAdmin
}