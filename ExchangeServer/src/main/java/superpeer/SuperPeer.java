package superpeer;

import messenger.*;
import resourcesupport.*;
import java.net.Socket;

/*
* Class that manages SuperPeer responsibilities
* Forwards orders, accepts new exchanges, 
*/
public class SuperPeer extends Thread {
	private Exchange myExchange;

	public SuperPeer(Exchange myExchange) {
		this.myExchange = myExchange;
	}

	public void run() {
		MessageQueue toSendQueue = new MessageQueue();
		MessageQueue toProcessQueue = new MessageQueue();

		SuperPeerReceiver receiver = new SuperPeerReceiver(myExchange, toProcessQueue);
		SuperPeerSender sender = new SuperPeerSender(myExchange, toSendQueue);
		SuperPeerProcessor processor = new SuperPeerProcessor(myExchange, toProcessQueue, toSendQueue);

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