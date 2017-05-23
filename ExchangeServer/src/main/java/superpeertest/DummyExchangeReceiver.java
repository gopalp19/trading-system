package superpeertest;

import resourcesupport.*;
import messenger.*;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class DummyExchangeReceiver extends Thread {
	private Exchange myEx;
	private int myPort;
	private ArrayList<ExchangeMessage> log;

	public DummyExchangeReceiver(Exchange myEx) {
		this.myEx = myEx;
		myPort = myEx.portNum();
		log = new ArrayList<ExchangeMessage>();
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
			System.out.println("Exception in DummyExchangeReceiver run: " + e.getMessage());
		}
	}

	ExchangeMessage readMessage(Socket client) {
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
			return (ExchangeMessage) broker.parse(raw_message);
		}
		catch (Exception e) {
			System.out.println("Exception in DummyExchangeReceiver readMessage: " + e.getMessage());
			return new BuyMessage();
		}
	}

	void printLog() {
		synchronized (log) {
			System.out.println("Printing messages received by: " + myEx.textString);
			for (ExchangeMessage msg : log) {
				msg.print();
				System.out.println();
			}
		}
	}
}





