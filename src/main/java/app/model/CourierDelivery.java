package app.model;

import javax.persistence.*;

@Entity
public class CourierDelivery {
    private @Id @GeneratedValue Long courierDeliveryId;
    @OneToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
    private Order order;
    private String assignedCourier;

    private CourierDelivery(){}

    public CourierDelivery(Order order, String assignedCourier) {
        this.order = order;
        this.assignedCourier = assignedCourier;
    }

    public Long getCourierDeliveryId() {
        return courierDeliveryId;
    }

    public void setCourierDeliveryId(Long courierDeliveryId) {
        this.courierDeliveryId = courierDeliveryId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getAssignedCourier() {
        return assignedCourier;
    }

    public void setAssignedCourier(String assignedCourier) {
        this.assignedCourier = assignedCourier;
    }
}
