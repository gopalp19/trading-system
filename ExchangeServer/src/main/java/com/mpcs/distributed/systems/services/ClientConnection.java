package com.mpcs.distributed.systems.services;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

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

        	BufferedReader inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        	PrintStream outputStream = new PrintStream(clientSocket.getOutputStream());

        	//Logging mechanism should start here before loop

        	//Should be userName first
        	String clientsInput = inputStream.readLine();

            String[] userName = clientsInput.split(":");

    		boolean userValid = userService.isUserValid(userName[1], "NYSE");
    		if(userValid){
        		outputStream.println("Login succesful. Connection established to NYSE!");
    		}else{
        		outputStream.println("Login unssuccesful!");
        		//TODO: Throw error? Exit?
    		}
        	
        	//Exchange server is always listening to client in connection
            while (true) {
            	clientsInput = inputStream.readLine();
            	//TODO: Client has given exchange a command
            	//processCommand(clientsInput);


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
