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
@Table(name = "oil_products")
public class OilProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="oil_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private String techStand; // cong nghe tieu chuan

    @Column(nullable = false)
    private String oilType;

    @Column
    private String imageUrl;

    @Column(nullable = false)
    private boolean isDelete = false;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @OneToMany(mappedBy = "oilProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OilProductPackage> oilProductPackages;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "BikeOil",
            joinColumns = {@JoinColumn(name = "oil_id", referencedColumnName = "oil_id")},
            inverseJoinColumns = {@JoinColumn(name = "biketype_id", referencedColumnName = "biketype_id")}
    )
    private Set<BikeType> bikeTypes;

}
