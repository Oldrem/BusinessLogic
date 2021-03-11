package app.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name="client_order")
public class Order {
    private @Id @GeneratedValue Long orderId;
    private String clientName;
    private String clientLastName;
    @ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
    @JoinColumn(name = "productId", nullable = false)
    private Product product;
    private String fullAddress;
    private String methodOfDelivery;
    private LocalDateTime creationDate;
    private LocalDateTime confirmationDate;
    private Boolean isConfirmed;
    private Boolean isCanceled;
    private Boolean isPayed;
    private Boolean isReceived;

    public Order() {}

    public Order(String clientName, String clientLastName, Product product, String fullAddress, String methodOfDelivery, LocalDateTime creationDate, LocalDateTime confirmationDate, Boolean isConfirmed, Boolean isCanceled, Boolean isPayed, Boolean isReceived) {
        this.clientName = clientName;
        this.clientLastName = clientLastName;
        this.product = product;
        this.fullAddress = fullAddress;
        this.methodOfDelivery = methodOfDelivery;
        this.creationDate = creationDate;
        this.confirmationDate = confirmationDate;
        this.isConfirmed = isConfirmed;
        this.isCanceled = isCanceled;
        this.isPayed = isPayed;
        this.isReceived = isReceived;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(LocalDateTime confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public Boolean getConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        isConfirmed = confirmed;
    }

    public Boolean getCanceled() {
        return isCanceled;
    }

    public void setCanceled(Boolean canceled) {
        isCanceled = canceled;
    }

    public Boolean getPayed() {
        return isPayed;
    }

    public void setPayed(Boolean payed) {
        isPayed = payed;
    }

    public Boolean getReceived() {
        return isReceived;
    }

    public void setReceived(Boolean received) {
        isReceived = received;
    }
}
