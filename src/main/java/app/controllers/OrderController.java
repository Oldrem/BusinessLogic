package app.controllers;

import app.controllers.helpers.OrderRequestBody;
import app.model.Order;
import app.repositories.OrderRepository;
import app.services.OrderService;
import app.repositories.ProductRepository;
import app.services.ProductBookingException;
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

    public OrderController(OrderRepository orderRepository,
                           OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
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
    ResponseEntity<Order> createOrder(@Valid @RequestBody OrderRequestBody rawOrder) throws URISyntaxException
    {
        System.out.println(rawOrder.getProductId());
        try
        {
            Order result = orderService.startAddOrderTransaction(rawOrder);
            return ResponseEntity.created(new URI("/order/" + result.getOrderId()))
                    .body(result);
        }
        catch (ProductBookingException e)
        {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
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
    ResponseEntity<Order> updatePayedStatus(@PathVariable Long id, @RequestBody Boolean value) {
        Optional<Order> order = orderRepository.findById(id);
        if (!order.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (!value) return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

        orderService.startOnOrderPaidTransaction(order.get());

        return ResponseEntity.ok().body(order.get());
    }

    @PutMapping("/order/{id}/confirmed")
    ResponseEntity<Order> updateConfirmationStatus(@PathVariable Long id, @RequestBody Boolean value) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (!optionalOrder.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (!value) return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        Order order = optionalOrder.get();
        order.setConfirmed(true);
        order.setConfirmationDate(LocalDateTime.now());
        orderRepository.save(order);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/order/{id}/received")
    ResponseEntity<Order> updateReceivedStatus(@PathVariable Long id, @RequestParam Boolean value) {
        Optional<Order> order = orderRepository.findById(id);
        if (!order.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (!value) return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

        order.get().setReceived(true);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
