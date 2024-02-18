package org.lashop.newback.config.security;

import org.lashop.newback.models.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class AccountUserDetails implements UserDetails {

    private final Account account;

    public AccountUserDetails(Account account) {
        this.account = account;
    }

    // достать возможности статуса аккаунта
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = account.getRole().name();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);

        return Collections.singleton(authority);
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }


    @Override
    public String getUsername() {
        return account.getEmail();
    }

    // не просрочен ли аккаунт
    @Override
    public boolean isAccountNonExpired() {
        Account.State state = account.getAccountState();
        return state != Account.State.BANNED && state != Account.State.DELETED && state != null;
    }

    // не заблокирован ли аккаунт
    @Override
    public boolean isAccountNonLocked() {
        return !account.getAccountState().equals(Account.State.BANNED);
    }

    // не просрочен ли пароль и мыло
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //доступен или нет аккаунт
    @Override
    public boolean isEnabled() {
        return account.getAccountState().equals(Account.State.CONFIRMED);
    }

    public Long getId() {
        return account.getId();
    }
}
