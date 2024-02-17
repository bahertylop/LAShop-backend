package org.lashop.newback.services.impl;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.lashop.newback.dto.AccountDto;
import org.lashop.newback.models.Account;
import org.lashop.newback.repositories.AccountRepository;
import org.lashop.newback.services.AccountService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void signUp(AccountDto accountDto) {
        Optional<Account> position = accountRepository.findByEmail(accountDto.getEmail());

        if (position.isPresent()) throw new IllegalArgumentException("DUBLICATE_EMAIL");

        Account account = Account.builder()
                .firstName(accountDto.getFirstName())
                .lastName(accountDto.getLastName())
                .email(accountDto.getEmail())
                .phoneNumber(accountDto.getPhoneNumber())
                .password(passwordEncoder.encode(accountDto.getPassword()))     // добавить енкодер
                .role(Account.Role.valueOf(accountDto.getRole()))
                .accountState(Account.State.valueOf(accountDto.getAccountState()))
                .personalSale(accountDto.getPersonalSale())
                .build();
        accountRepository.save(account);
    }

    @Override
    public AccountDto getAccount(long accountId) {
        Optional<Account> position = accountRepository.findById(accountId);
        return position.map(AccountDto::from).orElseThrow(() -> new RuntimeException("ACCOUNT_NOT_FOUND"));
    }

    @Override
    public AccountDto validEmailAndPassword(String email, String password) {
//        Optional<Account> account = accountRepository.findByEmail(email);
//
//        if (account.isPresent() && passwordEncoder.matches(password, account.get().getPassword())) {
//            return AccountDto.from(account.get());
//        }
//        throw new RuntimeException("PASSWORD_NOT_VALID");

        return AccountDto.from(accountRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElseThrow(() -> new RuntimeException("PASSWORD_NOT_VALID")));
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
