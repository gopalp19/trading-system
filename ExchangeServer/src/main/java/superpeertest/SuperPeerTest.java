package superpeertest;

import superpeer.*;
import resourcesupport.*;
import messenger.*;
import java.time.LocalDateTime;

public class SuperPeerTest {
	public static void main(String[] args) throws InterruptedException {
		SuperPeer londonSuper = new SuperPeer(Exchange.LONDON);
		DummyExchangeSender londonSender = new DummyExchangeSender(Exchange.LONDON);
		DummyExchangeSender parisSender = new DummyExchangeSender(Exchange.EURONEXT_PARIS);
		DummyExchangeReceiver londonRec = new DummyExchangeReceiver(Exchange.LONDON);
		DummyExchangeReceiver parisRec = new DummyExchangeReceiver(Exchange.EURONEXT_PARIS);

		londonSuper.start();
		londonRec.start();
		parisRec.start();
		Thread.sleep(1000);

		BuyMessage lonMsg = new BuyMessage(Exchange.LONDON, "user0", Stock.BARCLAYS_PLC, 10, LocalDateTime.now(), "0");
		SellMessage parisMsg = new SellMessage(Exchange.EURONEXT_PARIS, "user1", Stock.MICHELIN, 10, LocalDateTime.now(), "1");

		londonSender.send(lonMsg);
		londonSender.send(parisMsg);
		Thread.sleep(1000);
		londonRec.printLog();
		parisRec.printLog();

	}
}