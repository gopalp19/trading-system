package superpeer;

import messenger.Message;
import messenger.MessageQueue;
import resourcesupport.Continent;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.time.LocalDateTime;


class SuperPeerLogger extends Thread {
	Continent myContinent;
	MessageQueue logQueue;
	String logPath;

	SuperPeerLogger(Continent myContinent, MessageQueue logQueue, String logPath) {
		this.myContinent = myContinent;
		this.logQueue = logQueue;
		this.logPath = logPath;
	}

	public void run() {
		String logStamp = LocalDateTime.now().toString().replaceAll(":|\\.","-");

		String logName = logPath + myContinent + "_log_" + logStamp + ".txt";

		try (
				PrintWriter writer = new PrintWriter(Files.newBufferedWriter(Paths.get(logName)));
			)
		{
			while (true) {
				Message next = logQueue.take();
				ArrayList<String> text = next.toStringList();
				for (String line : text) {
					writer.println(line);
				}
				writer.println();
				writer.flush();
			}
		}
		catch (Exception e) {
			System.err.println(e);
			return;
		}
	}
}