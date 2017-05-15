package com.mpcs.distributed.systems;

import java.util.*;

public class ExchangeUserListener implements Runnable {
	Thread thread;
	Scanner scanner;
	
	ExchangeUserListener(Scanner scanner) {
		this.scanner = scanner;
		thread = new Thread(this);
		thread.setDaemon(true);
		thread.start();
	}
	
	public void run() {
		while (scanner.hasNextLine()) {
			System.out.println(scanner.nextLine());
		}
	}
}
