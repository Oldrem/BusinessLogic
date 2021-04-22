package app.requests;

import app.model.DeliveryMethod;
import app.model.Order;
import app.model.OrderStatus;
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

        if (DeliveryMethod.fromText(methodOfDelivery) == null)
            throw new ProductBookingException("Invalid delivery method");

        Product product = p.get();
        return new Order(clientName, clientLastName, product, fullAddress, methodOfDelivery,
                LocalDateTime.now(), null,
                OrderStatus.NEW);
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientLastName() {
        return clientLastName;
    }

    public void setClientLastName(String clientLastName) {
        this.clientLastName = clientLastName;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getMethodOfDelivery() {
        return methodOfDelivery;
    }

    public void setMethodOfDelivery(String methodOfDelivery) {
        this.methodOfDelivery = methodOfDelivery;
    }
}
