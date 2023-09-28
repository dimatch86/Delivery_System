package ru.skillbox.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "shipment")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, name = "departure_address")
    private String departureAddress;

    @Column(nullable = false, name = "destination_address")
    private String destinationAddress;

    @CreationTimestamp
    @Column(name = "creation_time")
    private LocalDateTime creationTime;

    @Column(nullable = false, name = "order_id")
    private Long orderId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;

    public Shipment(String departureAddress, String destinationAddress, LocalDateTime creationTime, Long orderId, ShipmentStatus status) {
        this.departureAddress = departureAddress;
        this.destinationAddress = destinationAddress;
        this.creationTime = creationTime;
        this.orderId = orderId;
        this.status = status;
    }

    public Shipment(String departureAddress, String destinationAddress) {
        this.departureAddress = departureAddress;
        this.destinationAddress = destinationAddress;
    }
}
