package com.shop.project.controller;

import com.shop.project.dto.CreateOrderDTO;
import com.shop.project.entity.Order;
import com.shop.project.entity.Product;
import com.shop.project.service.OrderService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> addOrder(@RequestBody CreateOrderDTO createOrderDTO) {

        Optional<Order> orderOptional = orderService.addOrder(createOrderDTO);
        return orderOptional.map(order -> {
            addLinksToOrder(order);
            return ResponseEntity.ok(order);
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable("id") Long id) {
        Optional<Order> orderById = orderService.findById(id);
        return orderById.map(order -> {
            addLinksToOrder(order);
            return ResponseEntity.ok(order);
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public ResponseEntity<CollectionModel<Order>> getAllOrders() {
        List<Order> orders = orderService.getOrders();
        orders.forEach(this::addLinksToOrder);
        Link link = linkTo(OrderController.class).withSelfRel();
        return ResponseEntity.ok(new CollectionModel<>(orders, link));
    }

    private void addLinksToOrder(Order order) {
        order.getProducts().forEach(product -> product.addIf(!product.hasLinks(), () -> linkTo(ProductController.class).slash(product.getId()).withSelfRel()));
        order.add(linkTo(OrderController.class).slash(order.getId()).withSelfRel());
    }
}
