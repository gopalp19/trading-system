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

		MutualFundBuyMessage buyMsg = new MutualFundBuyMessage(Exchange.LONDON, "user0", MutualFund.BANKING, 20, LocalDateTime.now(), "0");
		MutualFundResultMessage resultMsg = new MutualFundResultMessage(Exchange.LONDON, "user1", MutualFund.BANKING, 20, LocalDateTime.now(), "1");

		londonSender.send(buyMsg);
		londonSender.send(resultMsg);
		Thread.sleep(1000);
		londonRec.printLog();
		parisRec.printLog();

	}
}