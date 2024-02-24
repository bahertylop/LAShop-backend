package org.lashop.newback.services;

import org.lashop.newback.dto.AccountDto;

import java.util.List;

public interface UsersService {

    List<AccountDto> getAllAccounts();

    void takeAccountDeleted(String email);

    void takeAccountBanned(String email);

    void takeAccountConfirmed(String email);

    void takeRoleUser(String email);

    void takeRoleAdmin(String email);


}
