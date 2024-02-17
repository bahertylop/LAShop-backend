package org.lashop.newback.controllers;


import lombok.RequiredArgsConstructor;
import org.lashop.newback.dto.AccountDto;
import org.lashop.newback.dto.requests.SignUpRequest;
import org.lashop.newback.models.Account;
import org.lashop.newback.services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AuthorizationController {

    private final AccountService accountService;

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
