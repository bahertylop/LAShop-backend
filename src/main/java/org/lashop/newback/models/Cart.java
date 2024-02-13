package org.lashop.newback.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "shoe_type_id")
    private ShoeType shoeType;

    @Min(1)
    private double size;

    @Min(1)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
