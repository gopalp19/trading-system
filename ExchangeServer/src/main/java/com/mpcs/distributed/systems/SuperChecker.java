package com.mpcs.distributed.systems;

import resourcesupport.Exchange;

import java.io.*;
import java.net.ServerSocket;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class SuperChecker implements Runnable {
	private int refractory = 5000; // ms refractory period
	private AtomicLong lastRun = new AtomicLong(0); // time of last executed
	private int period = 1000;
	private Exchange exchange;
	
	SuperChecker(Exchange e) {
		exchange = e;
		ScheduledThreadPoolExecutor stpe = new ScheduledThreadPoolExecutor(1);
		stpe.scheduleAtFixedRate(this, (long) 0, period, TimeUnit.MILLISECONDS);
	}
	
	public void run() {
		synchronized(lastRun) {
			long current = System.currentTimeMillis();
			if (current - lastRun.get() < refractory) return;
			try (ServerSocket ss = new ServerSocket(exchange.continent().portNum())) {
				System.out.println("Detected no superpeer for: " + exchange.continent());
				// succeed then no superpeer
				lastRun.getAndSet(current);			
				ElectionManager.handleElectionRequest();
			} catch (IOException e) {
				// fail then has superpeer
				return;
			}
		}
	}
	
	public void request() {
		synchronized(lastRun) {
			long current = System.currentTimeMillis();
			if (current - lastRun.get() < refractory) return;
			ElectionManager.handleElectionRequest();
			lastRun.getAndSet(current);
		}
	}
}
