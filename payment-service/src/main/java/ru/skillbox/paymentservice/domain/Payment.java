package ru.skillbox.paymentservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "creation_time")
    private LocalDateTime creationTime;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    public Payment(Double amount, Long orderId, LocalDateTime creationTime, PaymentStatus status) {
        this.amount = amount;
        this.orderId = orderId;
        this.creationTime = creationTime;
        this.status = status;
    }
}
