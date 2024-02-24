package org.lashop.newback.services.impl;

import lombok.RequiredArgsConstructor;
import org.lashop.newback.dto.AccountDto;
import org.lashop.newback.models.Account;
import org.lashop.newback.repositories.AccountRepository;
import org.lashop.newback.services.UsersService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final AccountRepository accountRepository;
    @Override
    public List<AccountDto> getAllAccounts() {
        return AccountDto.from(accountRepository.findAll());
    }

    @Override
    public void takeAccountDeleted(long accountId) {
        Optional<Account> account = accountRepository.findById(accountId);

        if (account.isPresent()) {
            accountRepository.makeStateDeleted(accountId);
        } else {
            throw new RuntimeException("account not found");
        }
    }

    @Override
    public void takeAccountBanned(long accountId) {
        Optional<Account> account = accountRepository.findById(accountId);

        if (account.isPresent()) {
            accountRepository.makeStateBanned(accountId);
        } else {
            throw new RuntimeException("account not found");
        }
    }

    @Override
    public void takeAccountConfirmed(long accountId) {
        Optional<Account> account = accountRepository.findById(accountId);

        if (account.isPresent()) {
            accountRepository.makeStateConfirmed(accountId);
        } else {
            throw new RuntimeException("account not found");
        }
    }

    @Override
    public void takeRoleUser(long accountId) {
        Optional<Account> account = accountRepository.findById(accountId);

        if (account.isPresent()) {
            accountRepository.makeRoleUser(accountId);
        } else {
            throw new RuntimeException("account not found");
        }
    }

    @Override
    public void takeRoleAdmin(long accountId) {
        Optional<Account> account = accountRepository.findById(accountId);

        if (account.isPresent()) {
            accountRepository.makeRoleAdmin(accountId);
        } else {
            throw new RuntimeException("account not found");
        }
    }
}
