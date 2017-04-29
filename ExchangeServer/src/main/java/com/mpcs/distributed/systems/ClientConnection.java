package com.mpcs.distributed.systems;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientConnection extends Thread{
	
    public ServerSocket serverSocket;
    private Socket clientSocket;

    public ClientConnection(ServerSocket serverSocket) throws IOException{
	    System.out.println("Parent waiting to connect to client");
	    this.serverSocket = serverSocket;
    	clientSocket = serverSocket.accept();
    }
    
	public void run() {
        try {
    	    System.out.println("Parent connected to client!");

        	BufferedReader inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        	PrintStream outputStream = new PrintStream(clientSocket.getOutputStream());

        	//Logging mechanism should start here before loop
        	
        	//Exchange server is always listening to client in connection
            while (true) {
            	String clientsInput = inputStream.readLine();
            	//TODO: Client has given exchange a command
            	//processCommand(clientsInput);
        	    System.out.println("Other client said" + clientsInput);


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
