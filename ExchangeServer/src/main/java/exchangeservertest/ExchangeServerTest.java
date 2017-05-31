package exchangeservertest;

import messenger.*;
import resourcesupport.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Spawn a number of DummyUsers that place random orders for an amount of time.
 * Running command:
 * java ExchangeServerTest <num_users> <ms_duration> <BUY | SELL> [optional_exchange_name] // if no exchange specified, will be chosen randomly for each user
 * 
 * OR
 * 
 * java -o // for Sam's original recipe
 */
class ExchangeServerTest {
	static boolean original = false;
	static boolean buy = true;
	static int numUsers = 0; // num users 
	static int duration = 0; // num ms to run
	static Exchange exchange = Exchange.LONDON;
	static String username = "anon";

	static volatile boolean end = false;
	
	public static void main(String[] args) {
		if (!parseArgs(args)) return;

		if (original) {
			doOriginal();
		} else {
			doNew();
		}		
	}
	
	private static void doNew() {
		System.out.println("Creating " + numUsers + " DummyUsers to run for " + duration + " ms");
		Random rand = new Random();
		
		for (int i = 0; i < numUsers; i++) {
			Exchange ex = exchange == null ? Exchange.values()[rand.nextInt(Exchange.values().length)] : exchange;
			System.out.println("DummyUser " + i + " locates at " + ex);
			try {
				DummyExchangeUser user = new DummyExchangeUser(ex, "anon", false);
				user.setDaemon(true);
				user.start();
			} catch (IOException e) {
				System.out.println("IOException. Exiting. Is " + ex + " exchange even up yet?");
				return;
			}
		}

		// shut down after waiting duration
		ScheduledThreadPoolExecutor stpe = new ScheduledThreadPoolExecutor(1);
		stpe.schedule(new Runnable() {public void run() {end = true;System.out.println("bye");}}, duration, TimeUnit.MILLISECONDS);
		stpe.shutdown();
	}

	/**
	 * Sam's original recipe
	 */
	private static void doOriginal() {
		System.out.println("Running Sam's original");
		DummyExchangeUser user;
		try {
			user = new DummyExchangeUser(Exchange.LONDON, "user1", true);
		} catch (Exception e) {
			System.out.println("Connection could not be formed: " + e.getMessage());
			return;
		}

		user.start();
		BuyMessage buy = new BuyMessage(Exchange.LONDON, username, Stock.BP_PLC, 300, LocalDateTime.now(), null);
		user.send(buy);
	}
	
	private static boolean parseArgs(String[] args) {
		try {			
			if (args[0].equals("-o")) {
				original = true;
				return true;
			}
			original = false;
			numUsers = Integer.parseInt(args[0]); 
			duration = Integer.parseInt(args[1]);
			switch (args[2]) {
				case "BUY":
					break;
				case "SELL":
					buy = false;
					break;
				default:
					throw new Exception();
			}
			if (args.length > 3) {
				exchange = Exchange.valueOf(args[3]);
			} else {
				exchange = null;
			}
		} catch (Exception e) {
			System.out.println("Error: please run as \"java ExchangeServerTest <num_users> <ms_duration> <BUY | SELL> [optional_exchange_name]\"");
			System.out.println("Else: run as \"java ExchangeServerTest -o\" to do original hard-coded procedure");
			return false;
		}
		return true;
	}
}