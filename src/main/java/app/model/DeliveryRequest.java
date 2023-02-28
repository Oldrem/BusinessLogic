package app.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "delivery_requests")
public class DeliveryRequest implements Serializable
{
    @Id @GeneratedValue
    private Long id;
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


    public Long getId() {
        return id;
    }

    public void setId(Long deliveryRequestId) {
        this.id = deliveryRequestId;
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
