package org.lashop.newback.services;

import org.lashop.newback.dto.AccountDto;

import java.util.Optional;

public interface AccountService {

    boolean signUp(AccountDto accountDto);

    AccountDto getAccount(long accountId);

    AccountDto validEmailAndPassword(String email, String password);

    void changePassword(long accountId, String password);

    void changeEmail(long accountId, String email);

    void changePersonalSale(long accountId, int sale);

    void changePhone(long accountId, String phone);

    void deleteAccount(long accountId);
}
