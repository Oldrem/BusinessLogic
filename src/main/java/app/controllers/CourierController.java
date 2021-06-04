package app.controllers;

import app.model.Courier;
import app.repositories.CourierRepository;
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
public class CourierController
{
    private CourierRepository courierRepository;

    public CourierController(CourierRepository courierRepository) {
        this.courierRepository = courierRepository;
    }

    @GetMapping("/couriers")
    Collection<Courier> couriers() {
        return (Collection<Courier>) courierRepository.findAll();
    }

    @GetMapping("/courier/{id}")
    ResponseEntity<?> getCourier(@PathVariable Long id) {
        Optional<Courier> courier = courierRepository.findById(id);
        return courier.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/courier")
    ResponseEntity<Courier> createCourier(@Valid @RequestBody Courier courier) throws URISyntaxException
    {
        Courier result = courierRepository.save(courier);
        return ResponseEntity.created(new URI("/courier/" + result.getCourierId()))
                .body(result);
    }

    @PutMapping("/courier/{id}")
    ResponseEntity<Courier> updateCourier(@PathVariable Long id, @Valid @RequestBody Courier courier)
    {
        if (!courierRepository.existsById(id))
            throw new RuntimeException("Invalid CourierId");
        courier.setCourierId(id);
        Courier result = courierRepository.save(courier);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/courier/{id}")
    public ResponseEntity<?> deleteCourier(@PathVariable Long id) {
        courierRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
