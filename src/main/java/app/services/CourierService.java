package app.services;

import app.model.Courier;
import app.repositories.CourierRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.stream.StreamSupport;

@Service("courierService")
public class CourierService
{
    private CourierRepository couriers;

    public CourierService(CourierRepository couriers)
    {
        this.couriers = couriers;
    }

    public Courier pickCourier()
    {
        Courier picked = null;
        int currentOrders = Integer.MAX_VALUE;

        StreamSupport.stream(couriers.findAll().spliterator(), false)
                .filter(Courier::isAvailable)
                .forEach(c -> System.out.println(c.getName() + ", " + c.getRequests().size()));

        return StreamSupport.stream(couriers.findAll().spliterator(), false)
                .filter(Courier::isAvailable)
                .min(Comparator.comparingInt(c -> c.getRequests().size())).get();
    }
}
