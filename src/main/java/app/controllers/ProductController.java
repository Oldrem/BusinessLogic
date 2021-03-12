package app.controllers;

import app.model.Product;
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
public class ProductController {
    private ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/products")
    Collection<Product> products() {
        return (Collection<Product>) productRepository.findAll();
    }

    @GetMapping("/product/{id}")
    ResponseEntity<?> getProduct(@PathVariable Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/product")
    ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) throws URISyntaxException
    {
        Product result = productRepository.save(product);
        return ResponseEntity.created(new URI("/product/" + result.getId()))
                .body(result);
    }

    @PutMapping("/product/{id}")
    ResponseEntity<Product> updateProduct(@Valid @RequestBody Product product) {
        Product result = productRepository.save(product);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
