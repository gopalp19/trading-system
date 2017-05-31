package exchangeservertest;

import resourcesupport.*;
import messenger.*;
import java.net.*;
import java.time.LocalDateTime;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class DummyExchangeUser extends Thread {
	ArrayList<Stock> localstocks;
	private boolean original = true;
	private Exchange myEx;
	private int myPort;
	private ArrayList<Message> log;
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	private String username;

	// original == true, if you want to run Sam's original
	public DummyExchangeUser(Exchange myEx, String username, boolean original) throws IOException {
		myEx.getStocks();
		this.myEx = myEx;
		this.original = original;
		myPort = myEx.portNum();
		log = new ArrayList<Message>();
	 	socket = new Socket("localhost", myEx.portNum());
		this.username = username;
		localstocks = myEx.getStocks();

		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out.println("client:" + username);
		} catch (IOException e) {
			System.out.println("Exception in DummyExchangeUser: " + e);
		}
	}

	public void run() {
		if (original) {
			doOriginal();
		} else {
			doNew();
		}
	}
	
	/**
	 * do Sam's original
	 */
	private void doOriginal() {
		try {
			MessageBroker broker = new MessageBroker();
			String line;
			while (true) {
				ArrayList<String> raw_message = new ArrayList<String>();
				line = in.readLine();
				while (!line.isEmpty()) {// messages finished by newline
					raw_message.add(line);
					line = in.readLine();
				}
				Message full_message = broker.parse(raw_message);
				full_message.print();
				log.add(full_message);
			}
		} catch (Exception e) {
			System.out.println("Exception in DummyExchangeUser run: " + e.getMessage());
		}		
	}

	public void send(Message msg) {
		try {
			ArrayList<String> raw_message = msg.toStringList();
			for (String line : raw_message) {
				out.println(line);
			}
			out.println();
		} catch (Exception e) {
			System.out.println("Exception in DummyExchangeUser send: " + e.getMessage());
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
	
	/** 
	 * new superpeer test
	 */
	void doNew() {
		Random r = new Random();
		int num = localstocks.size();
		while (!ExchangeServerTest.end) {
			Message m;
			if (ExchangeServerTest.buy) {
				m = new BuyMessage(myEx, username, localstocks.get(r.nextInt(num)), 1, null, null);
			} else {
				m = new SellMessage(myEx, username, localstocks.get(r.nextInt(num)), 1, null, null);
			}
			send(m);
			try {
				String line = in.readLine();
				while (!line.isEmpty()) {
					System.out.println(line);
					line = in.readLine();
				}
			} catch (IOException e) {
				System.out.println("closing: ioexception");
				return;
			}
		}
	}
}





