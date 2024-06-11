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
@Table(name = "bike_points")
public class BikePoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bikepoint_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private boolean isDelete = false;

    @OneToOne(mappedBy = "bikePoint", cascade = CascadeType.ALL, orphanRemoval = true)
    private Wallet wallet;

    @OneToMany(mappedBy = "bikePoint", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WithdrawBikePoint> withdrawBikePoints;

    @OneToMany(mappedBy = "bikePoint", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderUsing> orderUsings ;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany
    @JoinTable(
            name = "bike_point_brand",
            joinColumns = @JoinColumn(name = "bikepoint_id"),
            inverseJoinColumns = @JoinColumn(name = "brand_id")
    )
    private Set<Brand> brands;
}
