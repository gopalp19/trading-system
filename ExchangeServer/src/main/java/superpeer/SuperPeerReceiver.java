package superpeer;

/*
	Class that accepts incoming messages from a given socket.
*/
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import resourcesupport.*;
import messenger.*;

public class SuperPeerReceiver extends Thread {
	private Exchange myExchange;
	private ArrayList<MessageQueue> acceptingQueues;
	private MessageQueue logQueue;

	SuperPeerReceiver(Exchange myExchange, ArrayList<MessageQueue> acceptingQueues_, MessageQueue logQueue) {
		this.myExchange = myExchange;
		acceptingQueues = acceptingQueues_;
		this.logQueue = logQueue;
	}

	SuperPeerReceiver(Exchange myExchange, MessageQueue acceptingQueue, MessageQueue logQueue) {
		this.myExchange = myExchange;
		acceptingQueues = new ArrayList<MessageQueue>();
		acceptingQueues.add(acceptingQueue);
		this.logQueue = logQueue;
	}

	public void run() 
	{
		try (
			ServerSocket superServer = new ServerSocket(myExchange.continent().portNum());
			)
		{
			while (true) {
				Socket superClient = superServer.accept();
				new SuperPeerReceiverConversation(superClient, acceptingQueues, logQueue).start();
			}

		}
		catch (Exception e) {
			System.err.println("Exception in SuperPeerReceiver main: " + e.getMessage());
		}
	}
}

class SuperPeerReceiverConversation extends Thread {
	private Socket superClient;
	private ArrayList<MessageQueue> acceptingQueues;
	private MessageQueue logQueue;

	SuperPeerReceiverConversation(Socket superClient, ArrayList<MessageQueue> acceptingQueues, MessageQueue logQueue) {
		this.superClient = superClient;
		this.acceptingQueues = acceptingQueues;
		this.logQueue = logQueue;
	}

	public void run() {
		try (
				BufferedReader in = new BufferedReader(new InputStreamReader(superClient.getInputStream()));
			)
		{
			MessageBroker broker = new MessageBroker();
			String line;
			ArrayList<String> raw_message = new ArrayList<String>();
			line = in.readLine();
			while (!line.isEmpty()) // messages finished by newline
			{
				raw_message.add(line);
				line = in.readLine();
			}
			Message full_message = broker.parse(raw_message);
			passMessage(full_message);
		}
		catch (Exception e) {
			System.err.println("Exception in SuperPeerReceiverConversation run: " + e.getMessage());
		}
	}

	void passMessage(Message full_message) {
		logQueue.add(full_message);
		for (MessageQueue queue : acceptingQueues) {
			queue.add(full_message);
		}
	}
}








