package exchangeservertest;

import resourcesupport.*;
import messenger.*;
import java.net.Socket;
import java.util.ArrayList;
import java.io.*;

public class DummyExchangeUserSender {
	private Exchange myEx;
	private String username;

	public DummyExchangeUserSender(Exchange myEx, String username) {
		this.myEx = myEx;
		this.username = username;
	}

	public void send(Message msg) {
		try (
				Socket socket = new Socket("localhost", superPort);
				PrintWriter out = new PrintWriter(socket.getOutputStream());
			)
		{
			ArrayList<String> raw_message = msg.toStringList();

				for (String line : raw_message) {
					out.println(line);
				}
				out.println();
		}
		catch (Exception e) {
			System.out.println("Exception in DummyExchangeUserSender send: " + e.getMessage());
		}
	}
}