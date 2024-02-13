package org.lashop.newback.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

    public enum Role {
        USER, ADMIN
    }

    public enum State {
        NOT_CONFIRMED, CONFIRMED, DELETED, BANNED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    @Column(name = "mobile_number")
    private String phoneNumber;

    private String password;

    @Column(name = "personal_sale")
    private int personalSale;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "state")
    private State accountState;

    @OneToMany(mappedBy = "accountId")
    private List<Address> addresses;

    @OneToMany(mappedBy = "accountId")
    private List<Card> cards;
}
