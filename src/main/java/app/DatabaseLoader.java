package app;

import app.model.Product;
import app.model.security.Permission;
import app.model.security.Role;
import app.model.security.User;
import app.repositories.PermissionRepository;
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
    private final PermissionRepository permissions;

    @Autowired
    public DatabaseLoader(ProductRepository products, UserRepository users,
                          RoleRepository roles, PermissionRepository permissions) {
        this.products = products;
        this.users = users;
        this.roles = roles;
        this.permissions = permissions;
    }

    @Override
    public void run(String... strings) throws Exception {
        Product wires = products.save(new Product("Wires", 520, 415, 0));
        Product oscilograf = products.save(new Product("Осциллограф цифровой", 26500, 2, 0));
        Product searcher = products.save(new Product("Искатель скрытых коммуникаций", 9500, 10, 0));


        Permission p_ordersFullControl = permissions.save(new Permission("ORDERS_FULL_CONTROL"));
        Permission p_ordersRead = permissions.save(new Permission("ORDERS_READ"));
        Permission p_ordersConfirmFinish = permissions.save(new Permission("ORDERS_CONFIRM_FINISH"));
        Permission p_ordersConfirmPayment = permissions.save(new Permission("ORDERS_CONFIRM_PAYMENT"));
        Permission p_ordersConfirmLegitimacy = permissions.save(new Permission("ORDERS_CONFIRM_LEGITIMACY"));

        Permission p_deliveryFullControl = permissions.save(new Permission("DELIVERY_FULL_CONTROL"));
        Permission p_deliveryStart = permissions.save(new Permission("DELIVERY_START"));

        Permission p_productsFullControl = permissions.save(new Permission("PRODUCTS_FULL_CONTROL"));
        Permission p_productsModify = permissions.save(new Permission("PRODUCTS_MODIFY"));


        Role role_admin = roles.save(new Role("ADMIN",
                permissions.findAll().toArray(new Permission[0]))); // Admins get all permissions B)
        Role role_storefront = roles.save(new Role("STOREFRONT",
                p_ordersRead, p_deliveryStart, p_productsModify, p_ordersConfirmFinish, p_ordersConfirmLegitimacy));
        Role role_delivery = roles.save(new Role("DELIVERY_DEPT",
                p_deliveryFullControl));
        Role role_money = roles.save(new Role("PAYMENT_SYSTEM",
                p_ordersConfirmPayment));


        User admin = users.save(new User("admin", "123", role_admin));
        User delivery = users.save(new User("delivery", "123", role_delivery));
        User store = users.save(new User("store", "123", role_storefront));
        User bank = users.save(new User("bank", "123", role_money));
    }
}
