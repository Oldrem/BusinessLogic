package app.services;

import app.model.Order;
import app.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service("orderService")
@EnableScheduling //Does this work? Probably not
public class OrderService {

    public static Duration cancelPeriod = Duration.ofDays(1);

    private OrderRepository orders;

    @Autowired
    public void setData(OrderRepository orders) {
        this.orders = orders;
    }

    @Scheduled(fixedRate = 1000 * 60)
    public void scheduleFixedDelayTask() {

        for (Order order : orders.findAll()) {
            if (order.getConfirmed() && isOverCancelTime(order.getConfirmationDate()))
            {
                order.setCanceled(true);
                System.out.println("Cancelling order #" + order.getOrderId() + " - payment is overdue");
            }
        }
    }

    private static boolean isOverCancelTime(LocalDateTime dateTime)
    {
        Duration timePassed = Duration.between(dateTime, LocalDateTime.now());
        return timePassed.compareTo(cancelPeriod) >= 0;
    }
}
