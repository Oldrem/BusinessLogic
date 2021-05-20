package app.model;

import java.io.Serializable;
import java.util.Arrays;

public enum OrderStatus implements Serializable {
    NEW("new"),
    CONFIRMED("confirmed"),
    PAYED("payed"),
    RECEIVED("received"),
    CANCELED("canceled");

    private String text;

    OrderStatus(String text)
    {
        this.text = text;
    }

    public String getText(){
        return text;
    }

    public static OrderStatus fromText(String text) {
        return Arrays.stream(values())
                .filter(bl -> bl.text.equalsIgnoreCase(text))
                .findFirst()
                .orElse(null);
    }

}
