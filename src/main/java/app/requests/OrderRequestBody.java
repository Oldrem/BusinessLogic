package app.requests;

import app.model.DeliveryMethod;
import app.model.Order;
import app.model.OrderStatus;
import app.model.Product;
import app.repositories.ProductRepository;
import app.services.ProductBookingException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

public class OrderRequestBody
{
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^(.+)@(\\S+)$");

    private String clientName;
    private String clientLastName;
    private String clientEmail;
    private long productId;
    private String fullAddress;
    private String methodOfDelivery;

    public Order constructOrder(ProductRepository productRepository)
    {

        if (clientName == null || clientLastName == null)
            throw new ProductBookingException("Invalid client name or last name");

        Optional<Product> p = productRepository.findById(productId);
        if (!p.isPresent())
            throw new ProductBookingException("Invalid product ID");

        if (clientEmail == null || !EMAIL_PATTERN.matcher(clientEmail).matches())
            throw new ProductBookingException("Invalid E-Mail");

        if (methodOfDelivery == null || DeliveryMethod.fromText(methodOfDelivery) == null)
            throw new ProductBookingException("Invalid delivery method");

        if (fullAddress == null)
            throw new ProductBookingException("Invalid address");

        Product product = p.get();
        return new Order(clientName, clientLastName, clientEmail, product, fullAddress, methodOfDelivery,
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

    public void setClientEmail(String clientEmail)
    {
        this.clientEmail = clientEmail;
    }

    public String getClientEmail()
    {
        return clientEmail;
    }
}
