
package messenger;
import java.util.List;
import java.io.PrintWriter;
import java.net.Socket;

// Class that sends messages from send queue to other peers
public class Sender extends Thread {
	MessageQueue sendQueue;
	Socket left;
	Socket right;

	Sender(MessageQueue sendQueue, Socket left, Socket right) {
		this.sendQueue = sendQueue;
		this.left = left;
		this.right = right;
	}

	public void run() {
		while (true) {
			Message next = sendQueue.take();
			try (
				Socket destination = getDestination(next); 
				PrintWriter out = new PrintWriter(destination.getOutputStream());
				)
			{
				List<String> raw_message = next.toStringList();

				for (String line : raw_message) {
					out.println(line);
				}
			}
			catch (Exception e) {
				System.out.println("Exception in Sender run");
			}
		}
	}

	// TODO
	Socket getDestination(Message next) {
		return right;
	}
}