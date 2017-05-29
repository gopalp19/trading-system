package superpeer;

import messenger.*;
import messenger.mutualfundmessage.*;

class SuperPeerExceptionHandler extends Thread {
	private MessageQueue exceptionQueue;
	private MessageQueue toProcessQueue;

	SuperPeerExceptionHandler(MessageQueue exceptionQueue, MessageQueue toProcessQueue) {
		this.exceptionQueue = exceptionQueue;
		this.toProcessQueue = toProcessQueue;
	}

	public void run() {
		while (true) {
			Message next = exceptionQueue.take();
			if (next instanceof ExchangeMessage)
				handleExchangeMessage((ExchangeMessage) next);
			else
				handleSuperPeerMessage((SuperPeerMessage) next);
		}
	}

	void handleExchangeMessage(ExchangeMessage next) {
		if (next instanceof BuyMessage)
			toProcessQueue.add(new BuyResultMessage((BuyMessage) next));
		else if (next instanceof SellMessage) 
			toProcessQueue.add(new SellResultMessage((SellMessage) next));
		else if (next instanceof MutualFundReserveMessage)
			toProcessQueue.add(new MutualFundReserveResponseMessage((MutualFundReserveMessage) next));
		else
			// TODO: make new sleeping-sender that waits for next-hop destination to return
			System.err.println("Result message to " + next.getDestination() + " could not be sent");
	}

	void handleSuperPeerMessage(SuperPeerMessage next) {
		if (next instanceof MutualFundBuyMessage)
			toProcessQueue.add(new MutualFundResultMessage((MutualFundBuyMessage) next));
		else
			System.err.println("MutualFundMessage to " + next.getDestination() + " could not be sent");
	}

}