package app.services;

public class OrderPaymentException extends RuntimeException {
    public OrderPaymentException(String message)
    {
        super(message);
    }
}
