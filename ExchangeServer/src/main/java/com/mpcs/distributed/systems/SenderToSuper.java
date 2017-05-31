package com.mpcs.distributed.systems;

import messenger.*;
import resourcesupport.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Class for forwarding messages to SuperPeer
 * @author Alan
 */
public class SenderToSuper implements Runnable {
	int timeout = 5000; // time to wait for election to complete before trying again
	public MessageQueue queue = new MessageQueue();
	public Continent continent;
	public Thread thread;
	
	public SenderToSuper(Exchange exchange) {
		continent = exchange.continent();
		thread = new Thread(this);
		thread.setDaemon(true);
		thread.start();
	}
	
	public void run() {
		while (true) {
			Message message = queue.take();
			while (true) {
				try (Socket socket = new Socket("localhost", continent.portNum());
						PrintWriter pw = new PrintWriter(socket.getOutputStream())
				) {
					for (String str : message.toStringList()) {
						pw.println(str);
					}
					pw.println();
					break;
				} catch (IOException ioe) {
					ExchangeServer.superChecker.request();
					try {
						Thread.sleep(timeout);
					} catch (InterruptedException ie) {
					}
				}
			}
		}
	}
}
