package superpeer;

/*
	Class that accepts incoming messages from a given socket.
*/
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import Messenger.*;

public class Receiver extends Thread {
	private Socket input;
	private ArrayList<MessageQueue> acceptingQueues;

	Receiver(Socket input_, ArrayList<MessageQueue> acceptingQueues_) {
		input = input_;
		acceptingQueues = acceptingQueues_;
	}

	Receiver(Socket input_, MessageQueue acceptingQueue) {
		input = input_;
		acceptingQueues = new ArrayList<MessageQueue>(1);
		acceptingQueues.add(acceptingQueue);
	}

	public void run() 
	{
		MessageBroker broker = new MessageBroker();
		try (BufferedReader in = new BufferedReader(new InputStreamReader(input.getInputStream()));)
		{
			String line;
			ArrayList<String> raw_message = new ArrayList<String>();
			while(true) 
			{
				line = in.readLine();
				if (line.isEmpty()) 
				{
					Message full_message = broker.parse(raw_message);
					passMessage(full_message);
					raw_message.clear();
				}
				else 
				{
					raw_message.add(line);
				}
			}

		}
		catch (Exception e) {
			System.err.println("Exception in Receiver main: " + e.getMessage());
		}
	}

	void passMessage(Message full_message) {
		for (MessageQueue queue : acceptingQueues) {
			queue.add(full_message);
		}
	}
}