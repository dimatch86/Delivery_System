package ru.skillbox.paymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.paymentservice.domain.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
