package superpeer;

import messenger.*;
import messenger.mutualfundmessage.*;
import resourcesupport.*;
import java.util.Hashtable;
import java.util.ArrayList;
import java.time.LocalDateTime;


/*
	Stub processer
	Will take messages from toProcessQueue, act on them,
	and may put messages in toSendQueue
*/

class SuperPeerProcessor extends Thread {
	MessageQueue toProcessQueue;
	MessageQueue toSendQueue;
	resourcesupport.Exchange myExchange;
	MutualFundManager mutualManager;

	SuperPeerProcessor(Exchange myExchange, MessageQueue toProcessQueue, MessageQueue toSendQueue) {
		this.toProcessQueue = toProcessQueue;
		this.toSendQueue = toSendQueue;
		this.myExchange = myExchange;
		mutualManager = new MutualFundManager(toSendQueue);
	}

	public void run() {
		while(true) {
			Message next = toProcessQueue.take();
			if (atDestination(next))
				process(next);
			else
				forward(next);
		}
	}

	// Process message if this is its destination
	void process(Message next) {
		if (next instanceof MutualFundBuyMessage) {
			mutualManager.processOrder((MutualFundBuyMessage) next);
		}
		next.print();
	}

	// Forward message towards it destination
	// Sender will deal with specifics
	void forward(Message next) {
		toSendQueue.add(next);
	}

	boolean atDestination(Message next) {
		if (next instanceof ExchangeMessage)
			return false;
		return ((SuperPeerMessage)(next)).getDestination() == myExchange.continent();
	}
}

class MutualFundManager {
	Integer currentOrderNum = 0;
	Hashtable<String, MutualFundOrder> orders;
	MessageQueue toSendQueue;
	Continent myContinent;

	MutualFundManager(MessageQueue toSendQueue) {
		this.toSendQueue = toSendQueue;
		orders = new Hashtable<String, MutualFundOrder>();
	}

	void processOrder(MutualFundBuyMessage message) {
		if (message.quantity % message.fund.minimumBlock != 0) // units must be divisible by minimumBlock
			sendResultMessage(message, 0); // failure encoded as 0 units bought

		String orderId = (currentOrderNum++).toString();
		for (int i = 0; i < message.fund.stocks.length; ++i)  {
			sendReserveMessage(orderId, message.fund.stocks[i], message.quantity/message.fund.weights[i]);
		}
		orders.put(orderId, new MutualFundOrder(message));
	}

	void sendReserveMessage(String orderId, Stock stock, Integer amount) {
		toSendQueue.add(new MutualFundReserveMessage(myContinent, stock, amount, LocalDateTime.now(), orderId));
	}

	void sendResultMessage(MutualFundBuyMessage order, Integer unitsBought) {
		toSendQueue.add(new MutualFundResultMessage(order.buyerExchange, order.buyerUserName,
			order.fund, unitsBought, LocalDateTime.now(), order.orderID));
	}
}

class MutualFundOrder {
	MutualFundBuyMessage message;
	ArrayList<OrderStatus> status;

	MutualFundOrder(MutualFundBuyMessage message) {
		this.message = message;
		status = new ArrayList<OrderStatus>(message.fund.stocks.length);
	}
}

enum OrderStatus {
	WAITING, RESERVED;
}







