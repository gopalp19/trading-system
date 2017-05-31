package exchangeservertest;

import resourcesupport.*;
import messenger.*;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class DummyExchangeUserReceiver extends Thread {
	private Exchange myEx;
	private int myPort;
	private ArrayList<Message> log;

	public DummyExchangeUserReceiver(Exchange myEx) {
		this.myEx = myEx;
		myPort = myEx.portNum();
		log = new ArrayList<Message>();
	}

	public void run() {
		try (
				ServerSocket server = new ServerSocket(myPort);
			)
		{
			while (true) {
				Socket client = server.accept();
				synchronized (log) {
					log.add(readMessage(client));
				}
			}
		}
		catch (Exception e) {
			System.out.println("Exception in DummyExchangeUserReceiver run: " + e.getMessage());
		}
	}

	Message readMessage(Socket client) {
		try (
				BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
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
			return broker.parse(raw_message);
		}
		catch (Exception e) {
			System.out.println("Exception in DummyExchangeUserReceiver readMessage: " + e.getMessage());
			return new BuyMessage();
		}
	}

	void printLog() {
		synchronized (log) {
			System.out.println("Printing messages received by: " + myEx.textString);
			for (Message msg : log) {
				msg.print();
				System.out.println();
			}
		}
	}
}





