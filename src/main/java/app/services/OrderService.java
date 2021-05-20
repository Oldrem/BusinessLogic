package app.services;

import app.model.DeliveryRequest;
import app.model.Order;
import app.model.OrderStatus;
import app.model.Product;
import app.repositories.DeliveryRequestRepository;
import app.repositories.OrderRepository;
import app.repositories.ProductRepository;
import app.requests.OrderRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Duration;
import java.time.LocalDateTime;

@Service("orderService")
@EnableScheduling
public class OrderService
{
    public static final Duration cancelPeriod = Duration.ofMinutes(1);

    private OrderRepository orders;
    private ProductRepository products;
    private DeliveryRequestRepository deliveries;
    private TransactionTemplate transactionTemplate;
    private CourierService courierService;

    @Autowired
    public void setData(OrderRepository orders,
                        ProductRepository products,
                        DeliveryRequestRepository deliveries,
                        JtaTransactionManager transactionManager,
                        CourierService courierService)
    {
        this.orders = orders;
        this.products = products;
        this.deliveries = deliveries;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.courierService = courierService;
    }


    public void startOnOrderPaidTransaction(Order order)
    {
        if (order.getStatus() != OrderStatus.CONFIRMED)
            throw new OrderPaymentException("Order has not been confirmed yet");

        transactionTemplate.execute(new TransactionCallbackWithoutResult()
        {
            protected void doInTransactionWithoutResult(TransactionStatus status)
            {
                order.setStatus(OrderStatus.PAYED);
                Product product = order.getProduct();
                product.setBookedAmount(product.getBookedAmount() - 1);
                product.setAmount(product.getAmount() - 1);

                DeliveryRequest request = new DeliveryRequest(order, "На складе", null);
                if (order.getMethodOfDelivery().equals("courier"))
                {
                    request.setAssignedCourier(courierService.pickCourier());
                }
                deliveries.save(request);

                orders.save(order);
                products.save(product);
            }
        });
    }


    public Order startAddOrderTransaction(OrderRequestBody rawOrder)
    {
        Order order = rawOrder.constructOrder(products);
        Product product = order.getProduct();
        return transactionTemplate.execute(status -> {
            product.setBookedAmount(order.getProduct().getBookedAmount() + 1);
            products.save(product);
            if (product.getAmount() < product.getBookedAmount())
                throw new ProductBookingException("This product is either unavailable or fully booked");
            return orders.save(order);
        });
    }



    @Scheduled(fixedRate = 1000 * 60)
    public void removedUnpaidOverdueOrders()
    {
        System.out.println("Launching overdue order cancellation...");
        int cancelled = 0;

        for (Order order : orders.findAll()) {
            if (order.getStatus().getText().equals("confirmed") && isOverCancelTime(order.getConfirmationDate()))
            {
                order.setStatus(OrderStatus.CANCELED);
                orders.save(order);
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
