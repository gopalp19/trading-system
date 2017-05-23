package superpeertest;

import resourcesupport.*;
import messenger.*;
import java.net.Socket;
import java.util.ArrayList;
import java.io.*;

public class DummyExchangeSender {
	private Exchange myEx;
	private int superPort;

	public DummyExchangeSender(Exchange myEx) {
		this.myEx = myEx;
		superPort = myEx.continent().portNum();
	}

	public void send(ExchangeMessage msg) {
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
			System.out.println("Exception in DummyExchangeSender send: " + e.getMessage());
		}
	}
}