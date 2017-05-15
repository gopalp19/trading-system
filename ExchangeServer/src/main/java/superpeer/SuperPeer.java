package superpeer;

import messenger.*;
import java.net.Socket;

/*
* Class that manages SuperPeer responsibilities
* Forwards orders, accepts new exchanges, 
*/
public class SuperPeer extends Thread {
	String identifier;
	int port;
	Socket left;
	Socket right;

	SuperPeer(String identifier, int port, Socket left, Socket right) {
		this.identifier = identifier;
		this.port = port;
		this.left = left;
		this.right = right;
	}

	public void run() {
		MessageQueue toSendQueue = new MessageQueue();
		MessageQueue toProcessQueue = new MessageQueue();

		Receiver leftReceiver = new Receiver(left, toProcessQueue);
		Receiver rightReceiver = new Receiver(right, toProcessQueue);
		Sender sender = new Sender(toSendQueue, left, right);
		SuperPeerProcessor processor = new SuperPeerProcessor(toProcessQueue, toSendQueue);

		leftReceiver.start();
		rightReceiver.start();
		sender.start();
		processor.start();

		try {
			leftReceiver.join();
			rightReceiver.join();
			sender.join();
			processor.join();
		}
		catch (InterruptedException e) {
			System.err.println("Exception in SuperPeer: " + e.getMessage());
		}
	}
}