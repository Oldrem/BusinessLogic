package app.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "products")
public class Product implements Serializable {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private int price;
    private int storedAmount;
    private int bookedAmount;

    public Product (){}

    public Product(String name, int price, int storedAmount, int bookedAmount) {
        this.name = name;
        this.price = price;
        this.storedAmount = storedAmount;
        this.bookedAmount = bookedAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStoredAmount() {
        return storedAmount;
    }

    public void setStoredAmount(int amount) {
        this.storedAmount = amount;
    }

    public int getBookedAmount() {
        return bookedAmount;
    }

    public void setBookedAmount(int bookedAmount) {
        this.bookedAmount = bookedAmount;
    }

    public boolean tryBooking(int amount)
    {
        if (bookedAmount + amount > storedAmount) return false;
        bookedAmount += amount;
        return true;
    }

    public void releaseBooking(int amount)
    {
        bookedAmount -= amount;
    }

    public void shipBooked(int amount)
    {
        bookedAmount -= amount;
        storedAmount -= amount;
    }
}
