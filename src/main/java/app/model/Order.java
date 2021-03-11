package app.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "Order")
public class Order {
    private @Id @GeneratedValue Long orderId;
    private String clientName;
    private String clientLastName;
    private Long productId;
    private String fullAddress;
    private String methodOfDelivery;
    private Boolean isConfirmed;

    public Order() {}

    public Order(String clientName, String clientLastName, Long productId, String fullAddress, String methodOfDelivery, Boolean isConfirmed) {
        this.clientName = clientName;
        this.clientLastName = clientLastName;
        this.productId = productId;
        this.fullAddress = fullAddress;
        this.methodOfDelivery = methodOfDelivery;
        this.isConfirmed = isConfirmed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderId.equals(order.orderId) &&
                Objects.equals(clientName, order.clientName) &&
                Objects.equals(clientLastName, order.clientLastName) &&
                productId.equals(order.productId) &&
                Objects.equals(fullAddress, order.fullAddress) &&
                Objects.equals(methodOfDelivery, order.methodOfDelivery) &&
                Objects.equals(isConfirmed, order.isConfirmed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, clientName, clientLastName, productId, fullAddress, methodOfDelivery, isConfirmed);
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
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

    public Boolean getConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        isConfirmed = confirmed;
    }
}
