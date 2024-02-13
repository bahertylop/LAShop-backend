package org.lashop.newback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lashop.newback.models.Account;
import org.lashop.newback.models.Address;
import org.lashop.newback.models.Card;
import org.lashop.newback.models.Orders;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDto {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private int personalSale;
    private String role;
    private String accountState;
    private List<Long> addresses;
    private List<Long> cards;
    private List<Long> orders;

    public static AccountDto from(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .email(account.getEmail())
                .phoneNumber(account.getPhoneNumber())
                .password(account.getPassword())
                .personalSale(account.getPersonalSale())
                .role(account.getRole().name())
                .accountState(account.getAccountState().name())
                .addresses(account.getAddresses().stream().map(Address::getId).toList())
                .cards(account.getCards().stream().map(Card::getId).toList())
                .orders(account.getOrders().stream().map(Orders::getId).toList())
                .build();
    }

    public static List<AccountDto> from(List<Account> accounts) {
        return accounts.stream().map(AccountDto::from).toList();
    }
}