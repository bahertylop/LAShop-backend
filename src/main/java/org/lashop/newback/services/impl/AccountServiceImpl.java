package org.lashop.newback.services.impl;


import lombok.RequiredArgsConstructor;
import org.lashop.newback.dto.AccountDto;
import org.lashop.newback.models.Account;
import org.lashop.newback.repositories.AccountRepository;
import org.lashop.newback.services.AccountService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;


    @Override
    public boolean signUp(AccountDto accountDto) {
        Optional<Account> position = accountRepository.findByEmail(accountDto.getEmail());

        if (position.isPresent()) return false;

        Account account = Account.builder()
                .firstName(accountDto.getFirstName())
                .lastName(accountDto.getLastName())
                .email(accountDto.getEmail())
                .phoneNumber(accountDto.getPhoneNumber())
                .password(accountDto.getPassword())     // добавить енкодер
                .role(Account.Role.valueOf(accountDto.getRole()))
                .accountState(Account.State.valueOf(accountDto.getAccountState()))
                .personalSale(accountDto.getPersonalSale())
                .build();
        accountRepository.save(account);

        return true;
    }

    @Override
    public AccountDto getAccount(long accountId) {
        Optional<Account> position = accountRepository.findById(accountId);
        return position.map(AccountDto::from).orElse(null);
    }

    @Override
    public AccountDto validEmailAndPassword(String email, String password) {
        Optional<Account> account = accountRepository.findByEmail(email);

        if (account.isPresent() && account.get().getPassword().equals(password)) {
            return AccountDto.from(account.get());
        }
        return null;
    }


    @Override
    public void changePassword(long accountId, String password) {
        accountRepository.changePassword(accountId, password);
    }

    @Override
    public void changeEmail(long accountId, String email) {
        accountRepository.changeEmail(accountId, email);
    }

    @Override
    public void changePersonalSale(long accountId, int sale) {
        accountRepository.changePersonalSale(accountId, sale);
    }

    @Override
    public void changePhone(long accountId, String phone) {
        accountRepository.changePhone(accountId, phone);
    }

    @Override
    public void deleteAccount(long accountId) {
        accountRepository.deleteById(accountId);
    }
}
