package com.mpcs.distributed.systems.services;

import com.mpcs.distributed.systems.ExchangeServer;
import java.util.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import messenger.*;

import org.springframework.context.ApplicationContext;

import com.mpcs.distributed.systems.application.AppContext;

public class ClientConnection extends Thread{
	
    public ServerSocket serverSocket;
    private Socket clientSocket;
    
    private UserService userService;

    public ClientConnection(ServerSocket serverSocket) throws IOException{
	    this.serverSocket = serverSocket;
    	clientSocket = serverSocket.accept();
    	
    	ApplicationContext context = AppContext.getApplicationContext();
		this.userService = (UserService) context.getBean("userService");
    }
    
	public void run() {
        try {

        	Scanner inputStream = new Scanner(new InputStreamReader(clientSocket.getInputStream()));
        	PrintStream outputStream = new PrintStream(clientSocket.getOutputStream());

        	//Logging mechanism should start here before loop

        	//Should be [client|exchange]:[userName|exchangeName] only
        	String clientsInput = inputStream.nextLine();
        	String[] splitted = clientsInput.split(":");
        	if (splitted[0].equals("exchange")) {
        		// only time a peer contacts another peer directly is during election
        		// pass this socket to some election manager
        	} else if (!splitted[0].equals("client")) {
        		System.out.println("Error: expecting first message on socket to follow format: [client|exchange]:[userName|exchangeName]");
        		System.out.println("Received instead: " + clientsInput);
        		System.exit(1);;
        	}
            String userName = splitted[1];

    		boolean userValid = userService.isUserValid(userName, ExchangeServer.exchange.toString());
    		
    		if(userValid){
        		outputStream.println("Login succesful. Connection established to " + ExchangeServer.exchange);
    		}else{
        		outputStream.println("Login unssuccesful!");
        		//TODO: Throw error? Exit?
        		return;
    		}
        	
        	//Exchange server is always listening to client in connection
    		ArrayList<String> stringList = new ArrayList<>();
            while (inputStream.hasNextLine()) {
            	clientsInput = inputStream.nextLine();
            	//TODO: Client has given exchange a command
            	if (!clientsInput.isEmpty()) {
            		stringList.add(clientsInput);
            	} else {
            		Message message = MessageBroker.parse(stringList);
            		stringList = new ArrayList<>();
                	//processCommand(clientsInput);
            		if (message.getClass() == BuyMessage.class) {
            			// do buy request
            		} else if (message.getClass() == SellMessage.class) {
            			// do sell request
            		}
            	}           	
            }
        		
        } catch (IOException e) {
        	System.out.println("Connection to client is broken because of: " + e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
            	System.out.println("Failed to close socket: " +  e);
            }
            System.out.println("Succesfully closed connection with Client #");
        }
   
    }
}
