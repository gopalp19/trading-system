package superpeer;

import Messenger.*;


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
			process(next);
		}
	}

	// Process message
	void process(Message next) {
		next.print();
		toSendQueue.add(next);
	}
}