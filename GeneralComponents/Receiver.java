/*
	Class that accepts incoming messages from a given socket.
*/
import java.io.*;
import java.net.*;
import java.util.*;

public class Receiver implements Runnable {
	private Socket input;
	private Vector<MessageQueue> acceptingQueues;

	Receiver(Socket input_, Vector<MessageQueue> acceptingQueues_) {
		input = input_;
		acceptingQueues = acceptingQueues_;
	}

	public void run() 
	{
		MessageBroker broker = new MessageBroker();
		try (BufferedReader in = new BufferedReader(new InputStreamReader(input.getInputStream()));)
		{
			String line;
			Vector<String> raw_message = new Vector<String>();
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