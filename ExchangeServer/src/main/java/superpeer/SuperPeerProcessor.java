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
		mutualManager = new MutualFundManager(myExchange.continent(), toSendQueue);
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
		else if (next instanceof MutualFundReserveResponseMessage)
			mutualManager.processReserveResponse((MutualFundReserveResponseMessage) next);
		// TODO: other message types
		else 
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

	MutualFundManager(Continent myContinent, MessageQueue toSendQueue) {
		this.toSendQueue = toSendQueue;
		orders = new Hashtable<String, MutualFundOrder>();
		this.myContinent = myContinent;
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

	void processReserveResponse(MutualFundReserveResponseMessage message) {
		MutualFundOrder order = orders.get(message.orderID);
		if (null == order) {
			return;
		}

		if (message.reservationConfirmed) {		
			order.confirm(message.stock);
			if (order.allReserved())
				commitOrder(message.orderID, order);
		}
		else {
			cancelOrder(message.orderID, order);
			toSendQueue.add(order.getResultMessage(false));
			orders.remove(message.orderID);
		}
	}

	void sendReserveMessage(String orderId, Stock stock, Integer amount) {
		toSendQueue.add(new MutualFundReserveMessage(myContinent, stock, amount, LocalDateTime.now(), orderId));
	}

	void sendResultMessage(MutualFundBuyMessage order, Integer unitsBought) {
		toSendQueue.add(new MutualFundResultMessage(order.buyerExchange, order.buyerUserName,
			order.fund, unitsBought, LocalDateTime.now(), order.orderID));
	}

	void commitOrder(String orderID, MutualFundOrder order) {
		sendUpdates(orderID, order, true);
		toSendQueue.add(order.getResultMessage(true));
	}

	void cancelOrder(String orderID, MutualFundOrder order) {
		sendUpdates(orderID, order, false);
	}

	void sendUpdates(String orderID, MutualFundOrder order, boolean doCommit) {
		for (Stock stock : order.fund().stocks) {
			toSendQueue.add(new MutualFundUpdateMessage(myContinent, stock, LocalDateTime.now(), orderID, doCommit));
		}
	}
}

class MutualFundOrder {
	MutualFundBuyMessage message;
	ArrayList<OrderStatus> status;
	int totalReserved = 0;

	MutualFundOrder(MutualFundBuyMessage message) {
		this.message = message;
		status = new ArrayList<OrderStatus>();
		for (int i = 0; i < message.fund.stocks.length; ++i) {
			status.add(OrderStatus.WAITING);
		}
	}

	void confirm(Stock stock) {
		int index = java.util.Arrays.asList(message.fund.stocks).indexOf(stock);
		if (index < 0) {
			System.err.println("Stock: " + stock + " is not a part of MutualFund: " + message.fund);
			return;
		}
		status.set(index, OrderStatus.RESERVED);
		++totalReserved;
	}

	boolean allReserved() {
		if (totalReserved < status.size()) // quick check for definite negative
			return false;

		// Still have to check all in case of duplicate confirmations (from crash recovery)
		for (OrderStatus stat : status) {
			if (stat == OrderStatus.WAITING)
				return false;
		}
		return true;
	}

	MutualFund fund() {
		return message.fund;
	}

	void printStatus() {
		for (int i = 0; i < status.size(); ++ i) {
			System.out.println(message.fund.stocks[i] + ": " + status.get(i));
		}
	}

	MutualFundResultMessage getResultMessage(boolean success) {
		int quantity = success ? message.quantity : 0;
		return new MutualFundResultMessage(message.buyerExchange, message.buyerUserName, message.fund, quantity, LocalDateTime.now(), message.orderID);
	}
}

enum OrderStatus {
	WAITING, RESERVED;
}







