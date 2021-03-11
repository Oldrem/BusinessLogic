package app;

import app.model.Product;
import app.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import org.springframework.stereotype.Component;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final ProductRepository products;

    @Autowired
    public DatabaseLoader(ProductRepository products) {
        this.products = products;
    }

    @Override
    public void run(String... strings) throws Exception {
        Product wires = this.products.save(new Product("Wires", 520, 415, 0));
        Product oscilograf = this.products.save(new Product("Осциллограф цифровой", 26500, 2, 0));
        Product searcher = this.products.save(new Product("Искатель скрытых коммуникаций", 9500, 10, 0));
    }
}
