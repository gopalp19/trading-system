package messenger;

import java.util.concurrent.LinkedBlockingQueue;

// Probably just a LinkedBlockingQueue
// Might need addtional functionality
public class MessageQueue {
	private LinkedBlockingQueue<Message> queue;

	public Message take() 
	{
		while (true) {
			try {
				return queue.take();
			}
			catch (InterruptedException e) {
				;
			}
		}
	}

	public void add(Message to_add) {
		while (true) {
			try {
				queue.put(to_add);
				return;
			}
			catch (InterruptedException e) {
				;
			}
		}
	}

}