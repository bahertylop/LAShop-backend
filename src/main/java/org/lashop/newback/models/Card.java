package org.lashop.newback.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "pay_system")
    private String paySystem;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "card_date")
    private String cardDate;

    @Column(name = "card_cvv")
    private String cardCVV;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}