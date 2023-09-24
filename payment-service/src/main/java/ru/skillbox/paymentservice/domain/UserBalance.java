package ru.skillbox.paymentservice.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user_balance")
@Data
@NoArgsConstructor
public class UserBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, name = "user_name", unique = true)
    private String userName;

    @Column(nullable = false, name = "balance")
    private Double balance;

    public UserBalance(String userName, Double balance) {
        this.userName = userName;
        this.balance = balance;
    }
}
