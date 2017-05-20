package messenger;

/*
	Class that accepts incoming messages from a given socket.
*/
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import resourcesupport.*;

public class SuperPeerReceiver extends Thread {
	private Exchange myExchange;
	private ArrayList<MessageQueue> acceptingQueues;

	SuperPeerReceiver(Exchange myExchange, ArrayList<MessageQueue> acceptingQueues_) {
		this.myExchange = myExchange;
		acceptingQueues = acceptingQueues_;
	}

	SuperPeerReceiver(Exchange myExchange, MessageQueue acceptingQueue) {
		this.myExchange = myExchange;
		acceptingQueues = new ArrayList<MessageQueue>();
		acceptingQueues.add(acceptingQueue);
	}

	public void run() 
	{
		MessageBroker broker = new MessageBroker();
		try (
			ServerSocket superServer = new ServerSocket(myExchange.continent().portNum());
			)
		{
			while (true) {
				Socket superClient = superServer.accept();
				new SuperPeerReceiverConversation(superClient, acceptingQueues).start();
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

	SuperPeerReceiverConversation(Socket superClient, ArrayList<MessageQueue> acceptingQueues) {
		this.superClient = superClient;
		this.acceptingQueues = acceptingQueues;
	}

	public void run() {
		try (
				BufferedReader in = new BufferedReader(new InputStreamReader(superClient.getInputStream()));
			)
		{
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
		for (MessageQueue queue : acceptingQueues) {
			queue.add(full_message);
		}
	}
}








