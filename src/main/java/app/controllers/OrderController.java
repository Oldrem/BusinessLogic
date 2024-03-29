package app.controllers;

import app.model.Order;
import app.model.OrderStatus;
import app.repositories.OrderRepository;
import app.requests.OrderRequestBody;
import app.services.EmailService;
import app.services.OrderService;
import app.services.ProductBookingException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class OrderController {
    private OrderRepository orderRepository;
    private OrderService orderService;
    private EmailService emailService;

    public OrderController(OrderRepository orderRepository,
                           OrderService orderService, EmailService emailService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.emailService = emailService;

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
    ResponseEntity<?> createOrder(@Valid @RequestBody OrderRequestBody rawOrder) throws URISyntaxException
    {
        try
        {
            Order result = orderService.addOrder(rawOrder);
            emailService.sendConfirmation(result);
            return ResponseEntity.created(new URI("/order/" + result.getId()))
                    .body(result);
        }
        catch (ProductBookingException e)
        {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/order/{id}")
    ResponseEntity<Order> updateOrder(@Valid @RequestBody Order order) {
        Order result;
        try
        {
            result = orderRepository.save(order);
        }
        catch (EmptyResultDataAccessException e)
        {

            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/order/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        try
        {
            orderRepository.deleteById(id);
        }
        catch (EmptyResultDataAccessException e)
        {

            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }


    @PutMapping("/order/{id}/pay")
    ResponseEntity<?> updatePayedStatus(@PathVariable Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (!optionalOrder.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Order order = optionalOrder.get();
        orderService.payForOrder(order);
        emailService.sendReceipt(order);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/order/{id}/confirm")
    ResponseEntity<Order> updateConfirmationStatus(@PathVariable Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (!optionalOrder.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Order order = optionalOrder.get();
        orderService.confirmOrder(order);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/order/{id}/receive")
    ResponseEntity<Order> updateReceivedStatus(@PathVariable Long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (!order.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        order.get().setStatus(OrderStatus.RECEIVED);
        orderRepository.save(order.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
