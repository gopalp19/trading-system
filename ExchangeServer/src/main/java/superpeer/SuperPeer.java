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
		System.out.println(myExchange + " acting as SuperPeer for " + myExchange.continent());
		this.myExchange = myExchange;
	}

	public void run() {
		MessageQueue toSendQueue = new MessageQueue();
		MessageQueue toProcessQueue = new MessageQueue();
		MessageQueue exceptionQueue = new MessageQueue();
		MessageQueue logQueue = new MessageQueue();

		SuperPeerReceiver receiver = new SuperPeerReceiver(myExchange, toProcessQueue, logQueue);
		SuperPeerSender sender = new SuperPeerSender(myExchange, toSendQueue, exceptionQueue, logQueue);
		SuperPeerProcessor processor = new SuperPeerProcessor(myExchange, toProcessQueue, toSendQueue);
		SuperPeerExceptionHandler handler = new SuperPeerExceptionHandler(exceptionQueue, toProcessQueue);
		SuperPeerLogger logger = new SuperPeerLogger(myExchange.continent(), logQueue, "Logs/");

		logger.start();
		handler.start();
		sender.start();
		processor.start();
		receiver.start();
		
		try {
			receiver.join();
			sender.join();
			processor.join();
			handler.join();
			logger.join();
		}
		catch (InterruptedException e) {
			System.err.println("Exception in SuperPeer: " + e.getMessage());
		}
	}
}