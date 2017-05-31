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
 * All outgoing writes to log should be handled through a single instance of Logger.
 * @author Alan
 */
public class Logger implements Runnable {
	private PrintWriter logger;
	public MessageQueue messageQueue;
	public Thread thread;	
	
	public Logger() {
		messageQueue = new MessageQueue();
        try {
        	new File("Logs").mkdirs();
        	FileOutputStream fos = new FileOutputStream(new File("Logs/" + ExchangeServer.exchange.toString() + "." + System.currentTimeMillis() + ".log"));
            logger = new PrintWriter(fos, true);            
        } catch (IOException e) {
            System.out.println("Error: IOException when trying to start a log file for " + ExchangeServer.exchange);
            return;
        }
		thread = new Thread(this);
		thread.setDaemon(true);
		thread.start();
	}
	
	public void run() {
		while (true) {
			Message m = messageQueue.take();
			ArrayList<String> lines = m.toStringList();
			for (String s : lines) {
				logger.println(s);
			}
			logger.println();
		}
	}
}
