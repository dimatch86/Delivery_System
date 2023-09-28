package ru.skillbox.orderservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.skillbox.dto.OrderStatus;
import ru.skillbox.dto.ProductDetailsDto;
import ru.skillbox.dto.ServiceName;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "departure_address")
    private String departureAddress;

    @Column(name = "destination_address")
    private String destinationAddress;

    @Column(name = "cost")
    private Double cost;

    @CreationTimestamp
    @Column(name = "creation_time")
    private LocalDateTime creationTime;

    @UpdateTimestamp
    @Column(name = "modified_time")
    private LocalDateTime modifiedTime;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "user_name")
    private String userName;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderStatusHistory> orderStatusHistory = new ArrayList<>();

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ProductDetails> productDetails = new ArrayList<>();

    public Order(
            String departureAddress,
            String destinationAddress,
            String description,
            OrderStatus status
    ) {
        this.departureAddress = departureAddress;
        this.destinationAddress = destinationAddress;
        this.description = description;
        this.status = status;
    }

    public Order(String description, String departureAddress, String destinationAddress, Double cost, LocalDateTime creationTime, LocalDateTime modifiedTime, OrderStatus status, String userName) {
        this.description = description;
        this.departureAddress = departureAddress;
        this.destinationAddress = destinationAddress;
        this.cost = cost;
        this.creationTime = creationTime;
        this.modifiedTime = modifiedTime;
        this.status = status;
        this.userName = userName;
    }

    public void addStatusHistory(OrderStatus status, ServiceName serviceName, String comment) {
        getOrderStatusHistory().add(new OrderStatusHistory(null, status, serviceName, comment, this));
    }

    public void addProductDetails(List<ProductDetailsDto> productDetailsDtos) {

        productDetailsDtos.forEach(productDetailsDto ->
                getProductDetails().add(new ProductDetails(null,
                productDetailsDto.getName(), productDetailsDto.getCost(),
                productDetailsDto.getAmount(), this)));

    }
}
