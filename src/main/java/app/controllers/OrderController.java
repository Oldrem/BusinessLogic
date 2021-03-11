package app.controllers;

import app.model.Order;
import app.model.Product;
import app.repositories.OrderRepository;
import app.services.OrderService;
import app.repositories.ProductRepository;
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
    private OrderService orderService;
    private ProductRepository productRepository;

    public OrderController(OrderRepository orderRepository,
                           OrderService orderService,
                           ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.productRepository = productRepository;
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
        Optional<Product> optionalProduct = productRepository.findById(order.getProduct().getId());
        if (optionalProduct.isPresent()){
            Product product = optionalProduct.get();
            product.setBookedAmount(product.getBookedAmount()+1);
        }
        else {
            return ResponseEntity.badRequest().body(result);
        }
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


    @PutMapping("/order/{id}/payed")
    ResponseEntity<Order> updatePayedStatus(@PathVariable Long id, @RequestParam Boolean value) {
        Optional<Order> order = orderRepository.findById(id);
        if (!order.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (!value) return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

        orderService.confirmOrderPayment(order.get());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
