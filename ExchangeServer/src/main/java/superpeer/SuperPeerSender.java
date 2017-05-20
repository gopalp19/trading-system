
package messenger;
import java.util.List;
import java.io.PrintWriter;
import java.net.Socket;
import resourcesupport.*;

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
			int destinationPort = getNextHopPort(next.destination.continent());
			try (
				Socket socket = new Socket(getIp(destination), destinationPort); 
				PrintWriter out = new PrintWriter(socket.getOutputStream());
				)
			{
				List<String> raw_message = next.toStringList();

				for (String line : raw_message) {
					out.println(line);
				}
			}
			catch (Exception e) {
				System.out.println("Exception in SuperPeerSender run");
			}
		}
	}

	// TODO
	Socket getIp(Exchange next) {
		return "128.135.164.171";
	}

	// Destination if super peer network is complete graph
	int getNextHopPort(Continent destinationContinent) {
		return destinationContinent.portNum();
	}
}