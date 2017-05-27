
package superpeer;
import java.util.List;
import java.io.PrintWriter;
import java.net.Socket;
import resourcesupport.*;
import messenger.*;
import java.util.ArrayList;

// Class that sends messages from send queue to other peers
public class SuperPeerSender extends Thread {
	private MessageQueue toSendQueue;
	private Exchange myExchange;

	SuperPeerSender(Exchange myExchange, MessageQueue toSendQueue) {
		this.toSendQueue = toSendQueue;
		this.myExchange = myExchange;
	}

	public void run() {
		while (true) {
			Message next = toSendQueue.take();
			int destinationPort = getNextHopPort(next);
			try (
				Socket socket = new Socket(getIp(next), destinationPort); 
				PrintWriter out = new PrintWriter(socket.getOutputStream());
				)
			{
				ArrayList<String> raw_message = next.toStringList();

				for (String line : raw_message) {
					out.println(line);
				}
				out.println();
			}
			catch (Exception e) {
				System.out.println("Message destined for port " + destinationPort 
					+ " could not be sent.");
			}
		}
	}

	// TODO
	String getIp(Message next) {
		return "localhost";
	}

	// Destination if super peer network is complete graph
	int getNextHopPort(Message next) {
		if (next instanceof ExchangeMessage) {
			Exchange destinationExchange = ((ExchangeMessage)next).getDestination();
			return getExchangeNextPort(destinationExchange);
		}
		else {
			Continent destinationContinent = ((SuperPeerMessage)next).getDestination();
			return getContinentNextPort(destinationContinent);
		}
	}

	int getExchangeNextPort(Exchange destinationExchange) {
		// If managed by another super peer
		if (destinationExchange.continent() != myExchange.continent())
			return destinationExchange.continent().portNum();
		// If managed by this super peer
		else
			return destinationExchange.portNum();
	}

	int getContinentNextPort(Continent destinationContinent) {
		return destinationContinent.portNum();
	}
}



