package org.lashop.newback.repositories;

import jakarta.transaction.Transactional;
import org.lashop.newback.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);

    @Transactional
    default void changePassword(long accountId, String newPassword) {
        Account account = findById(accountId).orElse(null);
        if (account != null) {
            account.setPassword(newPassword);
            save(account);
        }
    }

    @Transactional
    default void changeEmail(long accountId, String newEmail) {
        if (findByEmail(newEmail).isEmpty()) {
            Account account = findById(accountId).orElse(null);
            if (account != null) {
                account.setEmail(newEmail);
                save(account);
            }
        }
    }

    @Transactional
    default void changePhone(long accountId, String newPhone) {
        Account account = findById(accountId).orElse(null);
        if (account != null) {
            account.setPhoneNumber(newPhone);
            save(account);
        }
    }

    @Transactional
    default void changePersonalSale(long accountId, int newSale) {
        Account account = findById(accountId).orElse(null);
        if (account != null) {
            account.setPersonalSale(newSale);
            save(account);
        }
    }

    @Modifying
    @Transactional
    @Query("UPDATE Account p SET p.accountState = ?2 where p.id = ?1")
    void changeAccountState(long adminId, Account.State state);


    @Modifying
    @Transactional
    @Query("UPDATE Account p SET p.role = ?2 where p.id = ?1")
    void changeAccountRole(long accountId, Account.Role role);


}
