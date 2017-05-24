package com.mpcs.distributed.systems;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.*;
import java.util.*;

/**
 * Periodically increment the clock or set it forward when commanded.
 * @author Alan
 */
public class ExchangeTimer extends TimerTask {
	private static int period = 5000; // real ms between simulated hours
	private static LocalDateTime zeroTime = LocalDateTime.of(2016, 1, 1, 8, 0);
	
	private AtomicInteger ticks = new AtomicInteger(0);
	
	public ExchangeTimer() {
		System.out.println("Starting timer ...");
		Timer timer = new Timer(true);
		timer.schedule(this,  0,  period);
	}
	
	/** regularly update the clock ticks */
	public void run() {
		synchronized(ticks) {
			ticks.incrementAndGet();
			System.out.println("tick: " + zeroTime.plusHours(ticks.get()));
		}
	}
	
	/** compare new ticks with current ticks and update if new ticks is ahead. */
	public void update(int newTicks) {
		synchronized(ticks) {
			int oldTicks = ticks.get();
			if (newTicks > oldTicks) {
				ticks.getAndSet(newTicks);
			}
		}
	}
	
	public void update(LocalDateTime newTime) {
		update(ticksSinceStart(newTime));
	}
	
	public void update(String string) {
		if (string == null) return;
		update(LocalDateTime.parse(string));
	}
	
	/** convert local date time to ticks */
	public static int ticksSinceStart(LocalDateTime end) {
		long diff = zeroTime.until(end, ChronoUnit.HOURS);
		return (int) diff;
	}
	
	/** get current time */
	public LocalDateTime getTime() {
		synchronized(ticks) {
			return zeroTime.plusHours(ticks.get());			
		}
	}
}
