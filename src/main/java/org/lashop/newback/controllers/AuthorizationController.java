package org.lashop.newback.controllers;


import lombok.RequiredArgsConstructor;
import org.lashop.newback.config.security.JwtCore;
import org.lashop.newback.config.security.Token;
import org.lashop.newback.dto.AccountDto;
import org.lashop.newback.dto.requests.SignInRequest;
import org.lashop.newback.dto.requests.SignUpRequest;
import org.lashop.newback.models.Account;
import org.lashop.newback.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


// поработать над валидацией вводимых данных,
// поработать с возможной дубликацией номера телефона
@RestController
@RequiredArgsConstructor
public class AuthorizationController {

    private final AccountService accountService;
    private final AuthenticationManager authenticationManager;
    private JwtCore jwtCore;

    @Autowired
    public void jwtCoreSet(JwtCore jwtCore) {
        this.jwtCore = jwtCore;
    }


    @PostMapping("/api/signIn")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInRequest signInRequest) {

        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("not signedIn", HttpStatus.UNAUTHORIZED);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String jwt = jwtCore.generateToken(authentication);
        Token jwt = new Token(jwtCore.generateToken(authentication));
        return ResponseEntity.ok().body(jwt);
    }

    @PostMapping("/api/signUp")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (accountService.checkEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body("email used");
        }

        AccountDto accountDto = mapSignUpRequestToAccountDto(signUpRequest);
        accountService.signUp(accountDto);
        return ResponseEntity.ok("user signedUp");
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
