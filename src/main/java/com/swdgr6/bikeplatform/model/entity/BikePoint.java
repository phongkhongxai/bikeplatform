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

    @Column(nullable = false, unique = true)
    private String phone;

    @OneToOne(mappedBy = "bikePoint", cascade = CascadeType.ALL, orphanRemoval = true)
    private Wallet wallet;

    @OneToMany(mappedBy = "bikePoint", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WithdrawBikePoint> withdrawBikePoints;

    @OneToMany(mappedBy = "bikePoint", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderUsing> orderUsings ;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}
