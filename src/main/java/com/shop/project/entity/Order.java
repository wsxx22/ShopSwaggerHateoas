package com.shop.project.entity;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Order extends RepresentationModel<Order> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private final LocalDateTime orderDate = LocalDateTime.now();
    private BigDecimal price;

    @Getter(AccessLevel.NONE)
    @ManyToMany
    @JoinTable(name = "order_products", joinColumns = @JoinColumn(name = "id_order"),
            inverseJoinColumns = @JoinColumn(name = "id_product"))
    private List<Product> products;

    public List<Product> getProducts(){
        return new ArrayList<>(products);
    }
}