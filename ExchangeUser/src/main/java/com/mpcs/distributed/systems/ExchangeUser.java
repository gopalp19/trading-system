package com.mpcs.distributed.systems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ExchangeUser {

	public static void main(String[] args) throws UnknownHostException, IOException {

        System.out.println("Enter connection info:");
        
    	BufferedReader keyboardStream = new BufferedReader(new InputStreamReader(System.in));
		String userInput = keyboardStream.readLine();
        String[] userInputSplit = userInput.split("\\s+");
        
        //exchangeName should be used to find port
        String exchangeName = userInputSplit[1];
        String userName = userInputSplit[2];

        Socket s = new Socket("localhost", 9090);
        
	    //Stream from master
	    BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
	    
	    //Stream from our client
	    PrintWriter out = new PrintWriter(s.getOutputStream(), true);

    	out.println("userName:" + userName);
    	
    	String responseLine;
	    while ((responseLine = in.readLine()) != null) {
	    	System.out.println(responseLine);
	    	
	    	userInput = keyboardStream.readLine();
	    	out.println(userInput);
	    	if("exit".equals(userInput)){
	    		break;
	    	}
	    }
	    
        System.out.println("Closing connection to server ordering system!");
        
        s.close();

	}
	
}
