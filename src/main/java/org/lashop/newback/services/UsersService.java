package org.lashop.newback.services;

import org.lashop.newback.dto.AccountDto;

import java.util.List;

public interface UsersService {

    List<AccountDto> getAllAccounts();

    void takeAccountDeleted(long accountId);

    void takeAccountBanned(long accountId);

    void takeAccountConfirmed(long accountId);

    void takeRoleUser(long accountId);

    void takeRoleAdmin(long accountId);


}
