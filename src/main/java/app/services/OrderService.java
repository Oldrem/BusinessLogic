package app.services;

import app.model.DeliveryRequest;
import app.model.Order;
import app.model.OrderStatus;
import app.model.Product;
import app.repositories.DeliveryRequestRepository;
import app.repositories.OrderRepository;
import app.repositories.ProductRepository;
import app.requests.OrderRequestBody;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@Service("orderService")
@EnableScheduling
public class OrderService
{
    private OrderRepository orders;
    private ProductRepository products;
    private DeliveryRequestRepository deliveries;
    private TransactionTemplate transactionTemplate;
    private CourierService courierService;

    public OrderService(OrderRepository orders,
                        ProductRepository products,
                        DeliveryRequestRepository deliveries,
                        PlatformTransactionManager transactionManager,
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
}
