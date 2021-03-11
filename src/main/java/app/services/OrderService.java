package app.services;

import app.model.Order;
import app.model.Product;
import app.repositories.OrderRepository;
import app.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service("orderService")
@EnableScheduling
public class OrderService
{
    public static final Duration cancelPeriod = Duration.ofDays(1);

    private OrderRepository orders;
    private ProductRepository products;

    @Autowired
    public void setData(OrderRepository orders, ProductRepository products) {
        this.orders = orders;
        this.products = products;
    }


    @Transactional
    public void confirmOrderPayment(Order order)
    {
        order.setPayed(true);
        Product product = order.getProduct();
        product.setBookedAmount(product.getBookedAmount() - 1);
        product.setAmount(product.getAmount() - 1);
        //TODO start delivery
    }

    @Transactional
    public Order addOrder(Order order)
    {
        Optional<Product> p = products.findById(order.getProduct().getId());
        if (!p.isPresent())
            throw new ProductBookingException("Invalid product ID");

        Product product = p.get();
        product.setBookedAmount(order.getProduct().getBookedAmount()+1);
        if (product.getAmount() < product.getBookedAmount())
            throw new ProductBookingException("This product is either unavailable or all booked");
        return orders.save(order);
    }



    @Scheduled(fixedRate = 1000 * 60)
    public void scheduleFixedDelayTask()
    {
        System.out.println("Launching overdue order cancellation...");
        int cancelled = 0;

        for (Order order : orders.findAll()) {
            if (order.getConfirmed() && !order.getPayed() && isOverCancelTime(order.getConfirmationDate()))
            {
                order.setCanceled(true);
                System.out.println("Cancelling order #" + order.getOrderId() + " - payment is overdue");
                cancelled++;
            }
        }

        System.out.println("Cancelled " + cancelled + " orders.");
    }

    private static boolean isOverCancelTime(LocalDateTime dateTime)
    {
        Duration timePassed = Duration.between(dateTime, LocalDateTime.now());
        return timePassed.compareTo(cancelPeriod) >= 0;
    }
}
