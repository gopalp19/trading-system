package superpeer;

import messenger.*;
import java.net.Socket;

/*
* Class that manages SuperPeer responsibilities
* Forwards orders, accepts new exchanges, 
*/
public class SuperPeer extends Thread {
	private Exchange myExchange;

	SuperPeer(Exchange myExchange) {
		this.myExchange = myExchange;
	}

	public void run() {
		MessageQueue toSendQueue = new MessageQueue();
		MessageQueue toProcessQueue = new MessageQueue();

		Receiver receiver = new Receiver(myExchange, toProcessQueue);
		Sender sender = new Sender(myExchange, toSendQueue);
		SuperPeerProcessor processor = new SuperPeerProcessor(toProcessQueue, toSendQueue);

		receiver.start();
		sender.start();
		processor.start();

		try {
			receiver.join();
			sender.join();
			processor.join();
		}
		catch (InterruptedException e) {
			System.err.println("Exception in SuperPeer: " + e.getMessage());
		}
	}
}