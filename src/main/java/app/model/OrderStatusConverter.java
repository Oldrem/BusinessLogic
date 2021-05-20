package app.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, String>
{
    @Override
    public String convertToDatabaseColumn(OrderStatus category) {
        if (category == null) {
            return null;
        }
        return category.getText();
    }

    @Override
    public OrderStatus convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(OrderStatus.values())
                .filter(c -> c.getText().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
