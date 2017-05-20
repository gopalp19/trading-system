package superpeer;

import messenger.*;


/*
	Stub processer
	Will take messages from toProcessQueue, act on them,
	and may put messages in toSendQueue
*/

class SuperPeerProcessor extends Thread {
	MessageQueue toProcessQueue;
	MessageQueue toSendQueue;

	SuperPeerProcessor(MessageQueue toProcessQueue, MessageQueue toSendQueue) {
		this.toProcessQueue = toProcessQueue;
		this.toSendQueue = toSendQueue;
	}

	public void run() {
		while(true) {
			Message next = toProcessQueue.take();
			if (atDestination(next)
				process(next);
			else
				forward(next);
		}
	}

	// Process message if this is its destination
	void process(Message next) {
		next.print();
		toSendQueue.add(next);
	}

	// Forward message towards it destination
	void forward(Message next) {
		
	}
}