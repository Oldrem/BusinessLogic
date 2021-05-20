package app.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class DeliveryRequest implements Serializable
{
    @Id @GeneratedValue
    private Long deliveryRequestId;
    @OneToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
    private Order order;
    private String deliveryStatus;
    @ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
    @JoinColumn(name = "courierId", nullable = false)
    private Courier assignedCourier;


    public DeliveryRequest(){}

    public DeliveryRequest(Order order, String deliveryStatus, Courier assignedCourier) {
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

    public Courier getAssignedCourier() {
        return assignedCourier;
    }

    public void setAssignedCourier(Courier assignedCourier) {
        this.assignedCourier = assignedCourier;
    }
}
