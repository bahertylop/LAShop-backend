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
    public void takeAccountDeleted(String email) {
        Optional<Account> account = accountRepository.findByEmail(email);

        if (account.isPresent()) {
            accountRepository.changeAccountState(account.get().getId(), Account.State.DELETED);
        } else {
            throw new RuntimeException("account not found");
        }
    }

    @Override
    public void takeAccountBanned(String email) {
        Optional<Account> account = accountRepository.findByEmail(email);

        if (account.isPresent()) {
            accountRepository.changeAccountState(account.get().getId(), Account.State.BANNED);
        } else {
            throw new RuntimeException("account not found");
        }
    }

    @Override
    public void takeAccountConfirmed(String email) {
        Optional<Account> account = accountRepository.findByEmail(email);

        if (account.isPresent()) {
            accountRepository.changeAccountState(account.get().getId(), Account.State.CONFIRMED);
        } else {
            throw new RuntimeException("account not found");
        }
    }

    @Override
    public void takeRoleUser(String email) {
        Optional<Account> account = accountRepository.findByEmail(email);

        if (account.isPresent()) {
            accountRepository.changeAccountRole(account.get().getId(), Account.Role.USER);
        } else {
            throw new RuntimeException("account not found");
        }
    }

    @Override
    public void takeRoleAdmin(String email) {
        Optional<Account> account = accountRepository.findByEmail(email);

        if (account.isPresent()) {
            accountRepository.changeAccountRole(account.get().getId(), Account.Role.ADMIN);
        } else {
            throw new RuntimeException("account not found");
        }
    }
}
