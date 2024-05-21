package com.swdgr6.bikeplatform.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "oil_product_package")
public class OilProductPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="oilpack_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String volume;

    @Column(nullable = false)
    private String description;

    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    @OneToMany(mappedBy = "oilProductPackage", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Order> orders;

    @ManyToOne
    @JoinColumn(name = "oil_id", nullable = false)
    private OilProduct oilProduct;




}
