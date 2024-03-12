package org.lashop.newback.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lashop.newback.models.Account;
import org.lashop.newback.models.Address;
import org.lashop.newback.models.Card;
import org.lashop.newback.models.Orders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountDtoTest {

    @Test
    void testFromForOneObject() {
        Account account = Account.builder()
                .id(3)
                .firstName("qwerty")
                .lastName("qwerty")
                .email("qwerty@mail.com")
                .phoneNumber("89083006654")
                .password("qwertyuiop")
                .personalSale(0)
                .role(Account.Role.USER)
                .accountState(Account.State.DELETED)
                .cards(List.of(Card.builder().id(1).build(), Card.builder().id(2).build(), Card.builder().id(3).build()))
                .addresses(List.of(Address.builder().id(1).build(), Address.builder().id(2).build(), Address.builder().id(3).build()))
                .orders(List.of(Orders.builder().id(1).build(), Orders.builder().id(2).build(), Orders.builder().id(3).build()))
                .build();

        AccountDto accountDto = AccountDto.from(account);
        AccountDto resAccountDto = AccountDto.builder()
                .id(3)
                .firstName("qwerty")
                .lastName("qwerty")
                .email("qwerty@mail.com")
                .phoneNumber("89083006654")
                .password("qwertyuiop")
                .personalSale(0)
                .role("USER")
                .accountState("DELETED")
                .addresses(List.of(1L, 2L, 3L))
                .cards(List.of(1L, 2L, 3L))
                .orders(List.of(1L, 2L, 3L))
                .build();
        Assertions.assertEquals(accountDto, resAccountDto);
    }

    @Test
    void testFromForListOfObjects() {
        List<Account> accountList = List.of(
                Account.builder()
                        .id(1)
                        .firstName("John")
                        .lastName("Doe")
                        .email("john.doe@example.com")
                        .phoneNumber("123456789")
                        .password("password123")
                        .personalSale(10)
                        .role(Account.Role.USER)
                        .accountState(Account.State.CONFIRMED)
                        .cards(List.of(Card.builder().id(1).build(), Card.builder().id(2).build()))
                        .addresses(List.of(Address.builder().id(1).build(), Address.builder().id(2).build()))
                        .orders(List.of(Orders.builder().id(1).build(), Orders.builder().id(2).build()))
                        .build(),
                Account.builder()
                        .id(2)
                        .firstName("Jane")
                        .lastName("Smith")
                        .email("jane.smith@example.com")
                        .phoneNumber("987654321")
                        .password("password456")
                        .personalSale(20)
                        .role(Account.Role.ADMIN)
                        .accountState(Account.State.NOT_CONFIRMED)
                        .cards(List.of(Card.builder().id(3).build(), Card.builder().id(4).build()))
                        .addresses(List.of(Address.builder().id(3).build(), Address.builder().id(4).build()))
                        .orders(List.of(Orders.builder().id(3).build(), Orders.builder().id(4).build()))
                        .build()
        );

        List<AccountDto> accountDtoList = AccountDto.from(accountList);

        List<AccountDto> expectedAccountDtoList = List.of(
                AccountDto.builder()
                        .id(1)
                        .firstName("John")
                        .lastName("Doe")
                        .email("john.doe@example.com")
                        .phoneNumber("123456789")
                        .password("password123")
                        .personalSale(10)
                        .role("USER")
                        .accountState("CONFIRMED")
                        .addresses(List.of(1L, 2L))
                        .cards(List.of(1L, 2L))
                        .orders(List.of(1L, 2L))
                        .build(),
                AccountDto.builder()
                        .id(2)
                        .firstName("Jane")
                        .lastName("Smith")
                        .email("jane.smith@example.com")
                        .phoneNumber("987654321")
                        .password("password456")
                        .personalSale(20)
                        .role("ADMIN")
                        .accountState("NOT_CONFIRMED")
                        .addresses(List.of(3L, 4L))
                        .cards(List.of(3L, 4L))
                        .orders(List.of(3L, 4L))
                        .build()
        );

        Assertions.assertEquals(expectedAccountDtoList, accountDtoList);
    }

}