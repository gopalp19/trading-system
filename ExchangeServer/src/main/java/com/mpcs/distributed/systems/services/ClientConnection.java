package com.mpcs.distributed.systems.services;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.ApplicationContext;

import com.mpcs.distributed.systems.ElectionManager;
import com.mpcs.distributed.systems.ExchangeServer;
import com.mpcs.distributed.systems.application.AppContext;

import messenger.*;
import messenger.mutualfundmessage.*;
import resourcesupport.Continent;
import resourcesupport.Stock;

public class ClientConnection extends Thread{
	
    public ServerSocket serverSocket;
    private Socket clientSocket;
    private String userName;
    private UserService userService;
    private StockService stockService;
    
    public static ConcurrentHashMap<String, Integer> idToQty = new ConcurrentHashMap<>();

    public ClientConnection(ServerSocket serverSocket) throws IOException{
	    this.serverSocket = serverSocket;
    	clientSocket = serverSocket.accept();
    	
    	ApplicationContext context = AppContext.getApplicationContext();
		this.userService = (UserService) context.getBean("userService");
		this.stockService = (StockService) context.getBean("stockService");
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
        		simpleReceive(inputStream, clientsInput); // receiving a forwarded message from the superpeer
        		return;
//        		System.out.println("Error: expecting first message on socket to follow format: [client|exchange]:[userName|exchangeName]");
//        		System.out.println("Received instead: " + clientsInput);
//        		System.exit(1);;
        	}
            userName = splitted[1];
            
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
    		boolean waiting = true;
            while (inputStream.hasNextLine()) {
            	clientsInput = inputStream.nextLine();
            	if (!clientsInput.isEmpty()) {
                	if (waiting) {
                		System.out.println("Received new message from client " + userName);
                		waiting = false;
                	}
            		stringList.add(clientsInput);
            	} else {
            		waiting = true;
            		Message message = MessageBroker.parse(stringList);
            		stringList = new ArrayList<>();
                	//processCommand(clientsInput);
            		if (message.getClass() == BuyMessage.class) {
            			BuyMessage b = (BuyMessage) message;
               			b.timeStamp = ExchangeServer.exchangeTimer.getTime();
               			b.orderID = System.currentTimeMillis() + ":" + userName;
               		 	if (b.stock.exchange() == ExchangeServer.exchange) {
                			BuyResultMessage br = new BuyResultMessage();
                			br.buyerUserName = b.buyerUserName;
                			br.buyerExchange = b.buyerExchange;
                			br.quantityBought = 0;
                			br.stock = b.stock;
                			br.timeStamp = b.timeStamp;                			
                			stockService.buyStock(b, br);
            				System.out.println("Replied to client");
                			ExchangeServer.clientReplier.messageQueue.add(br);               				
            			} else {
            				ExchangeServer.senderToSuper.queue.add(b);
            				System.out.println("Forwarded to superpeer");
            			}
            		} else if (message.getClass() == SellMessage.class) {
                        // temporarily auto-confirm any local sell request with 0 sold
            			SellMessage s = (SellMessage) message;
            			s.timeStamp = ExchangeServer.exchangeTimer.getTime();
               			s.orderID = System.currentTimeMillis() + ":" + userName;
            			if (s.stock.exchange() == ExchangeServer.exchange) {
                			SellResultMessage sr = new SellResultMessage();
                			sr.sellerUserName = s.sellerUserName;
                			sr.sellerExchange = s.sellerExchange;
                			sr.quantitySold = 0;
                			sr.stock = s.stock;
                			sr.timeStamp = s.timeStamp;
                			stockService.sellStock(s, sr);
            				System.out.println("Replied to client");
                			ExchangeServer.clientReplier.messageQueue.add(sr);               				
            			} else {
            				ExchangeServer.senderToSuper.queue.add(s);
            				System.out.println("Forwarded to superpeer");
            			}
            		} else if (message.getClass() == MutualFundBuyMessage.class) {
            			MutualFundBuyMessage m = (MutualFundBuyMessage) message;
            			m.timeStamp = ExchangeServer.exchangeTimer.getTime();
               			m.orderID = System.currentTimeMillis() + ":" + userName;
           				ExchangeServer.senderToSuper.queue.add(m);
         				System.out.println("Forwarded to superpeer");
            		} else {
            			continue;
            		}
            	}           	
            }
            System.out.println("InputStream out of lines");
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
            if (userName != null) {
            	System.out.println("Succesfully closed connection with Client " + userName);
            }
        }
   
    }
	

	// receive a forwarded message from a superpeer
	void simpleReceive(Scanner scan, String firstLine) {
    	//Exchange server is always listening to client in connection
		ArrayList<String> stringList = new ArrayList<>();
		stringList.add(firstLine);
		boolean waiting = true;
        while (scan.hasNextLine()) {
        	String input = scan.nextLine();
        	if (!input.isEmpty()) {
            	if (waiting) {
            		System.out.println("Received new message from superpeer");
            		waiting = false;
            	}        		
        		stringList.add(input);
        	} else {
        		waiting = true;
        		Message message = MessageBroker.parse(stringList);
        		stringList = new ArrayList<>();
            	//processCommand(clientsInput);
        		if (message.getClass() == BuyMessage.class) {
        			BuyMessage b = (BuyMessage) message;
        			BuyResultMessage br = new BuyResultMessage();
        			br.buyerUserName = b.buyerUserName;
        			br.buyerExchange = b.buyerExchange;
        			br.quantityBought = 0;
        			br.stock = b.stock;
        			ExchangeServer.exchangeTimer.update(b.timeStamp);
        			br.timeStamp = ExchangeServer.exchangeTimer.getTime();
        			stockService.buyStock(b, br);
    				ExchangeServer.senderToSuper.queue.add(br);
    				System.out.println("Forwarded to superpeer");
    				
        		} else if (message.getClass() == SellMessage.class) {
        			SellMessage s = (SellMessage) message;
        			SellResultMessage sr = new SellResultMessage();
        			sr.sellerUserName = s.sellerUserName;
        			sr.sellerExchange = s.sellerExchange;
        			sr.quantitySold = 0;
        			sr.stock = s.stock;
        			ExchangeServer.exchangeTimer.update(s.timeStamp);
        			sr.timeStamp = ExchangeServer.exchangeTimer.getTime();
        			stockService.sellStock(s, sr);
    				ExchangeServer.senderToSuper.queue.add(sr);
    				System.out.println("Forwarded to superpeer");
        			ExchangeServer.clientReplier.messageQueue.add(sr);               		
        			
        		} else if (message.getClass() == SellResultMessage.class) {
    				System.out.println("Replied to client");
        			ExchangeServer.clientReplier.messageQueue.add(message);               				       			
        			
        		} else if (message.getClass() == BuyResultMessage.class) {
    				System.out.println("Replied to client");
        			ExchangeServer.clientReplier.messageQueue.add(message);               				        			
        			
        		} else if (message.getClass() == MutualFundReserveMessage.class) {
        			MutualFundReserveMessage m = (MutualFundReserveMessage) message;
        			int result = stockService.reserveStock(m.stock, m.quantity);
        			MutualFundReserveResponseMessage mr = new MutualFundReserveResponseMessage();
        			mr.superpeer = m.superpeer;
        			mr.stock = m.stock;
        			mr.quantity = m.quantity;
        			mr.timeStamp = ExchangeServer.exchangeTimer.getTime();
        			mr.orderID = m.orderID;
        			mr.reservationConfirmed = result > 0;
        			idToQty.put(m.orderID + m.stock, mr.reservationConfirmed ? mr.quantity : 0);
       				System.out.println("Reserved " + mr.quantity + " units of " + mr.stock + ": " + mr.reservationConfirmed);
        			ExchangeServer.senderToSuper.queue.add(mr);
        			
        		} else if (message.getClass() == MutualFundUpdateMessage.class) {
        			MutualFundUpdateMessage m = (MutualFundUpdateMessage) message;
        			stockService.unreserveStock(m.stock, idToQty.get(m.orderID + m.stock));
    				if (m.doCommit) {
    					BuyMessage tempBuy = new BuyMessage();
    					tempBuy.stock = m.stock;
    					tempBuy.quantity = idToQty.get(m.orderID + m.stock);
    					stockService.buyStock(tempBuy, new BuyResultMessage());
    					System.out.println("MF bought " + idToQty.get(m.orderID + m.stock) + " units of " + m.stock);
        			} else {
        				System.out.println("Unreserved " + idToQty.get(m.orderID + m.stock) + " units of " + m.stock);
        			}
        		} else if (message.getClass() == MutualFundResultMessage.class) {
        			System.out.println("Replying to client about MF");
        			ExchangeServer.clientReplier.messageQueue.add(message);
        			
        		} else {
        			continue;
        		}
        	}           	
        }
        System.out.println("Closing superPeer message");
	}
}
