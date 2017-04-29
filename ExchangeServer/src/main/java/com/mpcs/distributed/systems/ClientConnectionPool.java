package com.mpcs.distributed.systems;


import java.io.IOException;
import java.net.ServerSocket;

public class ClientConnectionPool extends Thread{
	
	public ServerSocket serverSocket;

    public ClientConnectionPool(ServerSocket serverSocket){
	    this.serverSocket = serverSocket;
    }
    
	public void run() {
    	
    	while(true){

			try {
	    		ClientConnection peerServer = new ClientConnection(serverSocket);
		        peerServer.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

}
