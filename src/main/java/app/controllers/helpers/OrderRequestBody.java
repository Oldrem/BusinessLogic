package app.controllers.helpers;

import app.model.Order;
import app.model.Product;
import app.repositories.ProductRepository;
import app.services.ProductBookingException;

import java.time.LocalDateTime;
import java.util.Optional;

public class OrderRequestBody
{
    private String clientName;
    private String clientLastName;
    private long productId;
    private String fullAddress;
    private String methodOfDelivery;

    public Order constructOrder(ProductRepository productRepository)
    {
        Optional<Product> p = productRepository.findById(productId);
        if (!p.isPresent())
            throw new ProductBookingException("Invalid product ID");

        Product product = p.get();
        return new Order(clientName, clientLastName, product, fullAddress, methodOfDelivery,
                LocalDateTime.now(), null,
                false, false, false, false);
    }
}
