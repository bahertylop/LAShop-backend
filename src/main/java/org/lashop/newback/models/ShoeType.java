package org.lashop.newback.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ShoeType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_type_id")
    private long id;

    private String brand;

    private String model;

    private String color;

    @ElementCollection
    private List<String> photos;

    private String description;

    private int price;

    @Column(name = "in_stock")
    private boolean inStock;
}
