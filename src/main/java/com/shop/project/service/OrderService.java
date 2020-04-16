package com.shop.project.service;

import com.shop.project.dto.CreateOrderDTO;
import com.shop.project.entity.Order;
import com.shop.project.entity.Product;
import com.shop.project.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final ProductService productService;
    private final OrderRepository orderRepository;


    public OrderService(OrderRepository orderRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    public Optional<Order> addOrder(CreateOrderDTO createOrderDTO) {

        List<Product> orderProducts = createOrderDTO.getProducts().stream()
                .filter(product -> productService.buyProduct(product).isPresent())
                .collect(Collectors.toList());

        if (!orderProducts.isEmpty()) {
            Order order = Order.builder().price(getTotalPrice(orderProducts)).products(orderProducts).build();
            Order completedOrder = orderRepository.save(order);
            return Optional.of(completedOrder);
        }
        return Optional.empty();
    }

    public Optional<Order> findById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    private BigDecimal getTotalPrice(List<Product> products){
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Product product : products){
            int amount = product.getAmount();
            totalPrice = totalPrice.add(product.getPrice().multiply(new BigDecimal(amount)));
        }
        return totalPrice;
    }
}
