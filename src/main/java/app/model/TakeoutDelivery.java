package app.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class TakeoutDelivery {
    private @Id @GeneratedValue Long takeoutDeliveryId;
    @OneToOne
    private Order order;
    private String takeoutAddress;

    private TakeoutDelivery(){}

    public TakeoutDelivery(Order order, String takeoutAddress) {
        this.order = order;
        this.takeoutAddress = takeoutAddress;
    }

    public Long getTakeoutDeliveryId() {
        return takeoutDeliveryId;
    }

    public void setTakeoutDeliveryId(Long takeoutDeliveryId) {
        this.takeoutDeliveryId = takeoutDeliveryId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getTakeoutAddress() {
        return takeoutAddress;
    }

    public void setTakeoutAddress(String takeoutAddress) {
        this.takeoutAddress = takeoutAddress;
    }
}
