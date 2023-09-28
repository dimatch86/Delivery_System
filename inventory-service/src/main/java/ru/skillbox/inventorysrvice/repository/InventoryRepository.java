package ru.skillbox.inventorysrvice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.inventorysrvice.domain.Product;


@Repository
public interface InventoryRepository extends JpaRepository<Product, Long> {

    Product findProductByName(String name);

}
