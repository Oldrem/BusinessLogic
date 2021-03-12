package app.controllers;

import app.model.DeliveryRequest;
import app.repositories.DeliveryRequestRepository;
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
public class DeliveryRequestController
{
    private DeliveryRequestRepository deliveryRequestRepository;

    public DeliveryRequestController(DeliveryRequestRepository deliveryRequestRepository) {
        this.deliveryRequestRepository = deliveryRequestRepository;
    }

    @GetMapping("/deliveryRequests")
    Collection<DeliveryRequest> deliveryRequests() {
        return (Collection<DeliveryRequest>) deliveryRequestRepository.findAll();
    }

    @GetMapping("/deliveryRequest/{id}")
    ResponseEntity<?> getDeliveryRequest(@PathVariable Long id) {
        Optional<DeliveryRequest> deliveryRequest = deliveryRequestRepository.findById(id);
        return deliveryRequest.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/deliveryRequest")
    ResponseEntity<DeliveryRequest> createDeliveryRequest(@Valid @RequestBody DeliveryRequest deliveryRequest) throws URISyntaxException
    {
        DeliveryRequest result = deliveryRequestRepository.save(deliveryRequest);
        return ResponseEntity.created(new URI("/deliveryRequest/" + result.getDeliveryRequestId()))
                .body(result);
    }

    @PutMapping("/deliveryRequest/{id}")
    ResponseEntity<DeliveryRequest> updateDeliveryRequest(@Valid @RequestBody DeliveryRequest deliveryRequest) {
        DeliveryRequest result = deliveryRequestRepository.save(deliveryRequest);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/deliveryRequest/{id}")
    public ResponseEntity<?> deleteDeliveryRequest(@PathVariable Long id) {
        deliveryRequestRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
