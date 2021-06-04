package app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class Courier
{
    @Id @GeneratedValue
    private Long courierId;
    private String name;
    private String lastName;
    private boolean available;
    @OneToMany(targetEntity=DeliveryRequest.class, mappedBy="assignedCourier")
    private List<DeliveryRequest> requests;


    public Courier() {}

    public Courier(String name, String lastName, boolean available)
    {
        this.name = name;
        this.lastName = lastName;
        this.available = available;
    }


    public Long getCourierId() {
        return courierId;
    }

    public void setCourierId(Long courierId) {
        this.courierId = courierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @JsonIgnore
    public List<DeliveryRequest> getRequests() {
        return requests;
    }

    public List<DeliveryRequest> getActiveRequests() {
        return null; // requests.stream().filter(); TODO
    }

    public void setRequests(List<DeliveryRequest> requests) {
        this.requests = requests;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
