package app.services.scheduling;

import app.model.Order;
import app.model.OrderStatus;
import app.model.Product;
import app.repositories.OrderRepository;
import app.repositories.ProductRepository;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import java.time.Duration;
import java.time.LocalDateTime;


public class UnpaidOrderCancellationJob implements Job
{
    public static final Duration checkPeriod = Duration.ofSeconds(10);
    public static final Duration cancelAfter = Duration.ofSeconds(20);

    @Override
    public void execute(JobExecutionContext jobExecutionContext)
    {
        OrderRepository orders = null;
        ProductRepository products = null;

        try {
            orders = (OrderRepository) jobExecutionContext.getScheduler().getContext().get("orders");
            products = (ProductRepository) jobExecutionContext.getScheduler().getContext().get("products");
        }
        catch (SchedulerException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Launching overdue order cancellation...");
        int cancelled = 0;

        for (Order order : orders.findAll()) {

            if (order.getStatus() == OrderStatus.CONFIRMED && isOverCancelTime(order.getConfirmationDate()))
            {
                System.out.println("Cancelling order #" + order.getId() + " - payment is overdue");
                order.setStatus(OrderStatus.CANCELED);
                orders.save(order);
                Product product = order.getProduct();
                product.releaseBooking(1);
                products.save(product);
                cancelled++;
            }
        }

        System.out.println("Cancelled " + cancelled + " orders.");
    }

    private static boolean isOverCancelTime(LocalDateTime dateTime)
    {
        return Duration.between(dateTime, LocalDateTime.now()).compareTo(cancelAfter) >= 0;
    }
}
