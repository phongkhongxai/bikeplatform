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
@Table(name = "bike_types")
public class BikeType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="biketype_id")
    private Long id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String transmission;

    @Column(nullable = false)
    private String cylinder_capacity;

    @Column(nullable = false)
    private String oil_capacity;

    @Column(nullable = false)
    private boolean isDelete;

    @OneToMany(mappedBy = "bikeType", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Vehicle> vehicles;

    @ManyToMany(mappedBy = "bikeTypes", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<OilProduct> oilProducts;


}
