package superpeertest;

import superpeer.*;
import resourcesupport.*;
import messenger.*;
import messenger.mutualfundmessage.*;
import java.time.LocalDateTime;

public class SuperPeerTest {
	public static void main(String[] args) throws InterruptedException {
		SuperPeer londonSuper = new SuperPeer(Exchange.LONDON);
		SuperPeer nySuper = new SuperPeer(Exchange.NEW_YORK_STOCK_EXCHANGE);
		SuperPeer johanSuper = new SuperPeer(Exchange.JOHANNESBURG);
		SuperPeer hkSuper = new SuperPeer(Exchange.HONG_KONG);
		DummyExchangeSender londonSender = new DummyExchangeSender(Exchange.LONDON);
		DummyExchangeSender parisSender = new DummyExchangeSender(Exchange.EURONEXT_PARIS);
		DummyExchangeReceiver londonRec = new DummyExchangeReceiver(Exchange.LONDON);
		DummyExchangeReceiver parisRec = new DummyExchangeReceiver(Exchange.EURONEXT_PARIS);
		DummyExchangeReceiver frankfurtRec = new DummyExchangeReceiver(Exchange.FRANKFURT);
		DummyExchangeReceiver nyseRec = new DummyExchangeReceiver(Exchange.NEW_YORK_STOCK_EXCHANGE);
		DummyExchangeReceiver tokyoRec = new DummyExchangeReceiver(Exchange.TOKYO);

		londonSuper.start();
		nySuper.start();
		johanSuper.start();
		hkSuper.start();
		// londonRec.start();
		// nyseRec.start();
		// parisRec.start();
		// frankfurtRec.start();
		// tokyoRec.start();


		Thread.sleep(1000);

		MutualFundBuyMessage buyMsg = new MutualFundBuyMessage(Exchange.LONDON, "user0", MutualFund.BANKING, 20, LocalDateTime.now(), "0");
		// MutualFundResultMessage resultMsg = new MutualFundResultMessage(Exchange.LONDON, "user1", MutualFund.BANKING, 20, LocalDateTime.now(), "1");

		// MutualFundReserveMessage rsvMsg = new MutualFundReserveMessage(Continent.EUROPE, Stock.BP_PLC, 20, LocalDateTime.now(), "0");
		// MutualFundReserveResponseMessage resMsg = new MutualFundReserveResponseMessage(Continent.EUROPE, Stock.DEUTSCHE_BANK, 20, LocalDateTime.now(), "0", true);
		// MutualFundUpdateMessage updateMsg = new MutualFundUpdateMessage(Continent.EUROPE, Stock.BP_PLC, 20, LocalDateTime.now(), "0", true);

		londonSender.send(buyMsg);
		Thread.sleep(1000);

		// for (Stock stock : MutualFund.BANKING.stocks)
		// 	londonSender.send(new MutualFundReserveResponseMessage(Continent.EUROPE, stock, 20, LocalDateTime.now(), "0", false));

		// londonSender.send(resMsg);
		// londonSender.send(updateMsg);
		// Thread.sleep(1000);
		// londonSender.send(updateMsg);
		// Thread.sleep(1000);
		londonRec.printLog();
		parisRec.printLog();
		frankfurtRec.printLog();
		tokyoRec.printLog();
		nyseRec.printLog();

	}
}