package app.services;

import app.jms.DeliveryRequestProducer;
import app.model.DeliveryRequest;
import app.model.Order;
import app.model.OrderStatus;
import app.model.Product;
import app.repositories.DeliveryRequestRepository;
import app.repositories.OrderRepository;
import app.repositories.ProductRepository;
import app.requests.OrderRequestBody;
import app.responces.ReceiptResponse;
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
                product.shipBooked(1);
                orders.save(order);
                products.save(product);
            }
        });
    }


    public Order startAddOrderTransaction(OrderRequestBody rawOrder)
    {
        Order order = rawOrder.constructOrder(products);
        Product product = order.getProduct();
        return transactionTemplate.execute(status ->
        {
            if (!product.tryBooking(1))
                throw new ProductBookingException("This product is either unavailable or fully booked");
            products.save(product);
            return orders.save(order);
        });
    }

    public void requestDelivery(Order order){
        DeliveryRequest request = new DeliveryRequest(order, "На складе", null);
        DeliveryRequestProducer producer = new DeliveryRequestProducer();
        producer.sendMessage(request);
    }

    public ReceiptResponse prepareReceipt(Order order)
    {
        String out = "ЗАО «ЧИП и ДИП» — Приборы, Радиодетали и Электронные компоненты\n\n";
        out += "Заказ №" + order.getOrderId() + "\n";
        out += order.getCreationDate() + "\n\n";
        Product product = order.getProduct();
        out += "1 " + product.getName() + "        " + product.getPrice() + "\n";
        out += "Итого: " + product.getPrice() + "\n";
        out += "Оплачено: " + product.getPrice() + "\n\n";
        out += "Очень важные строки текста: 342827984890235743625\n";
        out += "Ух как важно: ***********4973\n";
        out += "Никому не говори прям: #FG943782A4\n\n";
        out += "Спасибо, что выбрали нас! Ждём вас снова!";
        return new ReceiptResponse(out);
    }
}
