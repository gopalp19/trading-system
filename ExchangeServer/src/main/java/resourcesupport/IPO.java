package resourcesupport;

import java.time.LocalDateTime;

/**
 * Container to store a stock's initial quantity.
 * Created by Alan on 4/27/2017.
 */
public class IPO {
    private Stock stock;
    private LocalDateTime startTime;
    private int quantity;

    IPO(Stock stock, LocalDateTime startTime, int quantity) {
        this.setStock(stock);
        this.setStartTime(startTime);
        this.setQuantity(quantity);
    }

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
