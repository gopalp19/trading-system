package com.mpcs.distributed.systems;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.atomic.*;
import superpeer.SuperPeer;

import resourcesupport.Exchange;

/**
 * ElectionManager handles this exchange's election responsibilities, including creating a super peer when needed.
 * @author Alan
 *
 */
public class ElectionManager {
	static int timeout = 1000; // ms timeout
	
	static AtomicBoolean inElection = new AtomicBoolean(false);
	static Thread thread;
	
	public static void handleElectionRequest() {
		synchronized (inElection) {
			if (inElection.get()) {
				return;
			} else {
				inElection.set(true);
			}
		}
		for (Exchange e : ExchangeServer.neighborPeers) {
			if (e.ordinal() > ExchangeServer.exchange.ordinal()) {
				if (checkAlive(e)) {
					synchronized(inElection) {
						inElection.set(false);
						return;
					}
				}
			}
		}
		// no higher ranking neighbors are alive; so try to create superpeer
		try {
			ServerSocket serverSocket = new ServerSocket(ExchangeServer.exchange.continent().portNum());
			ExchangeServer.superPeer = new SuperPeer(ExchangeServer.exchange);
			ExchangeServer.superPeer.start();
		} catch (Exception e) {
			// could not create ServerSocket/superPeer. Probably because already exists.
			ExchangeServer.superPeer = null;
		}
	}
	
	private static boolean checkAlive(Exchange e) { 
		try (
			Socket socket = new Socket("localhost", e.portNum());
			PrintWriter pw = new PrintWriter(socket.getOutputStream());
			Scanner scan = new Scanner(socket.getInputStream())
		) {
			socket.setSoTimeout(timeout);
			pw.println("exchange:" + ExchangeServer.exchange);
			String reply = scan.nextLine();
			if (reply.equals("stop")) return true;
		} catch (Exception exception) {
			return false;
		}
		return false;
	}
}
