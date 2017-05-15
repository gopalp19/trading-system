package com.mpcs.distributed.systems;

import java.util.concurrent.atomic.*;

/**
 * ElectionManager handles this exchange's election responsibilities, including creating a super peer when needed.
 * @author Alan
 *
 */
public class ElectionManager implements Runnable {
	public static AtomicBoolean inElection = new AtomicBoolean(false);
	public static Thread thread;
	
	ElectionManager() {
		thread = new Thread(this);
		thread.setDaemon(true);
		thread.start();
	}
	
	public void run() {
		
//		while (true) {
//			
//		}
	}
}
