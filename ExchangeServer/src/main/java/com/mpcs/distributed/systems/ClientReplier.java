package com.mpcs.distributed.systems;

import java.io.*;
import messenger.*;
import messenger.mutualfundmessage.MutualFundResultMessage;
import resourcesupport.Exchange;

import java.util.ArrayList;
import java.util.concurrent.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;

/**
 * All outgoing writes to client sockets should be handled through a single instance of ClientReplier.
 * @author Alan
 */
public class ClientReplier implements Runnable {
	public MessageQueue messageQueue;
	public ConcurrentHashMap<String, Socket> socketBank;
	public Thread thread;	
	
	public ClientReplier() {
		messageQueue = new MessageQueue();
		socketBank = new ConcurrentHashMap<>();
		thread = new Thread(this);
		thread.setDaemon(true);
		thread.start();
	}
	
	public void run() {
		while (true) {
			Socket socket = null;
			String name = null;
			Message m = messageQueue.take();
			try {
				if (m.getClass() == BuyResultMessage.class) {
					BuyResultMessage b = (BuyResultMessage) m;
					name = b.buyerUserName;
					socket = socketBank.get(name);
					
				} else if (m.getClass() == SellResultMessage.class) {
					SellResultMessage s = (SellResultMessage) m;
					name = s.sellerUserName;
					socket = socketBank.get(name);
					
				} else if (m.getClass() == MutualFundResultMessage.class) {
					MutualFundResultMessage mf = (MutualFundResultMessage) m;
					name = mf.buyerUserName;
					socket = socketBank.get(name);
				}
			} catch (NullPointerException e) {
				System.out.println("Cannot send message to null userName");
				continue;					
			}
			if (socket == null) {
				System.out.println("No known socket for user " + name);
				continue;
			}
			try {
				PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
				ArrayList<String> lines = m.toStringList();
				for (String line : lines) {
					pw.println(line);
				}
				pw.println();
			} catch (IOException e) {
			    System.out.println("IOException when writing to client " + name);
	            continue;
	        }
		}
	}
}
