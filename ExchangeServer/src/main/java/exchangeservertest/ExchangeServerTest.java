package exchangeservertest;

import messenger.*;
import resourcesupport.*;
import java.time.LocalDateTime;

/**
 * Spawn a number of DummyUsers that place random orders for an amount of time.
 * @author Alan
 *
 */
class ExchangeServerTest {
	public static void main(String[] args) {
		String username = "user1";

		DummyExchangeUser user;
		try {
			user = new DummyExchangeUser(Exchange.LONDON, username);
		}
		catch (Exception e) {
			System.out.println("Connection could not be formed: " + e.getMessage());
			return;
		}

		user.start();
		BuyMessage buy = new BuyMessage(Exchange.LONDON, username, Stock.BP_PLC, 300, LocalDateTime.now(), orderId.toString());
		user.send(buy);
	}
}