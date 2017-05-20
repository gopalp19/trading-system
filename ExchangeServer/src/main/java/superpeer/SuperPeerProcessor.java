package superpeer;

import messenger.*;
import resourcesupport.*;


/*
	Stub processer
	Will take messages from toProcessQueue, act on them,
	and may put messages in toSendQueue
*/

class SuperPeerProcessor extends Thread {
	MessageQueue toProcessQueue;
	MessageQueue toSendQueue;
	resourcesupport.Exchange myExchange;

	SuperPeerProcessor(Exchange myExchange, MessageQueue toProcessQueue, MessageQueue toSendQueue) {
		this.toProcessQueue = toProcessQueue;
		this.toSendQueue = toSendQueue;
		this.myExchange = myExchange;
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
		next.print();
		// TODO pass to local exchange
	}

	// Forward message towards it destination
	// Sender will deal with specifics
	void forward(Message next) {
		toSendQueue.add(next);
	}

	boolean atDestination(Message next) {
		// TODO add SuperPeer specific destination
		return myExchange == next.destination;
	}
}