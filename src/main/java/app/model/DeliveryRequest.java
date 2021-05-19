package app.model;

import javax.persistence.*;

@Entity
public class DeliveryRequest
{
    @Id @GeneratedValue
    private Long deliveryRequestId;
    @OneToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
    private Order order;
    private String deliveryStatus;
    private String assignedCourier;

    public DeliveryRequest(){}

    public DeliveryRequest(Order order, String deliveryStatus, String assignedCourier) {
        this.order = order;
        this.deliveryStatus = deliveryStatus;
        this.assignedCourier = assignedCourier;
    }

    public Long getDeliveryRequestId() {
        return deliveryRequestId;
    }

    public void setDeliveryRequestId(Long deliveryRequestId) {
        this.deliveryRequestId = deliveryRequestId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getAssignedCourier() {
        return assignedCourier;
    }

    public void setAssignedCourier(String assignedCourier) {
        this.assignedCourier = assignedCourier;
    }
}
