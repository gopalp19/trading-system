package com.mpcs.distributed.systems.services;

import com.mpcs.distributed.systems.ElectionManager;
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
        try (Scanner inputStream = new Scanner(new InputStreamReader(clientSocket.getInputStream()))){
        	PrintStream outputStream = new PrintStream(clientSocket.getOutputStream());

        	//TODO - Logging mechanism should start here before loop

        	//Should be [client|exchange]:[userName|exchangeName] only
        	String clientsInput = inputStream.nextLine();
        	String[] splitted = clientsInput.split(":");
        	if (splitted[0].equals("exchange")) {
        		// only time a peer contacts another peer directly is during election
        		try {
        			outputStream.println("stop");
            		ElectionManager.handleElectionRequest();
            		return;
        		} catch (IndexOutOfBoundsException | IllegalArgumentException | NullPointerException e) {
                    System.out.println("Error: specified invalid exchange name. See resourcesupport.Exchange for list of valid names.");	
                    return;
        		}
        	} else if (!splitted[0].equals("client")) {
        		System.out.println("Error: expecting first message on socket to follow format: [client|exchange]:[userName|exchangeName]");
        		System.out.println("Received instead: " + clientsInput);
        		System.exit(1);;
        	}
            String userName = splitted[1];

    		boolean userValid = userService.isUserValid(userName, ExchangeServer.exchange.toString());
    	    if(userValid){
        		outputStream.println("Login succesful. Connection established to " + ExchangeServer.exchange);
        		ExchangeServer.clientReplier.socketBank.put(userName, clientSocket);
    		}else{
        		outputStream.println("Login unssuccesful!");
        		return;
    		}
        	
        	//Exchange server is always listening to client in connection
    		ArrayList<String> stringList = new ArrayList<>();
            while (inputStream.hasNextLine()) {
            	clientsInput = inputStream.nextLine();
            	if (!clientsInput.isEmpty()) {
            		stringList.add(clientsInput);
            	} else {
            		Message message = MessageBroker.parse(stringList);
            		Message outMessage;
            		stringList = new ArrayList<>();
                	//processCommand(clientsInput);
            		if (message.getClass() == BuyMessage.class) {
            			// TODO - do buy request
                        // temporarily auto-reply any buy request with 0 bought
            			BuyMessage b = (BuyMessage) message;
            			BuyResultMessage br = new BuyResultMessage();
            			br.buyerUserName = b.buyerUserName;
            			br.buyerExchange = b.buyerExchange;
            			br.quantityBought = 0;
            			br.stock = b.stock;
            			outMessage = br;
            		} else if (message.getClass() == SellMessage.class) {
            			// TODO - do sell request
                        // temporarily auto-confirm any sell request with 0 sold
            			SellMessage s = (SellMessage) message;
            			SellResultMessage sr = new SellResultMessage();
            			sr.sellerUserName = s.sellerUserName;
            			sr.sellerExchange = s.sellerExchange;
            			sr.quantitySold = 0;
            			sr.stock = s.stock;
            			outMessage = sr;
            		} else {
            			continue;
            		}
        			ExchangeServer.clientReplier.messageQueue.add(outMessage);   
            	}           	
            }        		
        } catch (MalformedMessageException e) {
        	System.out.println("Malformed message from client");
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
