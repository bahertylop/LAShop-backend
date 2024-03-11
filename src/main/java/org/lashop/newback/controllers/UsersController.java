package org.lashop.newback.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.lashop.newback.dto.AccountDto;
import org.lashop.newback.dto.requests.AccountEmailRequest;
import org.lashop.newback.services.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @GetMapping("api/adm/users/all")
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        List<AccountDto> accountDtos = usersService.getAllAccounts();
        return new ResponseEntity<>(accountDtos, HttpStatus.OK);
    }

    @PostMapping("api/adm/users/deleteAccount")
    public ResponseEntity<?> makeAccountDeleted(@RequestBody AccountEmailRequest accountEmailRequest) {
        if (accountEmailRequest == null || accountEmailRequest.getEmail() == null) {
            return ResponseEntity.badRequest().body("request has empty body");
        }

        try {
            usersService.takeAccountDeleted(accountEmailRequest.getEmail());
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok("state changed to deleted");
    }

    @PostMapping("api/adm/users/banAccount")
    public ResponseEntity<?> makeAccountBanned(@RequestBody AccountEmailRequest accountEmailRequest) {
        if (accountEmailRequest == null || accountEmailRequest.getEmail() == null) {
            return ResponseEntity.badRequest().body("request has empty body");
        }

        try {
            usersService.takeAccountBanned(accountEmailRequest.getEmail());
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok("state changed to banned");
    }

    @PostMapping("api/adm/users/confirmAccount")
    public ResponseEntity<?> makeAccountConfirmed(@RequestBody AccountEmailRequest accountEmailRequest) {
        if (accountEmailRequest == null || accountEmailRequest.getEmail() == null) {
            return ResponseEntity.badRequest().body("request has empty body");
        }

        try {
            usersService.takeAccountConfirmed(accountEmailRequest.getEmail());
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok("state changed to confirmed");
    }

    @PostMapping("api/adm/users/takeAdmin")
    public ResponseEntity<?> changeToAdmin(@RequestBody AccountEmailRequest accountEmailRequest) {
        if (accountEmailRequest == null || accountEmailRequest.getEmail() == null) {
            return ResponseEntity.badRequest().body("request has empty body");
        }

        try {
            usersService.takeRoleAdmin(accountEmailRequest.getEmail());
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok("role changed to admin");
    }

    @PostMapping("api/adm/users/takeUser")
    public ResponseEntity<?> changeToUser(@RequestBody AccountEmailRequest accountEmailRequest) {
        if (accountEmailRequest == null || accountEmailRequest.getEmail() == null) {
            return ResponseEntity.badRequest().body("request has empty body");
        }

        try {
            usersService.takeRoleUser(accountEmailRequest.getEmail());
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok("role changed to user");
    }
}
