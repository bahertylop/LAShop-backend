package org.lashop.newback.controllers;


import lombok.RequiredArgsConstructor;
import org.lashop.newback.dto.AccountDto;
import org.lashop.newback.dto.requests.SignInRequest;
import org.lashop.newback.dto.requests.SignUpRequest;
import org.lashop.newback.models.Account;
import org.lashop.newback.services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;


// поработать над валидацией вводимых данных,
// поработать с возможной дубликацией номера телефона
@Controller
@RequiredArgsConstructor
public class AuthorizationController {

    private final AccountService accountService;

    @PostMapping("/api/signIn")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        try {
            AccountDto accountDto = accountService.validEmailAndPassword(signInRequest.getEmail(), signInRequest.getPassword());
            return ResponseEntity.ok("user signedIn");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

    }

    @PostMapping("/api/signUp")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        AccountDto accountDto = mapSignUpRequestToAccountDto(signUpRequest);
        accountService.signUp(accountDto);
    }

    private AccountDto mapSignUpRequestToAccountDto(SignUpRequest signUpRequest) {
        return AccountDto.builder()
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .email(signUpRequest.getEmail())
                .phoneNumber(signUpRequest.getPhoneNumber())
                .password(signUpRequest.getPassword())
                .role(Account.Role.USER.name())
                .accountState(Account.State.NOT_CONFIRMED.name())
                .personalSale(0)
                .build();
    }
}
