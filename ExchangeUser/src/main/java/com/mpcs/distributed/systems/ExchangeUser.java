package com.mpcs.distributed.systems;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import resourcesupport.*;
import java.util.*;
import messenger.*;
import messenger.mutualfundmessage.MutualFundBuyMessage;

public class ExchangeUser {
	static Exchange exchange = null;
	static String userName = null;
	
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		if (!validArgs(args)) {
			return;
		}
        Socket s = new Socket("localhost", exchange.portNum());
	    //Stream from master
	    Scanner in = new Scanner(s.getInputStream());
	    new ExchangeUserListener(in);
	    //Stream from our client
	    PrintWriter out = new PrintWriter(s.getOutputStream(), true);
    	out.println("client:" + userName);
    	
    	System.out.println("Enter input in format: <BUY|SELL|MF> <stock_name> <quantity>");
    	Scanner userReader = new Scanner(System.in);
	    while (userReader.hasNextLine()) {
	    	String userLine = userReader.nextLine();
	    	System.out.println(">>> Received user line: " + userLine);
	    	if(userLine.equals("exit")) {
	    		break;
	    	}
	    	String[] terms = userLine.split("[\\s]+");
	    	try {
	    		Message m = null;
	    		switch (terms[0].toLowerCase()) {
	    		    case "buy":
	    		    	BuyMessage bm = new BuyMessage(exchange, userName, Stock.valueOf(terms[1]), Integer.parseInt(terms[2]), null, null);
	    		    	m = (Message) bm;
	    		        break;
	    		    case "sell":
	    		    	SellMessage sm = new SellMessage(exchange, userName, Stock.valueOf(terms[1]), Integer.parseInt(terms[2]), null, null);
	    		    	m = (Message) sm;
	    		    	break;
	    		    case "mf":
	    		    	MutualFundBuyMessage mfm = new MutualFundBuyMessage(exchange, userName, MutualFund.valueOf(terms[1]), Integer.parseInt(terms[2]), null, null);
	    		    	m = (Message) mfm;
	    		    	break;
	    		    default:
	    		    	System.out.println("Only know BUY and SELL commands, not " + terms[0]);
	    		}
	    		for (String line : m.toStringList()) {
	    			out.println(line);
	    		}
	    		out.println();
	    	} catch (NullPointerException | IllegalArgumentException e) {
	    		System.out.println("Invalid line");
				System.out.println("See resourcesupport.Stock.java for list of valid stock names");
	    	} catch (Exception e) {
	    		System.out.println("Invalid line");
	    		System.out.println("Expected input in format: <BUY|SELL> <stock_name> <quantity>");
	    	}
	    }
	    
        System.out.println("Closing connection to server ordering system!");
        userReader.close();
        s.close();
	}
	
	/**
	 * Parse and validate command-line arguments.
	 * @param args - command line arguments
	 * @return true if valid, false otherwise
	 */
	static boolean validArgs(String[] args) {
		try {
			exchange = Exchange.valueOf(args[0]);
			userName = args[1];
		} catch (Exception e) {
			System.out.println("Process started with invalid arguments.");
			System.out.println("Expected process start with format: java ExchangeUser <exchangeName> <userName>");
			System.out.println("See resourcesupport.Exchange.java for list of valid exchangeNames");
			return false;
		}
		return true;
	}
	
}
