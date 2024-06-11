package com.swdgr6.bikeplatform.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "order_usings")
public class OrderUsing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="orderusing_id")
    private Long id;

    @Column(nullable = false)
    private double price;

    @Column
    private int rating;

    @Column
    private String feedback;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateUsing;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateUpdateUsing;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "bikepoint_id", nullable = false)
    private BikePoint bikePoint;

    @OneToMany(mappedBy = "orderUsing", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Transaction> transactions;

    private boolean isDelete = false;
}
