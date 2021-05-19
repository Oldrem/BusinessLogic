package app.model;

import javax.persistence.*;

@Entity
public class Courier
{
    @Id @GeneratedValue
    private Long courierId;
    private String name;
    private String surname;
    @OneToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
    private DeliveryRequest assignedRequest;


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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public DeliveryRequest getAssignedRequest() {
        return assignedRequest;
    }

    public void setAssignedRequest(DeliveryRequest assignedRequest) {
        this.assignedRequest = assignedRequest;
    }
}
