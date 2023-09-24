package ru.skillbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.domain.Shipment;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
}
