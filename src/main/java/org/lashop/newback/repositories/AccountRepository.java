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
    @Query("UPDATE Account p SET p.accountState = Account.State.DELETED where p.id = ?1")
    void makeStateDeleted(long adminId);

    @Modifying
    @Transactional
    @Query("UPDATE Account p SET p.accountState = Account.State.BANNED where p.id = ?1")
    void makeStateBanned(long adminId);

    @Modifying
    @Transactional
    @Query("UPDATE Account p SET p.accountState = Account.State.CONFIRMED where p.id = ?1")
    void makeStateConfirmed(long adminId);

    @Modifying
    @Transactional
    @Query("UPDATE Account p SET p.role = Account.Role.ADMIN where p.id = ?1")
    void makeRoleAdmin(long accountId);

    @Modifying
    @Transactional
    @Query("UPDATE Account p SET p.role = Account.Role.USER where p.id = ?1")
    void makeRoleUser(long accountId);

}
