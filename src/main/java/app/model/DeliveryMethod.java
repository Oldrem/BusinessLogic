package app.model;

import java.util.Arrays;

public enum DeliveryMethod
{
    COURIER("courier"),
    TAKEOUT("takeout");

    private String text;

    DeliveryMethod(String text)
    {
        this.text = text;
    }

    public String getText(){
        return text;
    }

    public static DeliveryMethod fromText(String text) {
        return Arrays.stream(values())
                .filter(bl -> bl.text.equalsIgnoreCase(text))
                .findFirst()
                .orElse(null);
    }
}
