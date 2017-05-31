package superpeertest;

import superpeer.*;
import resourcesupport.*;
import messenger.*;
import messenger.mutualfundmessage.*;
import java.time.LocalDateTime;

public class SuperPeerTest {
	public static void main(String[] args) throws InterruptedException {
		// Real superpeers
		SuperPeer londonSuper = new SuperPeer(Exchange.LONDON);
		SuperPeer nySuper = new SuperPeer(Exchange.NEW_YORK_STOCK_EXCHANGE);
		SuperPeer johanSuper = new SuperPeer(Exchange.JOHANNESBURG);
		SuperPeer hkSuper = new SuperPeer(Exchange.HONG_KONG);

		// Dummy Exchanges (Simple message senders and receivers)
		DummyExchangeSender londonSender = new DummyExchangeSender(Exchange.LONDON);
		DummyExchangeSender tokyoSender = new DummyExchangeSender(Exchange.TOKYO);
		DummyExchangeReceiver londonRec = new DummyExchangeReceiver(Exchange.LONDON);
		DummyExchangeReceiver parisRec = new DummyExchangeReceiver(Exchange.EURONEXT_PARIS);
		DummyExchangeReceiver frankfurtRec = new DummyExchangeReceiver(Exchange.FRANKFURT);
		DummyExchangeReceiver nyseRec = new DummyExchangeReceiver(Exchange.NEW_YORK_STOCK_EXCHANGE);
		DummyExchangeReceiver tokyoRec = new DummyExchangeReceiver(Exchange.TOKYO);

		// Starting threads
		londonSuper.start();
		nySuper.start();
		johanSuper.start();
		hkSuper.start();
		londonRec.start();
		nyseRec.start();
		parisRec.start();
		frankfurtRec.start();
		tokyoRec.start();

		// Send a mutual fund buy message
		MutualFundBuyMessage buyMsg = new MutualFundBuyMessage(Exchange.LONDON, "user0", MutualFund.BANKING, 20, LocalDateTime.now(), "0");
		londonSender.send(buyMsg);
		Thread.sleep(1000);

		// Send reservation confirmation messages
		for (Stock stock : MutualFund.BANKING.stocks)
			tokyoSender.send(new MutualFundReserveResponseMessage(Continent.EUROPE, stock, 20, LocalDateTime.now(), "0", true));
		
		Thread.sleep(1000);
		// See what happened
		londonRec.printLog();
		parisRec.printLog();
		frankfurtRec.printLog();
		tokyoRec.printLog();
		nyseRec.printLog();

	}
}