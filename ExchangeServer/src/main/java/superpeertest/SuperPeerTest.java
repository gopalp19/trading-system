package superpeertest;

import superpeer.*;
import resourcesupport.*;
import messenger.*;
import messenger.mutualfundmessage.*;
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
		// MutualFundResultMessage resultMsg = new MutualFundResultMessage(Exchange.LONDON, "user1", MutualFund.BANKING, 20, LocalDateTime.now(), "1");

		MutualFundReserveMessage rsvMsg = new MutualFundReserveMessage(Continent.EUROPE, Stock.BP_PLC, 20, LocalDateTime.now(), "0");
		MutualFundReserveResponseMessage resMsg = new MutualFundReserveResponseMessage(Continent.EUROPE, Stock.BP_PLC, 20, LocalDateTime.now(), "0", true);
		MutualFundUpdateMessage updateMsg = new MutualFundUpdateMessage(Continent.EUROPE, Stock.BP_PLC, 20, LocalDateTime.now(), "0", true);


		londonSender.send(buyMsg);
		londonSender.send(rsvMsg);
		londonSender.send(resMsg);
		londonSender.send(updateMsg);
		// Thread.sleep(1000);
		// londonSender.send(updateMsg);
		Thread.sleep(1000);
		londonRec.printLog();
		// parisRec.printLog();

	}
}