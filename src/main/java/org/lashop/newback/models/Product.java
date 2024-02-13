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
// фактические существующие пары
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // артикул товара по сути
    @ManyToOne
    @JoinColumn(name = "shoe_type_id")
    private ShoeType shoeType;

    private double size;
}
