package app.controllers;

import app.model.Order;
import app.repositories.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class OrderController {
    private OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/orders")
    Collection<Order> orders() {
        return (Collection<Order>) orderRepository.findAll();
    }

    @GetMapping("/order/{id}")
    ResponseEntity<?> getOrder(@PathVariable Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/order")
    ResponseEntity<Order> createOrder(@Valid @RequestBody Order order) throws URISyntaxException {
        Order result = orderRepository.save(order);
        return ResponseEntity.created(new URI("/order/" + result.getOrderId()))
                .body(result);
    }

    @PutMapping("/order/{id}")
    ResponseEntity<Order> updateOrder(@Valid @RequestBody Order order) {
        Order result = orderRepository.save(order);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/order/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        orderRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
