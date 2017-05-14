package resourcesupport;

import java.time.LocalDateTime;

/**
 * Container to store a stock's instantaneous price.
 * Created by Alan on 4/27/2017.
 */
public class PriceInstance {
    private final Stock stock;
    private final LocalDateTime dateTime;
    private final float price;

    PriceInstance(Stock stock, LocalDateTime dateTime, float price) {
        this.stock = stock;
        this.dateTime = dateTime;
        this.price = price;
    }

    public Stock stock() {
        return stock;
    }

    public LocalDateTime dateTime() {
        return dateTime;
    }

    public float price() {
        return price;
    }
}
