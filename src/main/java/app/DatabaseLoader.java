package app;

import app.model.Product;
import app.model.Role;
import app.model.User;
import app.repositories.ProductRepository;
import app.repositories.RoleRepository;
import app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final ProductRepository products;
    private final UserRepository users;
    private final RoleRepository roles;

    @Autowired
    public DatabaseLoader(ProductRepository products, UserRepository users, RoleRepository roles) {
        this.products = products;
        this.users = users;
        this.roles = roles;
    }

    @Override
    public void run(String... strings) throws Exception {
        Product wires = this.products.save(new Product("Wires", 520, 415, 0));
        Product oscilograf = this.products.save(new Product("Осциллограф цифровой", 26500, 2, 0));
        Product searcher = this.products.save(new Product("Искатель скрытых коммуникаций", 9500, 10, 0));

        Role role_admin = this.roles.save(new Role("ADMIN"));
        Role role_delivery = this.roles.save(new Role("DELIVERY_DEPT"));
        Role role_storefront = this.roles.save(new Role("STOREFRONT"));
        Role role_money = this.roles.save(new Role("PAYMENT_SYSTEM"));

        User admin = this.users.save(new User("admin", "123", role_admin));
        User delivery = this.users.save(new User("delivery", "123", role_delivery));
        User store = this.users.save(new User("store", "123", role_storefront));
        User bank = this.users.save(new User("bank", "123", role_money));
    }
}
