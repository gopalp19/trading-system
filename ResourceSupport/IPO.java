package ResourceSupport;

import java.time.LocalDateTime;

/**
 * Container to store a stock's initial quantity.
 * Created by Alan on 4/27/2017.
 */
public class IPO {
    private final Stock stock;
    private  final LocalDateTime startTime;
    private final int quantity;

    IPO(Stock stock, LocalDateTime startTime, int quantity) {
        this.stock = stock;
        this.startTime = startTime;
        this.quantity = quantity;
    }

    public Stock stock() {
        return stock;
    }

    public LocalDateTime startTime() {
        return startTime;
    }

    public int quantity() {
        return quantity;
    }
}
